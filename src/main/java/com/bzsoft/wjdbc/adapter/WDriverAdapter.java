package com.bzsoft.wjdbc.adapter;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import com.bzsoft.wjdbc.WDriver;

public class WDriverAdapter implements Driver {

	private static String URL_BASE_PREFIX="jdbc:wjdbc:";
	private static String URL_PREFIX="itx:jdbc:wjdbc:";
	private final Driver realDriver;

	static {
		try {
			DriverManager.registerDriver(new WDriverAdapter());
		} catch (final SQLException e) {
			throw new RuntimeException("Couldn't register Wirtual-JDBC-DriverAdapter !", e);
		}
	}

	public WDriverAdapter() {
		realDriver = new WDriver();
	}

	@Override
	public Connection connect(final String url, final Properties info) throws SQLException {
		if (acceptsURL(url)){
			final SQLConverter conv = new ThreadLocalSQLConverter();
			return new WConnectionAdapter(realDriver.connect(getBaseURL(url), info), conv);
		}
		return null;
	}

	@Override
	public boolean acceptsURL(final String url) throws SQLException {
		return url.startsWith(URL_PREFIX);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
		return realDriver.getPropertyInfo(getBaseURL(url), info);
	}

	@Override
	public int getMajorVersion() {
		return realDriver.getMajorVersion();
	}

	@Override
	public int getMinorVersion() {
		return realDriver.getMinorVersion();
	}

	@Override
	public boolean jdbcCompliant() {
		return realDriver.jdbcCompliant();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return realDriver.getParentLogger();
	}

	private static String getBaseURL(final String url){
		return URL_BASE_PREFIX + url.substring(URL_PREFIX.length());
	}
}
