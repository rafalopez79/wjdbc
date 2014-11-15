package com.bzsoft.wjdbc.test.junit.oracle;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

public class BlobClobTest extends Oracle9iTest {
	public static Test suite() {
		final TestSuite suite = new TestSuite();

		suite.addTest(new BlobClobTest("testInsertBlobs"));
		suite.addTest(new BlobClobTest("testInsertClobs"));
		suite.addTest(new BlobClobTest("testInsertNClobs"));
		// suite.addTest(new BlobClobTest("testInsertSQLXMLs"));
		suite.addTest(new BlobClobTest("testInsertNStrings"));

		final TestSetup wrapper = new TestSetup(suite) {
			@Override
			protected void setUp() throws Exception {
				new BlobClobTest("").oneTimeSetup();
			}

			@Override
			protected void tearDown() throws Exception {
				new BlobClobTest("").oneTimeTearDown();
			}
		};

		return wrapper;
	}

	public BlobClobTest(final String s) {
		super(s);
	}

	@Override
	protected void oneTimeSetup() throws Exception {
		super.oneTimeSetup();

		final Connection connVJdbc = createVJdbcConnection();

		System.out.println("Creating tables ...");
		final Statement stmt = connVJdbc.createStatement();
		dropTables(stmt, new String[] { "blobs", "clobs", "nclobs", "sqlxmls", "nstrings" });
		stmt.executeUpdate("create table blobs (id int, someblob blob)");
		stmt.executeUpdate("create table clobs (id int, someclob clob)");
		stmt.executeUpdate("create table nclobs (id int, somenclob nclob)");
		stmt.executeUpdate("create table sqlxmls (id int, somexml xmltype)");
		stmt.executeUpdate("create table nstrings (id int, somestr varchar2(24), somenstr nvarchar2(24))");
		stmt.close();
		connVJdbc.close();
	}

	public void testInsertBlobs() throws Exception {
		final byte[] blobdata = new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77 };
		final PreparedStatement pstmt = _connVJdbc.prepareStatement("insert into blobs values(?, ?)");
		for (int i = 1; i <= 10; i++) {
			pstmt.setInt(1, i);
			pstmt.setBinaryStream(2, new ByteArrayInputStream(blobdata), blobdata.length);
			pstmt.addBatch();
		}
		assertEquals(pstmt.executeBatch().length, 10);
	}

	public void testInsertClobs() throws Exception {
		final StringBuffer sb = new StringBuffer();
		/*
		 * The upper bound of the loop was originally 1000 which resulted in a
		 * clob of about 8000 bytes. The Thin-Driver doesn't seem to cope with
		 * that, it's just possible to write up to 4k (see
		 * http://helma.org/pipermail/helma-user/2004-August/002723.html)
		 */
		for (int i = 0; i < 100; i++) {
			sb.append("CLOBDATA");
		}
		final String clobdata = sb.toString();
		final PreparedStatement pstmt = _connVJdbc.prepareStatement("insert into clobs values(?, ?)");
		for (int i = 1; i <= 10; i++) {
			pstmt.setInt(1, i);
			pstmt.setCharacterStream(2, new StringReader(clobdata), clobdata.length());
			pstmt.executeUpdate();
		}
		pstmt.close();

		final Statement stmtOrig = _connOther.createStatement();
		final Statement stmtVJdbc = _connVJdbc.createStatement();
		final ResultSet rsOrig = stmtOrig.executeQuery("select * from clobs");
		final ResultSet rsVJdbc = stmtVJdbc.executeQuery("select * from clobs");
		compareResultSets(rsOrig, rsVJdbc);
		stmtOrig.close();
		stmtVJdbc.close();
	}

	public void testInsertNClobs() throws Exception {
		final StringBuffer sb = new StringBuffer();
		/*
		 * The upper bound of the loop was originally 1000 which resulted in a
		 * clob of about 8000 bytes. The Thin-Driver doesn't seem to cope with
		 * that, it's just possible to write up to 4k (see
		 * http://helma.org/pipermail/helma-user/2004-August/002723.html)
		 */
		for (int i = 0; i < 100; i++) {
			sb.append("CLOBDATA");
		}
		final String nclobdata = sb.toString();
		final PreparedStatement pstmt = _connVJdbc.prepareStatement("insert into nclobs values(?, ?)");
		for (int i = 1; i <= 10; i++) {
			pstmt.setInt(1, i);
			pstmt.setCharacterStream(2, new StringReader(nclobdata), nclobdata.length());
			pstmt.executeUpdate();
		}
		pstmt.close();

		final Statement stmtOrig = _connOther.createStatement();
		final Statement stmtVJdbc = _connVJdbc.createStatement();
		final ResultSet rsOrig = stmtOrig.executeQuery("select * from nclobs");
		final ResultSet rsVJdbc = stmtVJdbc.executeQuery("select * from nclobs");
		compareResultSets(rsOrig, rsVJdbc);
		stmtOrig.close();
		stmtVJdbc.close();
	}

	public void testInsertSQLXMLs() throws Exception {
		final StringBuffer sb = new StringBuffer();
		/*
		 * The upper bound of the loop was originally 1000 which resulted in a
		 * clob of about 8000 bytes. The Thin-Driver doesn't seem to cope with
		 * that, it's just possible to write up to 4k (see
		 * http://helma.org/pipermail/helma-user/2004-August/002723.html)
		 */
		sb.append("<TEST>");
		for (int i = 0; i < 100; i++) {
			sb.append("<LOBDATA />");
		}
		sb.append("</TEST>");
		final String nclobdata = sb.toString();
		final PreparedStatement pstmt = _connVJdbc.prepareStatement("insert into sqlxmls values(?, ?)");
		for (int i = 1; i <= 10; i++) {
			pstmt.setInt(1, i);
			pstmt.setCharacterStream(2, new StringReader(nclobdata), nclobdata.length());
			pstmt.executeUpdate();
		}
		pstmt.close();

		final Statement stmtOrig = _connOther.createStatement();
		final Statement stmtVJdbc = _connVJdbc.createStatement();
		final ResultSet rsOrig = stmtOrig.executeQuery("select * from sqlxmls");
		final ResultSet rsVJdbc = stmtVJdbc.executeQuery("select * from sqlxmls");
		compareResultSets(rsOrig, rsVJdbc);
		stmtOrig.close();
		stmtVJdbc.close();
	}

	public void testInsertNStrings() throws Exception {
		final PreparedStatement pstmt = _connVJdbc.prepareStatement("insert into nstrings values(?, ?, ?)");
		for (int i = 1; i <= 10; i++) {
			pstmt.setInt(1, i);
			pstmt.setNString(2, "STRDATA");
			pstmt.setNString(3, "NSTRDATA");
			pstmt.executeUpdate();
		}
		pstmt.close();

		final Statement stmtOrig = _connOther.createStatement();
		final Statement stmtVJdbc = _connVJdbc.createStatement();
		final ResultSet rsOrig = stmtOrig.executeQuery("select * from nstrings");
		final ResultSet rsVJdbc = stmtVJdbc.executeQuery("select * from nstrings");
		compareResultSets(rsOrig, rsVJdbc);
		stmtOrig.close();
		stmtVJdbc.close();
	}

}
