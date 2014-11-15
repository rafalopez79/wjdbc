package com.bzsoft.wjdbc.test.junit.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.bzsoft.wjdbc.test.junit.VJdbcTest;

public class HSqlDbConnectionPoolTest extends VJdbcTest {
	public static Test suite() throws Exception {
		final TestSuite suite = new TestSuite();

		VJdbcTest.addAllTestMethods(suite, HSqlDbConnectionPoolTest.class);

		final TestSetup wrapper = new TestSetup(suite) {
			@Override
			protected void setUp() throws Exception {
				new HSqlDbConnectionPoolTest("").oneTimeSetup();
			}

			@Override
			protected void tearDown() throws Exception {
				new HSqlDbConnectionPoolTest("").oneTimeTearDown();
			}
		};

		return wrapper;
	}

	public HSqlDbConnectionPoolTest(final String s) {
		super(s);
	}

	@Override
	protected Connection createNativeDatabaseConnection() throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");
		return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/HSqlDb", "sa", "");
	}

	@Override
	protected String getVJdbcDatabaseShortcut() {
		return "HSqlDB";
	}

	public void testOpenSomeConnectionsFromConnectionPool() throws Exception {
		final Connection[] conn = new Connection[15];
		for (int i = 0; i < conn.length; i++) {
			conn[i] = createVJdbcConnection();
		}
		for (final Connection element : conn) {
			element.close();
		}
	}
}
