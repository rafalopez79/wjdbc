package com.bzsoft.wjdbc.server.rmi;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;

import com.bzsoft.wjdbc.rmi.CommandSinkRmi;
import com.bzsoft.wjdbc.rmi.ConnectionBrokerRmi;
import com.bzsoft.wjdbc.server.command.CommandProcessor;

public class ConnectionBrokerRmiImpl extends UnicastRemoteObject implements ConnectionBrokerRmi {

	private static final long		serialVersionUID	= 3257290235934029618L;

	private final CommandSinkRmi	remote;

	public ConnectionBrokerRmiImpl(final CommandProcessor cp, final RMISocketFactory sf, final int remotingPort) throws RemoteException {
		super(remotingPort, sf, sf);
		remote = createCommandSink(cp, sf, remotingPort);
	}

	@Override
	public CommandSinkRmi getCommandSink() throws RemoteException {
		return remote;
	}

	@Override
	public void shutdown() {
		try {
			UnicastRemoteObject.unexportObject(remote, true);
		} catch (final NoSuchObjectException e) {
			// empty
		}
	}

	private static final CommandSinkRmi createCommandSink(final CommandProcessor cp, final RMISocketFactory sf, final int port) throws RemoteException {
		final CommandSinkRmiImpl cs = new CommandSinkRmiImpl(cp);
		final Remote rem;
		if (sf != null) {
			rem = UnicastRemoteObject.exportObject(cs, port, sf, sf);
		} else {
			rem = UnicastRemoteObject.exportObject(cs, port);
		}
		return (CommandSinkRmi) rem;
	}
}
