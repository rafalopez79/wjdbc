package com.bzsoft.wjdbc.test.junit.db2;

import java.sql.Connection;
import java.sql.DriverManager;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.bzsoft.wjdbc.test.junit.VJdbcTest;
import com.bzsoft.wjdbc.test.junit.general.AddressTest;

public class Db2AddressTest extends AddressTest {
	public static Test suite() throws Exception {
		final TestSuite suite = new TestSuite();

		VJdbcTest.addAllTestMethods(suite, Db2AddressTest.class);

		final TestSetup wrapper = new TestSetup(suite) {
			@Override
			protected void setUp() throws Exception {
				new Db2AddressTest("").oneTimeSetup();
			}

			@Override
			protected void tearDown() throws Exception {
				new Db2AddressTest("").oneTimeTearDown();
			}
		};

		return wrapper;
	}

	public Db2AddressTest(final String s) {
		super(s);
	}

	@Override
	protected Connection createNativeDatabaseConnection() throws Exception {
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		return DriverManager.getConnection("jdbc:db2://mikepc:50000/VJDBC", "db2admin", "db2admin");
	}

	@Override
	protected String getVJdbcDatabaseShortcut() {
		return "DB2DB";
	}
}
