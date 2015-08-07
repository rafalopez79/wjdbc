package com.bzsoft.wjdbc.adapter;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
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
import java.util.Map;

public class WCallableStatementAdapter implements CallableStatement {

	private final CallableStatement	cstmt;
	private final SQLConverter			converter;

	protected WCallableStatementAdapter(final CallableStatement cstmt, final SQLConverter converter) {
		this.cstmt = cstmt;
		this.converter = converter;
	}

	@Override
	public ResultSet executeQuery(final String sql) throws SQLException {
		return cstmt.executeQuery(converter.convert(sql));
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return cstmt.unwrap(iface);
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		return cstmt.executeQuery();
	}

	@Override
	public int executeUpdate(final String sql) throws SQLException {
		return cstmt.executeUpdate(converter.convert(sql));
	}

	@Override
	public void registerOutParameter(final int parameterIndex, final int sqlType) throws SQLException {
		cstmt.registerOutParameter(parameterIndex, sqlType);
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return cstmt.isWrapperFor(iface);
	}

	@Override
	public int executeUpdate() throws SQLException {
		return cstmt.executeUpdate();
	}

	@Override
	public void close() throws SQLException {
		cstmt.close();
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
		cstmt.setNull(parameterIndex, sqlType);
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		return cstmt.getMaxFieldSize();
	}

	@Override
	public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {
		cstmt.registerOutParameter(parameterIndex, sqlType, scale);
	}

	@Override
	public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
		cstmt.setBoolean(parameterIndex, x);
	}

	@Override
	public void setMaxFieldSize(final int max) throws SQLException {
		cstmt.setMaxFieldSize(max);
	}

	@Override
	public void setByte(final int parameterIndex, final byte x) throws SQLException {
		cstmt.setByte(parameterIndex, x);
	}

	@Override
	public void setShort(final int parameterIndex, final short x) throws SQLException {
		cstmt.setShort(parameterIndex, x);
	}

	@Override
	public int getMaxRows() throws SQLException {
		return cstmt.getMaxRows();
	}

	@Override
	public boolean wasNull() throws SQLException {
		return cstmt.wasNull();
	}

	@Override
	public void setInt(final int parameterIndex, final int x) throws SQLException {
		cstmt.setInt(parameterIndex, x);
	}

	@Override
	public void setMaxRows(final int max) throws SQLException {
		cstmt.setMaxRows(max);
	}

	@Override
	public String getString(final int parameterIndex) throws SQLException {
		return cstmt.getString(parameterIndex);
	}

	@Override
	public void setLong(final int parameterIndex, final long x) throws SQLException {
		cstmt.setLong(parameterIndex, x);
	}

	@Override
	public void setEscapeProcessing(final boolean enable) throws SQLException {
		cstmt.setEscapeProcessing(enable);
	}

	@Override
	public boolean getBoolean(final int parameterIndex) throws SQLException {
		return cstmt.getBoolean(parameterIndex);
	}

	@Override
	public void setFloat(final int parameterIndex, final float x) throws SQLException {
		cstmt.setFloat(parameterIndex, x);
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return cstmt.getQueryTimeout();
	}

	@Override
	public void setDouble(final int parameterIndex, final double x) throws SQLException {
		cstmt.setDouble(parameterIndex, x);
	}

	@Override
	public byte getByte(final int parameterIndex) throws SQLException {
		return cstmt.getByte(parameterIndex);
	}

	@Override
	public void setQueryTimeout(final int seconds) throws SQLException {
		cstmt.setQueryTimeout(seconds);
	}

	@Override
	public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
		cstmt.setBigDecimal(parameterIndex, x);
	}

	@Override
	public short getShort(final int parameterIndex) throws SQLException {
		return cstmt.getShort(parameterIndex);
	}

	@Override
	public void cancel() throws SQLException {
		cstmt.cancel();
	}

	@Override
	public void setString(final int parameterIndex, final String x) throws SQLException {
		cstmt.setString(parameterIndex, x);
	}

	@Override
	public int getInt(final int parameterIndex) throws SQLException {
		return cstmt.getInt(parameterIndex);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return cstmt.getWarnings();
	}

	@Override
	public long getLong(final int parameterIndex) throws SQLException {
		return cstmt.getLong(parameterIndex);
	}

	@Override
	public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
		cstmt.setBytes(parameterIndex, x);
	}

	@Override
	public float getFloat(final int parameterIndex) throws SQLException {
		return cstmt.getFloat(parameterIndex);
	}

	@Override
	public void clearWarnings() throws SQLException {
		cstmt.clearWarnings();
	}

	@Override
	public void setDate(final int parameterIndex, final Date x) throws SQLException {
		cstmt.setDate(parameterIndex, x);
	}

	@Override
	public void setCursorName(final String name) throws SQLException {
		cstmt.setCursorName(name);
	}

	@Override
	public double getDouble(final int parameterIndex) throws SQLException {
		return cstmt.getDouble(parameterIndex);
	}

	@Override
	public void setTime(final int parameterIndex, final Time x) throws SQLException {
		cstmt.setTime(parameterIndex, x);
	}

	@SuppressWarnings("deprecation")
	@Override
	public BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
		return cstmt.getBigDecimal(parameterIndex, scale);
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
		cstmt.setTimestamp(parameterIndex, x);
	}

	@Override
	public boolean execute(final String sql) throws SQLException {
		return cstmt.execute(converter.convert(sql));
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		cstmt.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public byte[] getBytes(final int parameterIndex) throws SQLException {
		return cstmt.getBytes(parameterIndex);
	}

	@Override
	public Date getDate(final int parameterIndex) throws SQLException {
		return cstmt.getDate(parameterIndex);
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return cstmt.getResultSet();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		cstmt.setUnicodeStream(parameterIndex, x, length);
	}

	@Override
	public Time getTime(final int parameterIndex) throws SQLException {
		return cstmt.getTime(parameterIndex);
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return cstmt.getUpdateCount();
	}

	@Override
	public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
		return cstmt.getTimestamp(parameterIndex);
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		return cstmt.getMoreResults();
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		cstmt.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public Object getObject(final int parameterIndex) throws SQLException {
		return cstmt.getObject(parameterIndex);
	}

	@Override
	public void setFetchDirection(final int direction) throws SQLException {
		cstmt.setFetchDirection(direction);
	}

	@Override
	public void clearParameters() throws SQLException {
		cstmt.clearParameters();
	}

	@Override
	public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
		return cstmt.getBigDecimal(parameterIndex);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return cstmt.getFetchDirection();
	}

	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
		cstmt.setObject(parameterIndex, x, targetSqlType);
	}

	@Override
	public Object getObject(final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
		return cstmt.getObject(parameterIndex, map);
	}

	@Override
	public void setFetchSize(final int rows) throws SQLException {
		cstmt.setFetchSize(rows);
	}

	@Override
	public int getFetchSize() throws SQLException {
		return cstmt.getFetchSize();
	}

	@Override
	public void setObject(final int parameterIndex, final Object x) throws SQLException {
		cstmt.setObject(parameterIndex, x);
	}

	@Override
	public Ref getRef(final int parameterIndex) throws SQLException {
		return cstmt.getRef(parameterIndex);
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return cstmt.getResultSetConcurrency();
	}

	@Override
	public Blob getBlob(final int parameterIndex) throws SQLException {
		return cstmt.getBlob(parameterIndex);
	}

	@Override
	public int getResultSetType() throws SQLException {
		return cstmt.getResultSetType();
	}

	@Override
	public void addBatch(final String sql) throws SQLException {
		cstmt.addBatch(converter.convert(sql));
	}

	@Override
	public Clob getClob(final int parameterIndex) throws SQLException {
		return cstmt.getClob(parameterIndex);
	}

	@Override
	public void clearBatch() throws SQLException {
		cstmt.clearBatch();
	}

	@Override
	public boolean execute() throws SQLException {
		return cstmt.execute();
	}

	@Override
	public Array getArray(final int parameterIndex) throws SQLException {
		return cstmt.getArray(parameterIndex);
	}

	@Override
	public int[] executeBatch() throws SQLException {
		return cstmt.executeBatch();
	}

	@Override
	public Date getDate(final int parameterIndex, final Calendar cal) throws SQLException {
		return cstmt.getDate(parameterIndex, cal);
	}

	@Override
	public void addBatch() throws SQLException {
		cstmt.addBatch();
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
		cstmt.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public Time getTime(final int parameterIndex, final Calendar cal) throws SQLException {
		return cstmt.getTime(parameterIndex, cal);
	}

	@Override
	public void setRef(final int parameterIndex, final Ref x) throws SQLException {
		cstmt.setRef(parameterIndex, x);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return cstmt.getConnection();
	}

	@Override
	public Timestamp getTimestamp(final int parameterIndex, final Calendar cal) throws SQLException {
		return cstmt.getTimestamp(parameterIndex, cal);
	}

	@Override
	public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
		cstmt.setBlob(parameterIndex, x);
	}

	@Override
	public void registerOutParameter(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
		cstmt.registerOutParameter(parameterIndex, sqlType, typeName);
	}

	@Override
	public void setClob(final int parameterIndex, final Clob x) throws SQLException {
		cstmt.setClob(parameterIndex, x);
	}

	@Override
	public boolean getMoreResults(final int current) throws SQLException {
		return cstmt.getMoreResults(current);
	}

	@Override
	public void setArray(final int parameterIndex, final Array x) throws SQLException {
		cstmt.setArray(parameterIndex, x);
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return cstmt.getMetaData();
	}

	@Override
	public void registerOutParameter(final String parameterName, final int sqlType) throws SQLException {
		cstmt.registerOutParameter(parameterName, sqlType);
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return cstmt.getGeneratedKeys();
	}

	@Override
	public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
		cstmt.setDate(parameterIndex, x, cal);
	}

	@Override
	public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
		return cstmt.executeUpdate(converter.convert(sql), autoGeneratedKeys);
	}

	@Override
	public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
		cstmt.setTime(parameterIndex, x, cal);
	}

	@Override
	public void registerOutParameter(final String parameterName, final int sqlType, final int scale) throws SQLException {
		cstmt.registerOutParameter(parameterName, sqlType, scale);
	}

	@Override
	public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
		return cstmt.executeUpdate(converter.convert(sql), columnIndexes);
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
		cstmt.setTimestamp(parameterIndex, x, cal);
	}

	@Override
	public void registerOutParameter(final String parameterName, final int sqlType, final String typeName) throws SQLException {
		cstmt.registerOutParameter(parameterName, sqlType, typeName);
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
		cstmt.setNull(parameterIndex, sqlType, typeName);
	}

	@Override
	public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
		return cstmt.executeUpdate(converter.convert(sql), columnNames);
	}

	@Override
	public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
		return cstmt.execute(converter.convert(sql), autoGeneratedKeys);
	}

	@Override
	public URL getURL(final int parameterIndex) throws SQLException {
		return cstmt.getURL(parameterIndex);
	}

	@Override
	public void setURL(final int parameterIndex, final URL x) throws SQLException {
		cstmt.setURL(parameterIndex, x);
	}

	@Override
	public void setURL(final String parameterName, final URL val) throws SQLException {
		cstmt.setURL(parameterName, val);
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return cstmt.getParameterMetaData();
	}

	@Override
	public void setNull(final String parameterName, final int sqlType) throws SQLException {
		cstmt.setNull(parameterName, sqlType);
	}

	@Override
	public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
		cstmt.setRowId(parameterIndex, x);
	}

	@Override
	public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
		return cstmt.execute(converter.convert(sql), columnIndexes);
	}

	@Override
	public void setBoolean(final String parameterName, final boolean x) throws SQLException {
		cstmt.setBoolean(parameterName, x);
	}

	@Override
	public void setNString(final int parameterIndex, final String value) throws SQLException {
		cstmt.setNString(parameterIndex, value);
	}

	@Override
	public void setByte(final String parameterName, final byte x) throws SQLException {
		cstmt.setByte(parameterName, x);
	}

	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
		cstmt.setNCharacterStream(parameterIndex, value, length);
	}

	@Override
	public void setShort(final String parameterName, final short x) throws SQLException {
		cstmt.setShort(parameterName, x);
	}

	@Override
	public void setInt(final String parameterName, final int x) throws SQLException {
		cstmt.setInt(parameterName, x);
	}

	@Override
	public boolean execute(final String sql, final String[] columnNames) throws SQLException {
		return cstmt.execute(converter.convert(sql), columnNames);
	}

	@Override
	public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
		cstmt.setNClob(parameterIndex, value);
	}

	@Override
	public void setLong(final String parameterName, final long x) throws SQLException {
		cstmt.setLong(parameterName, x);
	}

	@Override
	public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		cstmt.setClob(parameterIndex, reader, length);
	}

	@Override
	public void setFloat(final String parameterName, final float x) throws SQLException {
		cstmt.setFloat(parameterName, x);
	}

	@Override
	public void setDouble(final String parameterName, final double x) throws SQLException {
		cstmt.setDouble(parameterName, x);
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return cstmt.getResultSetHoldability();
	}

	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
		cstmt.setBlob(parameterIndex, inputStream, length);
	}

	@Override
	public boolean isClosed() throws SQLException {
		return cstmt.isClosed();
	}

	@Override
	public void setBigDecimal(final String parameterName, final BigDecimal x) throws SQLException {
		cstmt.setBigDecimal(parameterName, x);
	}

	@Override
	public void setPoolable(final boolean poolable) throws SQLException {
		cstmt.setPoolable(poolable);
	}

	@Override
	public void setString(final String parameterName, final String x) throws SQLException {
		cstmt.setString(parameterName, x);
	}

	@Override
	public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		cstmt.setNClob(parameterIndex, reader, length);
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return cstmt.isPoolable();
	}

	@Override
	public void setBytes(final String parameterName, final byte[] x) throws SQLException {
		cstmt.setBytes(parameterName, x);
	}

	@Override
	public void setDate(final String parameterName, final Date x) throws SQLException {
		cstmt.setDate(parameterName, x);
	}

	@Override
	public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
		cstmt.setSQLXML(parameterIndex, xmlObject);
	}

	@Override
	public void setTime(final String parameterName, final Time x) throws SQLException {
		cstmt.setTime(parameterName, x);
	}

	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
		cstmt.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void setTimestamp(final String parameterName, final Timestamp x) throws SQLException {
		cstmt.setTimestamp(parameterName, x);
	}

	@Override
	public void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {
		cstmt.setAsciiStream(parameterName, x, length);
	}

	@Override
	public void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {
		cstmt.setBinaryStream(parameterName, x, length);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		cstmt.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setObject(final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
		cstmt.setObject(parameterName, x, targetSqlType, scale);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		cstmt.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		cstmt.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {
		cstmt.setObject(parameterName, x, targetSqlType);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
		cstmt.setAsciiStream(parameterIndex, x);
	}

	@Override
	public void setObject(final String parameterName, final Object x) throws SQLException {
		cstmt.setObject(parameterName, x);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
		cstmt.setBinaryStream(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(final String parameterName, final Reader reader, final int length) throws SQLException {
		cstmt.setCharacterStream(parameterName, reader, length);
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
		cstmt.setCharacterStream(parameterIndex, reader);
	}

	@Override
	public void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {
		cstmt.setDate(parameterName, x, cal);
	}

	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
		cstmt.setNCharacterStream(parameterIndex, value);
	}

	@Override
	public void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {
		cstmt.setTime(parameterName, x, cal);
	}

	@Override
	public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
		cstmt.setClob(parameterIndex, reader);
	}

	@Override
	public void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
		cstmt.setTimestamp(parameterName, x, cal);
	}

	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
		cstmt.setBlob(parameterIndex, inputStream);
	}

	@Override
	public void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {
		cstmt.setNull(parameterName, sqlType, typeName);
	}

	@Override
	public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
		cstmt.setNClob(parameterIndex, reader);
	}

	@Override
	public String getString(final String parameterName) throws SQLException {
		return cstmt.getString(parameterName);
	}

	@Override
	public boolean getBoolean(final String parameterName) throws SQLException {
		return cstmt.getBoolean(parameterName);
	}

	@Override
	public byte getByte(final String parameterName) throws SQLException {
		return cstmt.getByte(parameterName);
	}

	@Override
	public short getShort(final String parameterName) throws SQLException {
		return cstmt.getShort(parameterName);
	}

	@Override
	public int getInt(final String parameterName) throws SQLException {
		return cstmt.getInt(parameterName);
	}

	@Override
	public long getLong(final String parameterName) throws SQLException {
		return cstmt.getLong(parameterName);
	}

	@Override
	public float getFloat(final String parameterName) throws SQLException {
		return cstmt.getFloat(parameterName);
	}

	@Override
	public double getDouble(final String parameterName) throws SQLException {
		return cstmt.getDouble(parameterName);
	}

	@Override
	public byte[] getBytes(final String parameterName) throws SQLException {
		return cstmt.getBytes(parameterName);
	}

	@Override
	public Date getDate(final String parameterName) throws SQLException {
		return cstmt.getDate(parameterName);
	}

	@Override
	public Time getTime(final String parameterName) throws SQLException {
		return cstmt.getTime(parameterName);
	}

	@Override
	public Timestamp getTimestamp(final String parameterName) throws SQLException {
		return cstmt.getTimestamp(parameterName);
	}

	@Override
	public Object getObject(final String parameterName) throws SQLException {
		return cstmt.getObject(parameterName);
	}

	@Override
	public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
		return cstmt.getBigDecimal(parameterName);
	}

	@Override
	public Object getObject(final String parameterName, final Map<String, Class<?>> map) throws SQLException {
		return cstmt.getObject(parameterName, map);
	}

	@Override
	public Ref getRef(final String parameterName) throws SQLException {
		return cstmt.getRef(parameterName);
	}

	@Override
	public Blob getBlob(final String parameterName) throws SQLException {
		return cstmt.getBlob(parameterName);
	}

	@Override
	public Clob getClob(final String parameterName) throws SQLException {
		return cstmt.getClob(parameterName);
	}

	@Override
	public Array getArray(final String parameterName) throws SQLException {
		return cstmt.getArray(parameterName);
	}

	@Override
	public Date getDate(final String parameterName, final Calendar cal) throws SQLException {
		return cstmt.getDate(parameterName, cal);
	}

	@Override
	public Time getTime(final String parameterName, final Calendar cal) throws SQLException {
		return cstmt.getTime(parameterName, cal);
	}

	@Override
	public Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
		return cstmt.getTimestamp(parameterName, cal);
	}

	@Override
	public URL getURL(final String parameterName) throws SQLException {
		return cstmt.getURL(parameterName);
	}

	@Override
	public RowId getRowId(final int parameterIndex) throws SQLException {
		return cstmt.getRowId(parameterIndex);
	}

	@Override
	public RowId getRowId(final String parameterName) throws SQLException {
		return cstmt.getRowId(parameterName);
	}

	@Override
	public void setRowId(final String parameterName, final RowId x) throws SQLException {
		cstmt.setRowId(parameterName, x);
	}

	@Override
	public void setNString(final String parameterName, final String value) throws SQLException {
		cstmt.setNString(parameterName, value);
	}

	@Override
	public void setNCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
		cstmt.setNCharacterStream(parameterName, value, length);
	}

	@Override
	public void setNClob(final String parameterName, final NClob value) throws SQLException {
		cstmt.setNClob(parameterName, value);
	}

	@Override
	public void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {
		cstmt.setClob(parameterName, reader, length);
	}

	@Override
	public void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {
		cstmt.setBlob(parameterName, inputStream, length);
	}

	@Override
	public void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {
		cstmt.setNClob(parameterName, reader, length);
	}

	@Override
	public NClob getNClob(final int parameterIndex) throws SQLException {
		return cstmt.getNClob(parameterIndex);
	}

	@Override
	public NClob getNClob(final String parameterName) throws SQLException {
		return cstmt.getNClob(parameterName);
	}

	@Override
	public void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {
		cstmt.setSQLXML(parameterName, xmlObject);
	}

	@Override
	public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
		return cstmt.getSQLXML(parameterIndex);
	}

	@Override
	public SQLXML getSQLXML(final String parameterName) throws SQLException {
		return cstmt.getSQLXML(parameterName);
	}

	@Override
	public String getNString(final int parameterIndex) throws SQLException {
		return cstmt.getNString(parameterIndex);
	}

	@Override
	public String getNString(final String parameterName) throws SQLException {
		return cstmt.getNString(parameterName);
	}

	@Override
	public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
		return cstmt.getNCharacterStream(parameterIndex);
	}

	@Override
	public Reader getNCharacterStream(final String parameterName) throws SQLException {
		return cstmt.getNCharacterStream(parameterName);
	}

	@Override
	public Reader getCharacterStream(final int parameterIndex) throws SQLException {
		return cstmt.getCharacterStream(parameterIndex);
	}

	@Override
	public Reader getCharacterStream(final String parameterName) throws SQLException {
		return cstmt.getCharacterStream(parameterName);
	}

	@Override
	public void setBlob(final String parameterName, final Blob x) throws SQLException {
		cstmt.setBlob(parameterName, x);
	}

	@Override
	public void setClob(final String parameterName, final Clob x) throws SQLException {
		cstmt.setClob(parameterName, x);
	}

	@Override
	public void setAsciiStream(final String parameterName, final InputStream x, final long length) throws SQLException {
		cstmt.setAsciiStream(parameterName, x, length);
	}

	@Override
	public void setBinaryStream(final String parameterName, final InputStream x, final long length) throws SQLException {
		cstmt.setBinaryStream(parameterName, x, length);
	}

	@Override
	public void setCharacterStream(final String parameterName, final Reader reader, final long length) throws SQLException {
		cstmt.setCharacterStream(parameterName, reader, length);
	}

	@Override
	public void setAsciiStream(final String parameterName, final InputStream x) throws SQLException {
		cstmt.setAsciiStream(parameterName, x);
	}

	@Override
	public void setBinaryStream(final String parameterName, final InputStream x) throws SQLException {
		cstmt.setBinaryStream(parameterName, x);
	}

	@Override
	public void setCharacterStream(final String parameterName, final Reader reader) throws SQLException {
		cstmt.setCharacterStream(parameterName, reader);
	}

	@Override
	public void setNCharacterStream(final String parameterName, final Reader value) throws SQLException {
		cstmt.setNCharacterStream(parameterName, value);
	}

	@Override
	public void setClob(final String parameterName, final Reader reader) throws SQLException {
		cstmt.setClob(parameterName, reader);
	}

	@Override
	public void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {
		cstmt.setBlob(parameterName, inputStream);
	}

	@Override
	public void setNClob(final String parameterName, final Reader reader) throws SQLException {
		cstmt.setNClob(parameterName, reader);
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return cstmt.isCloseOnCompletion();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		cstmt.closeOnCompletion();
	}

	@Override
	public <T> T getObject(final int col, final Class<T> clazz) throws SQLException {
		return cstmt.getObject(col, clazz);
	}

	@Override
	public <T> T getObject(final String col, final Class<T> clazz) throws SQLException {
		return cstmt.getObject(col, clazz);
	}
}
