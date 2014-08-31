// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.bzsoft.wjdbc.WJdbcSqlException;
import com.bzsoft.wjdbc.rmi.KeepAliveTimerTask;
import com.bzsoft.wjdbc.serial.CallingContext;
import com.bzsoft.wjdbc.server.concurrent.Executor;

/**
 * The DecoratedCommandSink makes it easier to handle the CommandSink. It
 * contains a number of different utility methods which wrap parameters, unwrap
 * results and so on. Additionally it supports a Listener which is called before
 * and after execution of the command.
 */
public class DecoratedCommandSink {

	private final long						connectionUid;
	private final CommandSink				targetSink;
	private final CallingContextFactory	callingContextFactory;

	private CommandSinkListener			listener;
	private Future<?>							future;

	private volatile boolean				valid;

	public DecoratedCommandSink(final long connuid, final CommandSink sink, final CallingContextFactory ctxFactory, final Executor executor) {
		this(connuid, sink, ctxFactory, 60000l, executor);
		valid = true;
	}

	public DecoratedCommandSink(final long connuid, final CommandSink sink, final CallingContextFactory ctxFactory, final long pingPeriod,
			final Executor executor) {
		connectionUid = connuid;
		targetSink = sink;
		callingContextFactory = ctxFactory;
		listener = new NullCommandSinkListener();
		valid = true;
		if (pingPeriod > 0) {
			final KeepAliveTimerTask task = new KeepAliveTimerTask(this, connuid);
			future = executor.schedule(task, pingPeriod, pingPeriod, TimeUnit.MILLISECONDS);
		} else {
			future = null;
		}
	}

	public ConnectResult connect(final String url, final Properties props, final Properties clientInfo, final CallingContext ctx) throws SQLException {
		try {
			return targetSink.connect(url, props, clientInfo, ctx);
		} catch (final WJdbcSqlException e) {
			valid = false;
			close();
			throw e;
		}
	}

	public <R, P> R process(final long uid, final Command<R, P> cmd, final boolean withCallingContext) throws SQLException {
		validateReincarnation();
		try {
			CallingContext ctx = null;
			if (withCallingContext) {
				ctx = callingContextFactory.create();
			}
			listener.preExecution(cmd);
			try {
				return targetSink.process(connectionUid, uid, cmd, ctx);
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

	public <R, P> R process(final long uid, final Command<R, P> cmd) throws SQLException {
		return process(uid, cmd, false);
	}

	public <P> int processWithIntResult(final long uid, final Command<Integer, P> cmd) throws SQLException {
		return processWithIntResult(uid, cmd, false);
	}

	public <P> int processWithIntResult(final long uid, final Command<Integer, P> cmd, final boolean withCallingContext) throws SQLException {
		try {
			CallingContext ctx = null;
			if (withCallingContext) {
				ctx = callingContextFactory.create();
			}
			listener.preExecution(cmd);
			final Integer n = targetSink.process(connectionUid, uid, cmd, ctx);
			return n.intValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> boolean processWithBooleanResult(final long uid, final Command<Boolean, P> cmd) throws SQLException {
		return processWithBooleanResult(uid, cmd, false);
	}

	public <P> boolean processWithBooleanResult(final long uid, final Command<Boolean, P> cmd, final boolean withCallingContext) throws SQLException {
		try {
			CallingContext ctx = null;
			if (withCallingContext) {
				ctx = callingContextFactory.create();
			}
			listener.preExecution(cmd);
			final Boolean b = targetSink.process(connectionUid, uid, cmd, ctx);
			return b.booleanValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> byte processWithByteResult(final long uid, final Command<Byte, P> cmd) throws SQLException {
		return processWithByteResult(uid, cmd, false);
	}

	public <P> byte processWithByteResult(final long uid, final Command<Byte, P> cmd, final boolean withCallingContext) throws SQLException {
		try {
			CallingContext ctx = null;
			if (withCallingContext) {
				ctx = callingContextFactory.create();
			}
			listener.preExecution(cmd);
			final Byte b = targetSink.process(connectionUid, uid, cmd, ctx);
			return b.byteValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> short processWithShortResult(final long uid, final Command<Short, P> cmd) throws SQLException {
		return processWithShortResult(uid, cmd, false);
	}

	public <P> short processWithShortResult(final long uid, final Command<Short, P> cmd, final boolean withCallingContext) throws SQLException {
		try {
			CallingContext ctx = null;
			if (withCallingContext) {
				ctx = callingContextFactory.create();
			}
			listener.preExecution(cmd);
			final Short b = targetSink.process(connectionUid, uid, cmd, ctx);
			return b.shortValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> long processWithLongResult(final long uid, final Command<Long, P> cmd) throws SQLException {
		return processWithLongResult(uid, cmd, false);
	}

	public <P> long processWithLongResult(final long uid, final Command<Long, P> cmd, final boolean withCallingContext) throws SQLException {
		try {
			CallingContext ctx = null;
			if (withCallingContext) {
				ctx = callingContextFactory.create();
			}
			listener.preExecution(cmd);
			final Long b = targetSink.process(connectionUid, uid, cmd, ctx);
			return b.longValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> float processWithFloatResult(final long uid, final Command<Float, P> cmd) throws SQLException {
		return processWithFloatResult(uid, cmd, false);
	}

	public <P> float processWithFloatResult(final long uid, final Command<Float, P> cmd, final boolean withCallingContext) throws SQLException {
		try {
			CallingContext ctx = null;
			if (withCallingContext) {
				ctx = callingContextFactory.create();
			}
			listener.preExecution(cmd);
			final Float b = targetSink.process(connectionUid, uid, cmd, ctx);
			return b.floatValue();
		} finally {
			listener.postExecution(cmd);
		}
	}

	public <P> double processWithDoubleResult(final long uid, final Command<Double, P> cmd) throws SQLException {
		return processWithDoubleResult(uid, cmd, false);
	}

	public <P> double processWithDoubleResult(final long uid, final Command<Double, P> cmd, final boolean withCallingContext) throws SQLException {
		try {
			CallingContext ctx = null;
			if (withCallingContext) {
				ctx = callingContextFactory.create();
			}
			listener.preExecution(cmd);
			final Double b = targetSink.process(connectionUid, uid, cmd, ctx);
			return b.doubleValue();
		} finally {
			listener.postExecution(cmd);
		}
	}
}
