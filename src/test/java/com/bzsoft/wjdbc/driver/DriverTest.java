package com.bzsoft.wjdbc.driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.bzsoft.wjdbc.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DriverTest extends BaseTest{

	private Connection conn;

	@Before
	@Override
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
	public void testConnection() throws Exception{
		final Statement stmt = conn.createStatement();
		executeUpdate(stmt,
				"DROP SCHEMA IF EXISTS TEST0",
				"CREATE SCHEMA TEST0",
				"CREATE TABLE TEST0.TEST1 ( A INT, B VARCHAR(16), PRIMARY KEY (A))");
		final PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TEST0.TEST1 VALUES (?,?)");
		for(int i = 0; i < 1000; i++){
			pstmt.setInt(1, i);
			pstmt.setString(2, Integer.toString(i));
			final int c = pstmt.executeUpdate();
			Assert.assertTrue(c == 1);
		}
		executeUpdate(stmt, "DELETE FROM TEST0.TEST1");
		pstmt.close();
		final PreparedStatement pstmt2 = conn.prepareStatement("INSERT INTO TEST0.TEST1 VALUES (?,?)");
		for(int i = 0; i < 1000; i++){
			pstmt2.setInt(1, i);
			pstmt2.setString(2, Integer.toString(i));
			pstmt2.addBatch();
		}
		final int[] r = pstmt2.executeBatch();
		Assert.assertTrue(r != null && r.length == 1000);
		pstmt2.close();
		executeUpdate(stmt, "DELETE FROM TEST0.TEST1");
		executeUpdate(stmt, "DROP TABLE TEST0.TEST1");
		stmt.close();
	}
}
