package de.simplicit.vjdbc.test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestClient {

	public static void main(final String[] args) throws Exception {
		final Registry reg = LocateRegistry.getRegistry(4001);
		final Test test = (Test) reg.lookup("TEST");
		test.test();
	}

}
