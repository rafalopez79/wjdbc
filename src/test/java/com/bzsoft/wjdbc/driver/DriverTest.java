package com.bzsoft.wjdbc.driver;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.bzsoft.wjdbc.BaseTest;
import com.bzsoft.wjdbc.server.rmi.ConnectionServer;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DriverTest extends BaseTest{

	private ConnectionServer cs;
	private Connection conn;

	@Before
	public void setUp() throws Exception{
		cs = setUpServer();
		conn = setUpClient();
	}

	@After
	public void tearDown() throws Exception{
		if (conn != null){
			conn.close();
		}
		tearDownServer(cs);
	}

	@Test
	public void testConnection() throws Exception{
		final Statement stmt = conn.createStatement();
		executeUpdate(stmt, "DROP DATABASE IF EXISTS TEST", "CREATE DATABASE TEST");
		stmt.close();
	}

}
