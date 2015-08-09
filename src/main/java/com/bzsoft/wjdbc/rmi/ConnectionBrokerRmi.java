package com.bzsoft.wjdbc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectionBrokerRmi extends Remote {

	public CommandSinkRmi createCommandSink() throws RemoteException;

	public void shutdown()throws RemoteException;
}
