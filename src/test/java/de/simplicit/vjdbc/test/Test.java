package de.simplicit.vjdbc.test;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Test extends Remote {

	void test() throws RemoteException;
}
