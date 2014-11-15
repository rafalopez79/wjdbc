package com.bzsoft.wjdbc.test.junit.jdbcodbc;

import java.sql.Connection;
import java.sql.DriverManager;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.bzsoft.wjdbc.test.junit.VJdbcTest;
import com.bzsoft.wjdbc.test.junit.general.AddressTest;

public class JdbcOdbcAddressTest extends AddressTest {
	public static Test suite() throws Exception {
		final TestSuite suite = new TestSuite();

		VJdbcTest.addAllTestMethods(suite, JdbcOdbcAddressTest.class);

		final TestSetup wrapper = new TestSetup(suite) {
			@Override
			protected void setUp() throws Exception {
				new JdbcOdbcAddressTest("").oneTimeSetup();
			}

			@Override
			protected void tearDown() throws Exception {
				new JdbcOdbcAddressTest("").oneTimeTearDown();
			}
		};

		return wrapper;
	}

	public JdbcOdbcAddressTest(final String s) {
		super(s);
	}

	@Override
	protected Connection createNativeDatabaseConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:testdb");
	}

	@Override
	protected String getVJdbcDatabaseShortcut() {
		return "OdbcDB";
	}
}
