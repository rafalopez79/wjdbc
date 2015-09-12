package com.bzsoft.wjdbc.driver;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.bzsoft.wjdbc.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CloseJdbcObjectsTest extends BaseTest {

	protected Connection	conn;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		conn = setUpClient();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		if (conn != null) {
			conn.close();
		}
	}


	@SuppressWarnings("unchecked")
	@Test
	public void testConnection() throws Exception {
		conn.close();
		final Method[] ms = getMethods(Connection.class, "close", "isValid", "isClosed", "abort", "createArrayOf", "createStruct", "createBlob",
				"createClob", "createNClob", "createSQLXML", "setClientInfo", "setNetworkTimeout");
		for (final Method m : ms) {
			final Connection c = conn;
			final TestRunnable r = new TestRunnable() {

				@Override
				public String getDescription() {
					return m.getName();
				}

				@Override
				public void run() throws Exception {
					invokeMethodWithRandomParams(m, c);
				}
			};
			assertThrowsException(r, SQLException.class);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testStatement() throws Exception {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			conn.close();
			final Method[] ms = getMethods(Statement.class, "close", "isClosed", "closeOnCompletion", "isCloseOnCompletion", "getConnection",
					"executeLargeBatch","executeLargeUpdate","getLargeMaxRows","getLargeUpdateCount","setLargeMaxRows");
			for (final Method m : ms) {
				final Statement c = stmt;
				final TestRunnable r = new TestRunnable() {

					@Override
					public String getDescription() {
						return m.getName();
					}

					@Override
					public void run() throws Exception {
						invokeMethodWithRandomParams(m, c);
					}
				};
				assertThrowsException(r, SQLException.class);
			}
			stmt.close();
			final Method[] msStmt = getMethods(Statement.class, "close", "isClosed", "closeOnCompletion", "isCloseOnCompletion", "getConnection",
					"executeLargeBatch","executeLargeUpdate","getLargeMaxRows","getLargeUpdateCount","setLargeMaxRows");
			for (final Method m : msStmt) {
				final Statement c = stmt;
				final TestRunnable r = new TestRunnable() {

					@Override
					public String getDescription() {
						return m.getName();
					}

					@Override
					public void run() throws Exception {
						invokeMethodWithRandomParams(m, c);
					}
				};
				assertThrowsException(r, SQLException.class);
			}

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPreparedStatement() throws Exception {
		Statement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT 1");
			conn.close();
			final Method[] ms = getMethods(Statement.class, "close", "isClosed", "closeOnCompletion", "isCloseOnCompletion", "getConnection",
					"executeLargeBatch","executeLargeUpdate","getLargeMaxRows","getLargeUpdateCount","setLargeMaxRows");
			for (final Method m : ms) {
				final Statement c = stmt;
				final TestRunnable r = new TestRunnable() {

					@Override
					public String getDescription() {
						return m.getName();
					}

					@Override
					public void run() throws Exception {
						invokeMethodWithRandomParams(m, c);
					}
				};
				assertThrowsException(r, SQLException.class);
			}
			stmt.close();
			final Method[] msStmt = getMethods(Statement.class, "close", "isClosed", "closeOnCompletion", "isCloseOnCompletion", "getConnection",
					"executeLargeBatch","executeLargeUpdate","getLargeMaxRows","getLargeUpdateCount","setLargeMaxRows");
			for (final Method m : msStmt) {
				final Statement c = stmt;
				final TestRunnable r = new TestRunnable() {

					@Override
					public String getDescription() {
						return m.getName();
					}

					@Override
					public void run() throws Exception {
						invokeMethodWithRandomParams(m, c);
					}
				};
				assertThrowsException(r, SQLException.class);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}



	private static Method[] getMethods(final Class<?> clazz, final String... exclusions) {
		final Method[] ms = clazz.getDeclaredMethods();
		final Set<String> sExclusions = new HashSet<String>(Arrays.asList(exclusions));
		final List<Method> list = new ArrayList<Method>();
		for (final Method m : ms) {
			if (!sExclusions.contains(m.getName())) {
				list.add(m);
			}
		}
		return list.toArray(new Method[list.size()]);
	}

	private static Object invokeMethodWithRandomParams(final Method m, final Object o) throws Exception {
		final Class<?>[] params = m.getParameterTypes();
		final Object[] args = new Object[params.length];
		int i = 0;
		for (final Class<?> type : params) {
			args[i] = getRandomObject(type);
			i++;
		}
		return m.invoke(o, args);
	}

}
