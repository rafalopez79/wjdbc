package com.bzsoft.wjdbc.test.junit.oracle;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.bzsoft.wjdbc.test.junit.VJdbcTest;
import com.bzsoft.wjdbc.test.junit.general.AddressTest;

public class OracleAddressTest extends AddressTest {

	public static Test suite() throws Exception {
		final TestSuite suite = new TestSuite();

		VJdbcTest.addAllTestMethods(suite, OracleAddressTest.class);

		final TestSetup wrapper = new TestSetup(suite) {
			@Override
			protected void setUp() throws Exception {
				new OracleAddressTest("").oneTimeSetup();
			}

			@Override
			protected void tearDown() throws Exception {
				new OracleAddressTest("").oneTimeTearDown();
			}
		};

		return wrapper;
	}

	public OracleAddressTest(final String s) {
		super(s);
	}

	@Override
	protected Connection createNativeDatabaseConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", "scott", "tiger");
	}

	protected String getVJdbcPassword() {
		return "vjdbc";
	}

	protected String getVJdbcUser() {
		return "vjdbc";
	}

	@Override
	protected String getVJdbcDatabaseShortcut() {
		return "OracleDB";
	}

	@Override
	public String getCreateBlobsTableSql() {
		return "create table SomeBlobs (id int, description raw(100))";
	}

	@Override
	public void testExceptionStacktrace() throws Exception {
		final Statement stmtVJdbc = _connVJdbc.createStatement();
		try {
			stmtVJdbc.executeQuery("select * from nonexistingtable");
		} catch (final SQLException e) {
			final String msg = e.getMessage().toLowerCase();
			System.out.println("err msg " + msg);
			assertTrue(msg.indexOf("table not found") >= 0 || msg.indexOf("doesn't exist") >= 0 || msg.indexOf("does not exist") >= 0);
		}
		stmtVJdbc.close();
	}

	@Override
	public void testCancelStatement() throws Exception {
		final Statement stmt = _connVJdbc.createStatement();
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
					stmt.cancel();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		final ResultSet rs = stmt.executeQuery("select * from Address a, Address b where a.id = b.id");
		rs.close();
		stmt.close();
		t.join();
	}

	@Override
	public void testConversions() throws Exception {
		final Statement stmtVJdbc = _connVJdbc.createStatement();
		final Statement stmtNative = _connOther.createStatement();

		final ResultSet rsVJdbc = stmtVJdbc.executeQuery("select id, somenumber, stringboolean, integernumber, floatingnumber from Address");
		final ResultSet rsNative = stmtNative.executeQuery("select id, somenumber, stringboolean, integernumber, floatingnumber from Address");

		assertTrue(rsNative.next());
		assertTrue(rsVJdbc.next());

		final int nId = rsNative.getInt("id");
		final int vId = rsVJdbc.getInt("id");
		boolean nIsZero = rsNative.getBoolean("somenumber");
		boolean vIsZero = rsVJdbc.getBoolean("somenumber");
		assertEquals(nIsZero, vIsZero);

		nIsZero = rsNative.getBoolean("stringboolean");
		vIsZero = rsVJdbc.getBoolean("stringboolean");
		assertEquals(nId, vId);
		assertEquals(nIsZero, vIsZero);
		// Check String-to-Integer conversion
		assertEquals(rsNative.getShort("id"), rsNative.getShort("integernumber"));
		assertEquals(rsVJdbc.getShort("id"), rsVJdbc.getShort("integernumber"));
		assertEquals(rsNative.getInt("id"), rsNative.getInt("integernumber"));
		assertEquals(rsVJdbc.getInt("id"), rsVJdbc.getInt("integernumber"));
		assertEquals(rsNative.getLong("id"), rsNative.getLong("integernumber"));
		assertEquals(rsVJdbc.getLong("id"), rsVJdbc.getLong("integernumber"));
		// Check String-to-Float conversion
		assertEquals(rsNative.getBigDecimal("id").add(new BigDecimal("0.5")), rsNative.getBigDecimal("floatingnumber"));
		assertEquals(rsVJdbc.getBigDecimal("id").add(new BigDecimal("0.5")), rsVJdbc.getBigDecimal("floatingnumber"));
		rsNative.close();
		rsVJdbc.close();
		stmtVJdbc.close();
		stmtNative.close();
	}
}
