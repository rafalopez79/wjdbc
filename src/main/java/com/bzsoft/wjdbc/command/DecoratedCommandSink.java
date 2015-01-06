package com.bzsoft.wjdbc.command;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.bzsoft.wjdbc.WJdbcSqlException;
import com.bzsoft.wjdbc.rmi.KeepAliveTimerTask;
import com.bzsoft.wjdbc.server.concurrent.Executor;

/**
 * The DecoratedCommandSink makes it easier to handle the CommandSink. It
 * contains a number of different utility methods which wrap parameters, unwrap
 * results and so on. Additionally it supports a Listener which is called before
 * and after execution of the command.
 */
public class DecoratedCommandSink {

	private final long				connectionUid;
	private final CommandSink		targetSink;

	private CommandSinkListener	listener;
	private Future<?>					future;

	private volatile boolean		valid;

	public DecoratedCommandSink(final long connuid, final CommandSink sink, final Executor executor) {
		this(connuid, sink, 60000l, executor);
		valid = true;
	}

	public DecoratedCommandSink(final long connuid, final CommandSink sink, final long pingPeriod, final Executor executor) {
		connectionUid = connuid;
		targetSink = sink;
		listener = new NullCommandSinkListener();
		valid = true;
		if (pingPeriod > 0) {
			final KeepAliveTimerTask task = new KeepAliveTimerTask(this, connuid);
			future = executor.schedule(task, pingPeriod, pingPeriod, TimeUnit.MILLISECONDS);
		} else {
			future = null;
		}
	}

	public long connect(final String url, final Properties props, final Properties clientInfo) throws SQLException {
		try {
			return targetSink.connect(url, props, clientInfo);
		} catch (final WJdbcSqlException e) {
			valid = false;
			close();
			throw e;
		}
	}

	public <R, P> R process(final long uid, final Command<R, P> cmd) throws SQLException {
		validateReincarnation();
		try {
			listener.preExecution(cmd);
			try {
				return targetSink.process(connectionUid, uid, cmd);
			} catch (final WJdbcSqlException e) {
				valid = false;
				close();
				throw e;
			}
		} finally {
			listener.postExecution(cmd);
		}
	}

	public CommandSink getTargetSink() {
		return targetSink;
	}

	public boolean isValid() {
		return valid;
	}

	public void close() {
		if (future != null) {
			future.cancel(true);
		}
		targetSink.close();
	}

	public void setListener(final CommandSinkListener listener) {
		if (listener != null) {
			this.listener = listener;
		} else {
			this.listener = new NullCommandSinkListener();
		}
	}

	protected void validateReincarnation() throws SQLException {
		if (!valid) {
			throw new SQLException("Object invalid for server reincarnation");
		}
	}

	public <P> int processWithIntResult(final long uid, final Command<Integer, P> cmd) throws SQLException {
		try {
			listener.preExecution(cmd);
			final Integer n = targetSink.process(connectionUid, uid, cmd);
			return n.intValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> boolean processWithBooleanResult(final long uid, final Command<Boolean, P> cmd) throws SQLException {
		try {
			listener.preExecution(cmd);
			final Boolean b = targetSink.process(connectionUid, uid, cmd);
			return b.booleanValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> byte processWithByteResult(final long uid, final Command<Byte, P> cmd) throws SQLException {
		try {
			listener.preExecution(cmd);
			final Byte b = targetSink.process(connectionUid, uid, cmd);
			return b.byteValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> short processWithShortResult(final long uid, final Command<Short, P> cmd) throws SQLException {
		try {
			listener.preExecution(cmd);
			final Short b = targetSink.process(connectionUid, uid, cmd);
			return b.shortValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> long processWithLongResult(final long uid, final Command<Long, P> cmd) throws SQLException {
		try {
			listener.preExecution(cmd);
			final Long b = targetSink.process(connectionUid, uid, cmd);
			return b.longValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> float processWithFloatResult(final long uid, final Command<Float, P> cmd) throws SQLException {
		try {
			listener.preExecution(cmd);
			final Float b = targetSink.process(connectionUid, uid, cmd);
			return b.floatValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> double processWithDoubleResult(final long uid, final Command<Double, P> cmd) throws SQLException {
		try {
			listener.preExecution(cmd);
			final Double b = targetSink.process(connectionUid, uid, cmd);
			return b.doubleValue();
		} finally {
			listener.postExecution(cmd);
		}
	}
}
