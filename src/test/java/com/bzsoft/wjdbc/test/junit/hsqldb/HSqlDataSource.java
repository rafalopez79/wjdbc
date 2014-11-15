package com.bzsoft.wjdbc.test.junit.hsqldb;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class HSqlDataSource implements DataSource {
	public HSqlDataSource() throws SQLException {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (final ClassNotFoundException e) {
			throw new SQLException(e.getMessage());
		}
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public void setLoginTimeout(final int seconds) throws SQLException {
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(final PrintWriter out) throws SQLException {
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:.");
	}

	@Override
	public Connection getConnection(final String username, final String password) throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:.", username, password);
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return iface.isAssignableFrom(HSqlDataSource.class);
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return (T) this;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("getParentLogger");
	}
}
