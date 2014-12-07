package com.bzsoft.wjdbc.test.mock;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MockRSMD implements ResultSetMetaData {

	private final int[]	type;

	public MockRSMD(final int[] type) {
		this.type = type;
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public int getColumnCount() throws SQLException {
		return type.length;
	}

	@Override
	public boolean isAutoIncrement(final int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isCaseSensitive(final int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isSearchable(final int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isCurrency(final int column) throws SQLException {
		return false;
	}

	@Override
	public int isNullable(final int column) throws SQLException {
		return 0;
	}

	@Override
	public boolean isSigned(final int column) throws SQLException {
		return false;
	}

	@Override
	public int getColumnDisplaySize(final int column) throws SQLException {
		return 0;
	}

	@Override
	public String getColumnLabel(final int column) throws SQLException {
		return String.valueOf(column);
	}

	@Override
	public String getColumnName(final int column) throws SQLException {
		return String.valueOf(column);
	}

	@Override
	public String getSchemaName(final int column) throws SQLException {
		return "";
	}

	@Override
	public int getPrecision(final int column) throws SQLException {
		return 0;
	}

	@Override
	public int getScale(final int column) throws SQLException {
		return 0;
	}

	@Override
	public String getTableName(final int column) throws SQLException {
		return "";
	}

	@Override
	public String getCatalogName(final int column) throws SQLException {
		return "";
	}

	@Override
	public int getColumnType(final int column) throws SQLException {
		return type[column - 1];
	}

	@Override
	public String getColumnTypeName(final int column) throws SQLException {
		return "";
	}

	@Override
	public boolean isReadOnly(final int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isWritable(final int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isDefinitelyWritable(final int column) throws SQLException {
		return false;
	}

	@Override
	public String getColumnClassName(final int column) throws SQLException {
		return "";
	}

}
