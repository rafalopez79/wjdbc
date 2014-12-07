package com.bzsoft.wjdbc.test.mock;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

public class MockResultSet implements ResultSet {

	private final int		numRows;
	private int				count;
	private final Random	rand;
	private final int[]	types;

	public MockResultSet(final int numRows, final int[] types) {
		this.numRows = numRows;
		this.count = 0;
		this.types = types;
		this.rand = new Random(System.currentTimeMillis());
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
	public boolean next() throws SQLException {
		count++;
		return count <= numRows;
	}

	@Override
	public void close() throws SQLException {

	}

	@Override
	public boolean wasNull() throws SQLException {
		return false;
	}

	@Override
	public String getString(final int columnIndex) throws SQLException {
		return String.valueOf(rand.nextLong());
	}

	@Override
	public boolean getBoolean(final int columnIndex) throws SQLException {
		return rand.nextBoolean();
	}

	@Override
	public byte getByte(final int columnIndex) throws SQLException {
		return (byte) rand.nextInt();
	}

	@Override
	public short getShort(final int columnIndex) throws SQLException {
		return (short) rand.nextInt();
	}

	@Override
	public int getInt(final int columnIndex) throws SQLException {
		return rand.nextInt();
	}

	@Override
	public long getLong(final int columnIndex) throws SQLException {
		return rand.nextLong();
	}

	@Override
	public float getFloat(final int columnIndex) throws SQLException {
		return rand.nextFloat();
	}

	@Override
	public double getDouble(final int columnIndex) throws SQLException {
		return rand.nextDouble();
	}

	@Override
	public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
		return new BigDecimal(rand.nextDouble());
	}

	@Override
	public byte[] getBytes(final int columnIndex) throws SQLException {
		return String.valueOf(rand.nextLong()).getBytes();
	}

	@Override
	public Date getDate(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public InputStream getAsciiStream(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public InputStream getBinaryStream(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public String getString(final String columnLabel) throws SQLException {
		return String.valueOf(rand.nextLong());
	}

	@Override
	public boolean getBoolean(final String columnLabel) throws SQLException {
		return rand.nextBoolean();
	}

	@Override
	public byte getByte(final String columnLabel) throws SQLException {
		return (byte) rand.nextInt();
	}

	@Override
	public short getShort(final String columnLabel) throws SQLException {
		return (short) rand.nextInt();
	}

	@Override
	public int getInt(final String columnLabel) throws SQLException {
		return rand.nextInt();
	}

	@Override
	public long getLong(final String columnLabel) throws SQLException {
		return rand.nextLong();
	}

	@Override
	public float getFloat(final String columnLabel) throws SQLException {
		return rand.nextFloat();
	}

	@Override
	public double getDouble(final String columnLabel) throws SQLException {
		return rand.nextDouble();
	}

	@Override
	public BigDecimal getBigDecimal(final String columnLabel, final int scale) throws SQLException {
		return new BigDecimal(rand.nextDouble());
	}

	@Override
	public byte[] getBytes(final String columnLabel) throws SQLException {
		return String.valueOf(rand.nextLong()).getBytes();
	}

	@Override
	public Date getDate(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public InputStream getAsciiStream(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public InputStream getUnicodeStream(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public InputStream getBinaryStream(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {

	}

	@Override
	public String getCursorName() throws SQLException {
		return null;
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return new MockRSMD(types);
	}

	@Override
	public Object getObject(final int columnIndex) throws SQLException {
		return rand.nextLong();
	}

	@Override
	public Object getObject(final String columnLabel) throws SQLException {
		return rand.nextLong();
	}

	@Override
	public int findColumn(final String columnLabel) throws SQLException {
		return 0;
	}

	@Override
	public Reader getCharacterStream(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Reader getCharacterStream(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
		return new BigDecimal(rand.nextDouble());
	}

	@Override
	public BigDecimal getBigDecimal(final String columnLabel) throws SQLException {
		return new BigDecimal(rand.nextDouble());
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return false;
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFirst() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLast() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void beforeFirst() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterLast() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean first() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean last() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRow() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean absolute(final int row) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean relative(final int rows) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean previous() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFetchDirection(final int direction) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFetchSize(final int rows) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateNull(final int columnIndex) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(final int columnIndex, final byte x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(final int columnIndex, final short x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(final int columnIndex, final int x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(final int columnIndex, final long x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(final int columnIndex, final float x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(final int columnIndex, final double x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(final int columnIndex, final String x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(final int columnIndex, final Date x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(final int columnIndex, final Time x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(final int columnIndex, final Object x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNull(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(final String columnLabel, final boolean x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(final String columnLabel, final byte x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(final String columnLabel, final short x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(final String columnLabel, final int x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(final String columnLabel, final long x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(final String columnLabel, final float x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(final String columnLabel, final double x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(final String columnLabel, final BigDecimal x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(final String columnLabel, final String x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(final String columnLabel, final byte[] x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(final String columnLabel, final Date x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(final String columnLabel, final Time x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(final String columnLabel, final Timestamp x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(final String columnLabel, final Reader reader, final int length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(final String columnLabel, final Object x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveToInsertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Statement getStatement() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(final int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(final int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(final int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Array getArray(final int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Array getArray(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(final String columnLabel, final Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(final String columnLabel, final Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(final String columnLabel, final Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURL(final int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURL(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateRef(final int columnIndex, final Ref x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRef(final String columnLabel, final Ref x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(final String columnLabel, final Blob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(final int columnIndex, final Clob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(final String columnLabel, final Clob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateArray(final int columnIndex, final Array x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateArray(final String columnLabel, final Array x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public RowId getRowId(final int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowId getRowId(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowId(final String columnLabel, final RowId x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateNString(final int columnIndex, final String nString) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNString(final String columnLabel, final String nString) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(final String columnLabel, final NClob nClob) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public NClob getNClob(final int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(final int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getNString(final int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(final int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(final String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(final String columnLabel, final InputStream inputStream, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(final int columnIndex, final Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(final String columnLabel, final Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(final String columnLabel, final Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

}
