package com.bzsoft.wjdbc.test.junit.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.bzsoft.wjdbc.test.junit.VJdbcTest;

public class Oracle9iTest extends VJdbcTest {
	public Oracle9iTest(final String s) {
		super(s);
	}

	@Override
	protected Connection createNativeDatabaseConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", "scott", "tiger");
	}

	@Override
	protected String getVJdbcDatabaseShortcut() {
		return "OracleDB";
	}

	protected String getVJdbcPassword() {
		return "vjdbc";
	}

	protected String getVJdbcUser() {
		return "vjdbc";
	}

	@Override
	protected void oneTimeSetup() throws Exception {
		super.oneTimeSetup();

		Class.forName("oracle.jdbc.OracleDriver");
	}

	public static Test suite() throws Exception {
		final TestSuite suite = new TestSuite();

		suite.addTest(new Oracle9iTest("testConnection"));
		suite.addTest(new Oracle9iTest("testDatabaseMetaData"));
		suite.addTest(OracleAddressTest.suite());
		suite.addTest(NestedTableTest.suite());
		suite.addTest(ObjectTest.suite());
		suite.addTest(BlobClobTest.suite());

		final TestSetup wrapper = new TestSetup(suite) {
			@Override
			protected void setUp() throws Exception {
				new Oracle9iTest("").oneTimeSetup();
			}

			@Override
			protected void tearDown() throws Exception {
				new Oracle9iTest("").oneTimeTearDown();
			}
		};

		return wrapper;
	}

	protected void dropTypes(final Statement stmt, final String[] types) {
		for (final String type : types) {
			System.out.println("... drop type " + type);
			try {
				stmt.executeUpdate("drop type " + type);
			} catch (final SQLException e) {
				System.out.println("... doesn't exist");
			}
		}
	}
}
