package com.bzsoft.wjdbc.test.mock;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

public class MockDriver implements Driver {

	private final int[]	types;

	public MockDriver() {
		this.types = new int[] { Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.DECIMAL, Types.DOUBLE };
	}

	@Override
	public Connection connect(final String url, final Properties info) throws SQLException {
		return new MockConnection(1000000, types);
	}

	@Override
	public boolean acceptsURL(final String url) throws SQLException {
		return url.contains("mock");
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
		return null;
	}

	@Override
	public int getMajorVersion() {
		return 0;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		return false;
	}

}
