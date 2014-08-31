// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.rmi;

import java.sql.Connection;
import java.sql.SQLException;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.CommandSinkListener;
import com.bzsoft.wjdbc.command.DecoratedCommandSink;
import com.bzsoft.wjdbc.command.PingCommand;

/**
 * This timer task will periodically notify the server with a dummy command,
 * just to keep the connection alive. This will prevent the RMI-Object to be
 * garbage-collected when there aren't any RMI-Calls for a specific time (lease
 * value).
 */
public final class KeepAliveTimerTask implements CommandSinkListener, Runnable {

	private static final Command<Void, Connection>	DUMMYCOMMAND	= new PingCommand();

	private volatile boolean								ignoreNextPing;
	private final DecoratedCommandSink					sink;
	private final long										connuid;

	public KeepAliveTimerTask(final DecoratedCommandSink sink, final long connuid) {
		this.sink = sink;
		this.connuid = connuid;
		ignoreNextPing = false;
		sink.setListener(this);
	}

	@Override
	public <R, P> void preExecution(final Command<R, P> cmd) {
		ignoreNextPing = true;
	}

	@Override
	public <R, P> void postExecution(final Command<R, P> cmd) {
		ignoreNextPing = false;
	}

	@Override
	public void run() {
		if (ignoreNextPing) {
			ignoreNextPing = false;
		} else {
			try {
				sink.process(connuid, DUMMYCOMMAND);
			} catch (final SQLException e) {
				// Ignore it, sink is already closed
			}
		}
	}
}
