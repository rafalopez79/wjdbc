package com.bzsoft.wjdbc.adapter;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class WPreparedStatementAdapter implements PreparedStatement {

	private final PreparedStatement	pstmt;
	private final SQLConverter			converter;

	protected WPreparedStatementAdapter(final PreparedStatement pstmt, final SQLConverter converter) {
		this.pstmt = pstmt;
		this.converter = converter;
	}

	@Override
	public ResultSet executeQuery(final String sql) throws SQLException {
		return pstmt.executeQuery(converter.convert(sql));
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return pstmt.unwrap(iface);
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		return pstmt.executeQuery();
	}

	@Override
	public int executeUpdate(final String sql) throws SQLException {
		return pstmt.executeUpdate(converter.convert(sql));
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return pstmt.isWrapperFor(iface);
	}

	@Override
	public int executeUpdate() throws SQLException {
		return pstmt.executeUpdate();
	}

	@Override
	public void close() throws SQLException {
		pstmt.close();
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
		pstmt.setNull(parameterIndex, sqlType);
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		return pstmt.getMaxFieldSize();
	}

	@Override
	public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
		pstmt.setBoolean(parameterIndex, x);
	}

	@Override
	public void setMaxFieldSize(final int max) throws SQLException {
		pstmt.setMaxFieldSize(max);
	}

	@Override
	public void setByte(final int parameterIndex, final byte x) throws SQLException {
		pstmt.setByte(parameterIndex, x);
	}

	@Override
	public void setShort(final int parameterIndex, final short x) throws SQLException {
		pstmt.setShort(parameterIndex, x);
	}

	@Override
	public int getMaxRows() throws SQLException {
		return pstmt.getMaxRows();
	}

	@Override
	public void setInt(final int parameterIndex, final int x) throws SQLException {
		pstmt.setInt(parameterIndex, x);
	}

	@Override
	public void setMaxRows(final int max) throws SQLException {
		pstmt.setMaxRows(max);
	}

	@Override
	public void setLong(final int parameterIndex, final long x) throws SQLException {
		pstmt.setLong(parameterIndex, x);
	}

	@Override
	public void setEscapeProcessing(final boolean enable) throws SQLException {
		pstmt.setEscapeProcessing(enable);
	}

	@Override
	public void setFloat(final int parameterIndex, final float x) throws SQLException {
		pstmt.setFloat(parameterIndex, x);
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return pstmt.getQueryTimeout();
	}

	@Override
	public void setDouble(final int parameterIndex, final double x) throws SQLException {
		pstmt.setDouble(parameterIndex, x);
	}

	@Override
	public void setQueryTimeout(final int seconds) throws SQLException {
		pstmt.setQueryTimeout(seconds);
	}

	@Override
	public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
		pstmt.setBigDecimal(parameterIndex, x);
	}

	@Override
	public void cancel() throws SQLException {
		pstmt.cancel();
	}

	@Override
	public void setString(final int parameterIndex, final String x) throws SQLException {
		pstmt.setString(parameterIndex, x);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return pstmt.getWarnings();
	}

	@Override
	public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
		pstmt.setBytes(parameterIndex, x);
	}

	@Override
	public void clearWarnings() throws SQLException {
		pstmt.clearWarnings();
	}

	@Override
	public void setDate(final int parameterIndex, final Date x) throws SQLException {
		pstmt.setDate(parameterIndex, x);
	}

	@Override
	public void setCursorName(final String name) throws SQLException {
		pstmt.setCursorName(name);
	}

	@Override
	public void setTime(final int parameterIndex, final Time x) throws SQLException {
		pstmt.setTime(parameterIndex, x);
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
		pstmt.setTimestamp(parameterIndex, x);
	}

	@Override
	public boolean execute(final String sql) throws SQLException {
		return pstmt.execute(sql);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		pstmt.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return pstmt.getResultSet();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		pstmt.setUnicodeStream(parameterIndex, x, length);
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return pstmt.getUpdateCount();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		return pstmt.getMoreResults();
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		pstmt.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void setFetchDirection(final int direction) throws SQLException {
		pstmt.setFetchDirection(direction);
	}

	@Override
	public void clearParameters() throws SQLException {
		pstmt.clearParameters();
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return pstmt.getFetchDirection();
	}

	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
		pstmt.setObject(parameterIndex, x, targetSqlType);
	}

	@Override
	public void setFetchSize(final int rows) throws SQLException {
		pstmt.setFetchSize(rows);
	}

	@Override
	public int getFetchSize() throws SQLException {
		return pstmt.getFetchSize();
	}

	@Override
	public void setObject(final int parameterIndex, final Object x) throws SQLException {
		pstmt.setObject(parameterIndex, x);
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return pstmt.getResultSetConcurrency();
	}

	@Override
	public int getResultSetType() throws SQLException {
		return pstmt.getResultSetType();
	}

	@Override
	public void addBatch(final String sql) throws SQLException {
		pstmt.addBatch(converter.convert(sql));
	}

	@Override
	public void clearBatch() throws SQLException {
		pstmt.clearBatch();
	}

	@Override
	public boolean execute() throws SQLException {
		return pstmt.execute();
	}

	@Override
	public int[] executeBatch() throws SQLException {
		return pstmt.executeBatch();
	}

	@Override
	public void addBatch() throws SQLException {
		pstmt.addBatch();
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
		pstmt.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setRef(final int parameterIndex, final Ref x) throws SQLException {
		pstmt.setRef(parameterIndex, x);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return pstmt.getConnection();
	}

	@Override
	public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
		pstmt.setBlob(parameterIndex, x);
	}

	@Override
	public void setClob(final int parameterIndex, final Clob x) throws SQLException {
		pstmt.setClob(parameterIndex, x);
	}

	@Override
	public boolean getMoreResults(final int current) throws SQLException {
		return pstmt.getMoreResults(current);
	}

	@Override
	public void setArray(final int parameterIndex, final Array x) throws SQLException {
		pstmt.setArray(parameterIndex, x);
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return pstmt.getMetaData();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return pstmt.getGeneratedKeys();
	}

	@Override
	public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
		pstmt.setDate(parameterIndex, x, cal);
	}

	@Override
	public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
		return pstmt.executeUpdate(converter.convert(sql), autoGeneratedKeys);
	}

	@Override
	public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
		pstmt.setTime(parameterIndex, x, cal);
	}

	@Override
	public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
		return pstmt.executeUpdate(converter.convert(sql), columnIndexes);
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
		pstmt.setTimestamp(parameterIndex, x, cal);
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
		pstmt.setNull(parameterIndex, sqlType, typeName);
	}

	@Override
	public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
		return pstmt.executeUpdate(converter.convert(sql), columnNames);
	}

	@Override
	public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
		return pstmt.execute(converter.convert(sql), autoGeneratedKeys);
	}

	@Override
	public void setURL(final int parameterIndex, final URL x) throws SQLException {
		pstmt.setURL(parameterIndex, x);
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return pstmt.getParameterMetaData();
	}

	@Override
	public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
		pstmt.setRowId(parameterIndex, x);
	}

	@Override
	public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
		return pstmt.execute(converter.convert(sql), columnIndexes);
	}

	@Override
	public void setNString(final int parameterIndex, final String value) throws SQLException {
		pstmt.setNString(parameterIndex, value);
	}

	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
		pstmt.setNCharacterStream(parameterIndex, value, length);
	}

	@Override
	public boolean execute(final String sql, final String[] columnNames) throws SQLException {
		return pstmt.execute(converter.convert(sql), columnNames);
	}

	@Override
	public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
		pstmt.setNClob(parameterIndex, value);
	}

	@Override
	public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		pstmt.setClob(parameterIndex, reader, length);
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return pstmt.getResultSetHoldability();
	}

	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
		pstmt.setBlob(parameterIndex, inputStream, length);
	}

	@Override
	public boolean isClosed() throws SQLException {
		return pstmt.isClosed();
	}

	@Override
	public void setPoolable(final boolean poolable) throws SQLException {
		pstmt.setPoolable(poolable);
	}

	@Override
	public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		pstmt.setNClob(parameterIndex, reader, length);
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return pstmt.isPoolable();
	}

	@Override
	public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
		pstmt.setSQLXML(parameterIndex, xmlObject);
	}

	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
		pstmt.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		pstmt.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		pstmt.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		pstmt.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
		pstmt.setAsciiStream(parameterIndex, x);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
		pstmt.setBinaryStream(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
		pstmt.setCharacterStream(parameterIndex, reader);
	}

	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
		pstmt.setNCharacterStream(parameterIndex, value);
	}

	@Override
	public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
		pstmt.setClob(parameterIndex, reader);
	}

	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
		pstmt.setBlob(parameterIndex, inputStream);
	}

	@Override
	public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
		pstmt.setNClob(parameterIndex, reader);
	}

}
