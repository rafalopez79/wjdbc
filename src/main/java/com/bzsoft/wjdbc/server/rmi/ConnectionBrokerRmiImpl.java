package com.bzsoft.wjdbc.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;

import com.bzsoft.wjdbc.rmi.CommandSinkRmi;
import com.bzsoft.wjdbc.rmi.ConnectionBrokerRmi;
import com.bzsoft.wjdbc.server.command.CommandProcessor;

public class ConnectionBrokerRmiImpl extends UnicastRemoteObject implements ConnectionBrokerRmi {

	private static final long			serialVersionUID	= 3257290235934029618L;

	private final CommandProcessor	processor;
	private final RMISocketFactory	sf;
	private final int						port;

	public ConnectionBrokerRmiImpl(final CommandProcessor cp, final RMISocketFactory sf, final int remotingPort) throws RemoteException {
		super(remotingPort, sf, sf);
		port = remotingPort;
		processor = cp;
		this.sf = sf;
	}

	@Override
	public CommandSinkRmi createCommandSink() throws RemoteException {
		final CommandSinkRmiImpl cs = new CommandSinkRmiImpl(processor);
		final Remote rem;
		if (sf != null) {
			rem = UnicastRemoteObject.exportObject(cs, port, sf, sf);
		} else {
			rem = UnicastRemoteObject.exportObject(cs, port);
		}
		return (CommandSinkRmi) rem;
	}
}
