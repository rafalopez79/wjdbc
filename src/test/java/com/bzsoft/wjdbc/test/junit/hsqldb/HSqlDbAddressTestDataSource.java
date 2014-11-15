package com.bzsoft.wjdbc.test.junit.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.bzsoft.wjdbc.test.junit.VJdbcTest;
import com.bzsoft.wjdbc.test.junit.general.AddressTest;

public class HSqlDbAddressTestDataSource extends AddressTest {
	public static Test suite() throws Exception {
		final TestSuite suite = new TestSuite();

		VJdbcTest.addAllTestMethods(suite, HSqlDbAddressTestDataSource.class);

		final TestSetup wrapper = new TestSetup(suite) {
			@Override
			protected void setUp() throws Exception {
				new HSqlDbAddressTestDataSource("").oneTimeSetup();
			}

			@Override
			protected void tearDown() throws Exception {
				new HSqlDbAddressTestDataSource("").oneTimeTearDown();
			}
		};

		return wrapper;
	}

	public HSqlDbAddressTestDataSource(final String s) {
		super(s);
	}

	@Override
	protected Connection createNativeDatabaseConnection() throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");
		return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/HSqlDb", "sa", "");
	}

	@Override
	protected String getVJdbcDatabaseShortcut() {
		return "HSqlDB-DataSource";
	}
}
