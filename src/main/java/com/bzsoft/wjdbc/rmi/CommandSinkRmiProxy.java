package com.bzsoft.wjdbc.rmi;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Properties;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.CommandSink;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;

public class CommandSinkRmiProxy implements CommandSink {

	private CommandSinkRmi	targetSink;

	public CommandSinkRmiProxy(final CommandSinkRmi target) {
		targetSink = target;
	}

	@Override
	public long connect(final String url, final Properties props, final Properties clientInfo) throws SQLException {
		if (targetSink != null) {
			try {
				return targetSink.connect(url, props, clientInfo);
			} catch (final RemoteException e) {
				throw SQLExceptionHelper.wrap(e);
			}
		}
		throw new SQLException("Connection is already closed");
	}

	@Override
	public <R, P> R process(final long connuid, final long uid, final Command<R, P> cmd) throws SQLException {
		if (targetSink != null) {
			try {
				return targetSink.process(connuid, uid, cmd);
			} catch (final RemoteException e) {
				throw SQLExceptionHelper.wrap(e);
			}
		}
		throw new SQLException("Connection is already closed");
	}

	@Override
	public void close() {
		targetSink = null;
	}
}
