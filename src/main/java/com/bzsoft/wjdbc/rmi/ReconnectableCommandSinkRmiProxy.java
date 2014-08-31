package com.bzsoft.wjdbc.rmi;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.RMISocketFactory;
import java.sql.SQLException;
import java.util.Properties;

import com.bzsoft.wjdbc.WJdbcSqlException;
import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.CommandSink;
import com.bzsoft.wjdbc.command.ConnectResult;
import com.bzsoft.wjdbc.serial.CallingContext;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;

public class ReconnectableCommandSinkRmiProxy implements CommandSink {

	private final String					url;
	private final RMISocketFactory	sf;

	private CommandSinkRmi				rmiSink;

	public ReconnectableCommandSinkRmiProxy(final String url, final RMISocketFactory sf) throws Exception {
		this.url = url;
		this.sf = sf;
		final ConnectionBrokerRmi broker = (ConnectionBrokerRmi) Naming.lookup(url, sf);
		this.rmiSink = broker.createCommandSink();
	}

	@Override
	public ConnectResult connect(final String database, final Properties props, final Properties clientInfo, final CallingContext ctx)
			throws SQLException {
		if (rmiSink != null) {
			try {
				return rmiSink.connect(database, props, clientInfo, ctx);
			} catch (final NoSuchObjectException e) {
				ConnectionBrokerRmi broker;
				try {
					broker = (ConnectionBrokerRmi) Naming.lookup(url, sf);
					rmiSink = broker.createCommandSink();
				} catch (final Exception e1) {
					throw SQLExceptionHelper.wrap(e);
				}
				throw new WJdbcSqlException("Reconnection");
			} catch (final RemoteException e) {
				throw SQLExceptionHelper.wrap(e);
			}
		}
		throw new SQLException("Connection is already closed");
	}

	@Override
	public <R, P> R process(final long connuid, final long uid, final Command<R, P> cmd, final CallingContext ctx) throws SQLException {
		if (rmiSink != null) {
			try {
				return rmiSink.process(connuid, uid, cmd, ctx);
			} catch (final NoSuchObjectException e) {
				ConnectionBrokerRmi broker;
				try {
					broker = (ConnectionBrokerRmi) Naming.lookup(url, sf);
					rmiSink = broker.createCommandSink();
				} catch (final Exception e1) {
					throw SQLExceptionHelper.wrap(e);
				}
				throw new WJdbcSqlException("Server reincarnation");
			} catch (final RemoteException e) {
				throw SQLExceptionHelper.wrap(e);
			}
		}
		throw new SQLException("Connection is already closed");
	}

	@Override
	public void close() {
		rmiSink = null;
	}

}
