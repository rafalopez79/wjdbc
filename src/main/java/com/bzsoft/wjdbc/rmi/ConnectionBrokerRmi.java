// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectionBrokerRmi extends Remote {

	CommandSinkRmi createCommandSink() throws RemoteException;
}
