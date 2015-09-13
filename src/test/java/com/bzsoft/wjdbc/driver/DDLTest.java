package com.bzsoft.wjdbc.driver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.bzsoft.wjdbc.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DDLTest extends BaseTest{

	protected Connection conn;

	@Override
	@Before
	public void setUp() throws Exception{
		super.setUp();
		conn = setUpClient();
	}

	@Override
	@After
	public void tearDown() throws Exception{
		super.tearDown();
		if (conn != null){
			conn.close();
		}
	}


	@Test
	public void testInsertPreparedStatement1() throws Exception{
		final Statement stmt = conn.createStatement();
		executeUpdate(stmt,
				"DROP SCHEMA IF EXISTS TEST",
				"CREATE SCHEMA TEST",
				"CREATE TABLE TEST.TEST1 ( A INT, B VARCHAR(16), C DATE, D NUMBER(3,3), E FLOAT, F REAL,  PRIMARY KEY (A))");
		final PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TEST.TEST1 VALUES (?,?,?,?,?,?)");
		@SuppressWarnings("deprecation")
		final Date d = new Date(2015,11,9);
		final int count = 1000;
		for(int i = 0; i < count; i++){
			pstmt.setInt(1, i);
			pstmt.setString(2, Integer.toString(i));
			pstmt.setDate(3, new java.sql.Date(d.getTime()));
			pstmt.setBigDecimal(4, new BigDecimal( i / (double)count));
			pstmt.setFloat(5, i / (float)count);
			pstmt.setDouble(6, i / (double) count);
			final int c = pstmt.executeUpdate();
			Assert.assertTrue(c == 1);
		}
		pstmt.close();
		final PreparedStatement pstmt2 = conn.prepareStatement("SELECT A,B,C,D,E,F FROM TEST.TEST1 ORDER BY A");
		final ResultSet rs = pstmt2.executeQuery();
		int j = 0;
		while(rs.next()){
			Assert.assertTrue(rs.getInt(1) == j);
			Assert.assertTrue(rs.getString(2).equals(Integer.toString(j)));
			Assert.assertTrue(rs.getDate(3).equals(new java.sql.Date(d.getTime())));
			Assert.assertTrue(rs.getBigDecimal(4).equals(new BigDecimal( j / (double)count).setScale(3, RoundingMode.HALF_EVEN)));
			Assert.assertTrue(rs.getFloat(5) == j / (float) count);
			Assert.assertTrue(Math.abs(rs.getDouble(6) -j / (double)count) < EPS);
			j++;
		}
		rs.close();
		pstmt2.close();
		executeUpdate(stmt, "DELETE FROM TEST.TEST1");
		executeUpdate(stmt, "DROP TABLE TEST.TEST1");
		stmt.close();
	}

	@Test
	public void testLargeSelectWithBatch() throws Exception{
		final Statement stmt = conn.createStatement();
		executeUpdate(stmt,
				"DROP SCHEMA IF EXISTS TEST2",
				"CREATE SCHEMA TEST2",
				"CREATE TABLE TEST2.TEST1 ( A INT, B VARCHAR(16), C DATE, PRIMARY KEY (A))");
		final PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TEST2.TEST1 VALUES (?,?,?)");
		@SuppressWarnings("deprecation")
		final Date d = new Date(2015,11,9);
		final int count = 10000;
		int k = 0;
		for(int i = 0; i < count/100; i++){
			for(int j = 0 ; j< 100; j++){
				pstmt.setInt(1, k);
				pstmt.setString(2, Integer.toString(k));
				pstmt.setDate(3, new java.sql.Date(d.getTime()));
				pstmt.addBatch();
				k++;
			}
			final int[]batchResult = pstmt.executeBatch();
			for (final int element : batchResult) {
				Assert.assertEquals(element, 1);
			}
		}
		pstmt.close();
		final PreparedStatement pstmt2 = conn.prepareStatement("SELECT A,B,C FROM TEST2.TEST1 ORDER BY A");
		final ResultSet rs = pstmt2.executeQuery();
		int j = 0;
		while(rs.next()){
			Assert.assertTrue(rs.getInt(1) == j);
			Assert.assertTrue(rs.getString(2).equals(Integer.toString(j)));
			Assert.assertTrue(rs.getDate(3).equals(new java.sql.Date(d.getTime())));
			j++;
		}
		Assert.assertEquals(j, count);
		rs.close();
		final ResultSet rs2 = pstmt2.executeQuery();
		j = 0;
		while(rs2.next()){
			Assert.assertTrue(rs2.getInt(1) == j);
			Assert.assertTrue(rs2.getString(2).equals(Integer.toString(j)));
			Assert.assertTrue(rs2.getDate(3).equals(new java.sql.Date(d.getTime())));
			j++;
		}
		Assert.assertEquals(j, count);
		rs2.close();
		pstmt2.close();
		final PreparedStatement pstmt3 = conn.prepareStatement("DELETE FROM TEST2.TEST1 WHERE A = ?");
		for(int i = 0; i < count; i++){
			pstmt3.setBigDecimal(1, new BigDecimal(i));
			pstmt3.addBatch();
		}
		final int[] result = pstmt3.executeBatch();
		for(int i = 0; i < count; i++){
			Assert.assertTrue(result[i] == 1);
		}
		pstmt3.close();
		Assert.assertTrue(conn.isValid(1000));
		executeUpdate(stmt, "DELETE FROM TEST2.TEST1");
		executeUpdate(stmt, "DROP TABLE TEST2.TEST1");
		stmt.close();
	}

	@Test
	public void testDelete() throws Exception{
		final Statement stmt = conn.createStatement();
		executeUpdate(stmt,
				"DROP SCHEMA IF EXISTS TEST3",
				"CREATE SCHEMA TEST3",
				"CREATE TABLE TEST3.TEST1 ( A NUMBER(6,1), B VARCHAR(16), PRIMARY KEY (A))");
		final PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TEST3.TEST1 VALUES (?,?)");
		final int count = 1000;
		for(int i = 0; i < count; i++){
			pstmt.setBigDecimal(1, new BigDecimal(i));
			pstmt.setString(2, Integer.toString(i));
			final int r = pstmt.executeUpdate();
			Assert.assertEquals(r, 1);
		}
		pstmt.close();
		final PreparedStatement pstmt2 = conn.prepareStatement("SELECT A,B FROM TEST3.TEST1 ORDER BY A");
		final ResultSet rs = pstmt2.executeQuery();
		int j = 0;
		while(rs.next()){
			Assert.assertNotNull(rs.getBigDecimal("A"));
			Assert.assertNotNull(rs.getObject("A"));
			Assert.assertNotNull(rs.getBigDecimal(1));
			Assert.assertNotNull(rs.getString("B"));
			Assert.assertNotNull(rs.getString("B"));
			Assert.assertNotNull(rs.getString(2));
			Assert.assertTrue(rs.getBigDecimal("A").equals(new BigDecimal(j).setScale(1, RoundingMode.HALF_EVEN)));
			Assert.assertTrue(rs.getString("B").equals(Integer.toString(j)));
			j++;
		}
		Assert.assertEquals(j, count);
		rs.close();

		pstmt2.close();
		executeUpdate(stmt, "DELETE FROM TEST3.TEST1", "TRUNCATE TABLE TEST3.TEST1");
		executeUpdate(stmt, "DROP TABLE TEST3.TEST1");
		stmt.close();
	}

	@Test
	public void testParameters() throws Exception{
		final Statement stmt = conn.createStatement();
		executeUpdate(stmt,
				"DROP SCHEMA IF EXISTS TEST3",
				"CREATE SCHEMA TEST3",
				"CREATE TABLE TEST3.TEST1 ( A SMALLINT, B INT, C FLOAT, D REAL, E CLOB)");
		stmt.execute("TRUNCATE TABLE TEST3.TEST1");
		final String s = "Hello World";
		{
			final PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TEST3.TEST1 VALUES(?,?,?,?,?)");
			pstmt.setShort(1, (short)10);
			pstmt.setInt(2, 10);
			pstmt.setFloat(3, 10f);
			pstmt.setDouble(4, 10d);
			final Clob c = conn.createClob();
			Assert.assertNotNull(c);
			pstmt.setString(5, s);
			final boolean hasrs = pstmt.execute();
			Assert.assertFalse(hasrs);
			pstmt.close();
		}
		{
			final Statement stmtq = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			final ResultSet rs = stmtq.executeQuery("SELECT * FROM TEST3.TEST1");
			final boolean next = rs.next();
			Assert.assertTrue(next);
			final short a = rs.getShort("A");
			final int b = rs.getInt("B");
			final float c = rs.getFloat("C");
			final double d = rs.getDouble("D");
			final String e = rs.getString("E");
			Assert.assertEquals(a, (short) 10);
			Assert.assertEquals(b, 10);
			Assert.assertEquals(c, 10f, EPS);
			Assert.assertEquals(d, 10d, EPS);
			Assert.assertEquals(e, s);
			rs.close();
			stmtq.close();
		}
		stmt.close();
	}

	@Test
	public void testMetadata() throws Exception{
		final Statement stmt = conn.createStatement();
		executeUpdate(stmt,
				"DROP SCHEMA IF EXISTS TEST3",
				"CREATE SCHEMA TEST3",
				"CREATE TABLE TEST3.TEST1 ( A SMALLINT, B INT, C FLOAT, D REAL, E CLOB)");
		stmt.execute("TRUNCATE TABLE TEST3.TEST1");
		stmt.close();
		{
			final Statement stmtq = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			final ResultSet rs = stmtq.executeQuery("SELECT * FROM TEST3.TEST1");
			final ResultSetMetaData md = rs.getMetaData();
			Assert.assertEquals(md.getColumnCount(), 5);
			Assert.assertEquals(md.getColumnName(1), "A");
			Assert.assertEquals(md.getColumnName(2), "B");
			Assert.assertEquals(md.getColumnName(3), "C");
			Assert.assertEquals(md.getColumnName(4), "D");
			Assert.assertEquals(md.getColumnName(5), "E");

			Assert.assertEquals(md.getColumnType(1), Types.SMALLINT);
			Assert.assertEquals(md.getColumnType(2), Types.INTEGER);
			Assert.assertEquals(md.getColumnType(3), Types.DOUBLE);
			Assert.assertEquals(md.getColumnType(4), Types.REAL);
			Assert.assertEquals(md.getColumnType(5), Types.CLOB);

			rs.close();
			stmtq.close();
		}
	}
}
