package com.bzsoft.wjdbc.test;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class TestServer implements Test {

	protected TestServer() throws RemoteException {

	}

	@Override
	public void test() throws RemoteException {
		System.out.println("Testme!");
	}

	public static void main(final String[] args) throws Exception {
		final TestServer t = new TestServer();
		final TestServer t1 = new TestServer();
		final Remote rem = UnicastRemoteObject.exportObject(t, 4000);
		final Remote rem1 = UnicastRemoteObject.exportObject(t1, 4000);
		final Registry reg = LocateRegistry.createRegistry(4001);
		reg.rebind("TEST", rem);
		reg.rebind("TEST1", rem1);
		System.out.println("Running ...");
	}
}
