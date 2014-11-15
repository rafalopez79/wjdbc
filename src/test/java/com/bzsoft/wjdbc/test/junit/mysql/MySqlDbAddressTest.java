package com.bzsoft.wjdbc.test.junit.mysql;

import java.sql.Connection;
import java.sql.DriverManager;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.bzsoft.wjdbc.test.junit.VJdbcTest;
import com.bzsoft.wjdbc.test.junit.general.AddressTest;

public class MySqlDbAddressTest extends AddressTest {
	public static Test suite() throws Exception {
		final TestSuite suite = new TestSuite();

		Class.forName("com.mysql.jdbc.Driver");
		VJdbcTest.addAllTestMethods(suite, MySqlDbAddressTest.class);

		final TestSetup wrapper = new TestSetup(suite) {
			@Override
			protected void setUp() throws Exception {
				new MySqlDbAddressTest("").oneTimeSetup();
			}

			@Override
			protected void tearDown() throws Exception {
				new MySqlDbAddressTest("").oneTimeTearDown();
			}
		};

		return wrapper;
	}

	public MySqlDbAddressTest(final String s) {
		super(s);
	}

	@Override
	protected Connection createNativeDatabaseConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql:///test", "root", "");
	}

	@Override
	protected String getVJdbcDatabaseShortcut() {
		return "MySqlDB";
	}
}
