package com.bzsoft.wjdbc.test.junit.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

public class NestedTableTest extends Oracle9iTest {
	public NestedTableTest(final String s) {
		super(s);
	}

	@Override
	protected void oneTimeSetup() throws Exception {
		super.oneTimeSetup();

		final Connection connVJdbc = createVJdbcConnection();

		System.out.println("Creating tables ...");
		final Statement stmt = connVJdbc.createStatement();
		dropTables(stmt, new String[] { "product", "supplier_tbl" });
		System.out.println("... create type supplier");
		stmt.executeUpdate("create or replace type supplier as table of char(3)");
		System.out.println("... create table product");
		stmt.executeUpdate("create table product (prodno char(8) primary key, price number(5,2), supplierno supplier) nested table supplierno store as supplier_tbl");
		System.out.println("... insert product data");
		final PreparedStatement pstmt = connVJdbc.prepareStatement("insert into product values (?, 23.45, supplier('101', '224'))");
		for (int i = 0; i < 200; i++) {
			pstmt.setString(1, "XY" + i);
			pstmt.addBatch();
		}
		pstmt.executeBatch();
		pstmt.close();
		connVJdbc.close();
	}

	public static Test suite() {
		final TestSuite suite = new TestSuite();

		suite.addTest(new NestedTableTest("testConnection"));

		final TestSetup wrapper = new TestSetup(suite) {
			@Override
			protected void setUp() throws Exception {
				new NestedTableTest("").oneTimeSetup();
			}

			@Override
			protected void tearDown() throws Exception {
				new NestedTableTest("").oneTimeTearDown();
			}
		};

		return wrapper;
	}
}
