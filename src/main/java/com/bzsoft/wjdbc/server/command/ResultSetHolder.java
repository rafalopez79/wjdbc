//VJDBC - Virtual JDBC
//Written by Michael Link
//Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.server.command;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.bzsoft.wjdbc.serial.RowPacket;
import com.bzsoft.wjdbc.server.concurrent.Executor;


/**
 * The ResultSetHolder is responsible to hold a reference to an open ResultSet.
 * It reads succeeding RowPackets in a Worker-Thread to immediately return a
 * result when nextRowPacket is called.
 */
public class ResultSetHolder {

	private static final Logger	LOGGER	= Logger.getLogger(ResultSetHolder.class);

	private final Lock				lock;
	private final Executor			executor;
	private final int					rowPacketSize;

	private ResultSet					resultSet;
	private Future<RowPacket>		currentFuture;

	protected ResultSetHolder(final ResultSet resultSet, final Executor exec, final int rowPacketSize, final boolean lastPartReached)
			throws SQLException {
		this.resultSet = resultSet;
		this.executor = exec;
		this.rowPacketSize = rowPacketSize;
		this.lock = new ReentrantLock(true);
		if (!lastPartReached) {
			currentFuture = readNextRowPacket();
		} else {
			currentFuture = null;
		}
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		try {
			lock.lock();
			return resultSet.getMetaData();
		} finally {
			lock.unlock();
		}
	}

	public void close() throws SQLException {
		try {
			lock.lock();
			resultSet.close();
			resultSet = null;
			if (currentFuture != null) {
				currentFuture.cancel(true);
			}
		} finally {
			lock.unlock();
		}
	}

	public RowPacket nextRowPacket() throws SQLException {
		final RowPacket rp;
		if (currentFuture != null) {
			try {
				rp = currentFuture.get();
			} catch (final InterruptedException e) {
				final String msg = "Reader thread interrupted unexpectedly";
				LOGGER.error(msg, e);
				throw new SQLException(msg);
			} catch (final ExecutionException e) {
				LOGGER.error(e.getMessage(), e.getCause());
				throw new SQLException(e.getCause());
			}
			if (!rp.isLastPart()) {
				currentFuture = readNextRowPacket();
			} else {
				currentFuture = null;
			}
		} else {
			rp = null;
		}
		return rp;
	}

	private Future<RowPacket> readNextRowPacket() throws SQLException {
		if (resultSet != null) {
			final Callable<RowPacket> callable = new Callable<RowPacket>() {
				@Override
				public RowPacket call() throws Exception {
					try {
						lock.lock();
						if (resultSet != null) {
							final RowPacket rp = new RowPacket(rowPacketSize, false);
							rp.populate(resultSet);
							return rp;
						}
						return null;
					} finally {
						lock.unlock();
					}
				}
			};
			return executor.execute(callable);
		}
		return null;
	}
}
