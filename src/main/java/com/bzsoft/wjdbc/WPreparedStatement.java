package com.bzsoft.wjdbc;

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
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.bzsoft.wjdbc.command.DecoratedCommandSink;
import com.bzsoft.wjdbc.command.JdbcInterfaceType;
import com.bzsoft.wjdbc.command.PreparedStatementExecuteBatchCommand;
import com.bzsoft.wjdbc.command.PreparedStatementExecuteCommand;
import com.bzsoft.wjdbc.command.PreparedStatementQueryCommand;
import com.bzsoft.wjdbc.command.PreparedStatementUpdateCommand;
import com.bzsoft.wjdbc.command.ReflectiveCommand;
import com.bzsoft.wjdbc.parameters.ArrayParameter;
import com.bzsoft.wjdbc.parameters.BigDecimalParameter;
import com.bzsoft.wjdbc.parameters.BlobParameter;
import com.bzsoft.wjdbc.parameters.BooleanParameter;
import com.bzsoft.wjdbc.parameters.ByteArrayParameter;
import com.bzsoft.wjdbc.parameters.ByteParameter;
import com.bzsoft.wjdbc.parameters.ByteStreamParameter;
import com.bzsoft.wjdbc.parameters.CharStreamParameter;
import com.bzsoft.wjdbc.parameters.ClobParameter;
import com.bzsoft.wjdbc.parameters.DateParameter;
import com.bzsoft.wjdbc.parameters.DoubleParameter;
import com.bzsoft.wjdbc.parameters.FloatParameter;
import com.bzsoft.wjdbc.parameters.IntegerParameter;
import com.bzsoft.wjdbc.parameters.LongParameter;
import com.bzsoft.wjdbc.parameters.NullParameter;
import com.bzsoft.wjdbc.parameters.ObjectParameter;
import com.bzsoft.wjdbc.parameters.PreparedStatementParameter;
import com.bzsoft.wjdbc.parameters.RefParameter;
import com.bzsoft.wjdbc.parameters.RowIdParameter;
import com.bzsoft.wjdbc.parameters.SQLXMLParameter;
import com.bzsoft.wjdbc.parameters.ShortParameter;
import com.bzsoft.wjdbc.parameters.StringParameter;
import com.bzsoft.wjdbc.parameters.TimeParameter;
import com.bzsoft.wjdbc.parameters.TimestampParameter;
import com.bzsoft.wjdbc.parameters.URLParameter;
import com.bzsoft.wjdbc.serial.SerialBlob;
import com.bzsoft.wjdbc.serial.SerialClob;
import com.bzsoft.wjdbc.serial.SerialNClob;
import com.bzsoft.wjdbc.serial.SerialResultSetMetaData;
import com.bzsoft.wjdbc.serial.SerialSQLXML;
import com.bzsoft.wjdbc.serial.StreamingResultSet;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;
import com.bzsoft.wjdbc.util.Validate;

/**
 * The Class WPreparedStatement.
 */
public class WPreparedStatement extends WStatement implements PreparedStatement {

	private static final PreparedStatementParameter[]	EMPTYPARAMS	= new PreparedStatementParameter[0];

	private final List<PreparedStatementParameter[]>	batchCollector;

	private PreparedStatementParameter[]					paramList	= new PreparedStatementParameter[10];
	private int														maxIndex		= 0;

	/**
	 * Instantiates a new w prepared statement.
	 *
	 * @param uid
	 *           the uid
	 * @param connection
	 *           the connection
	 * @param sink
	 *           the sink
	 * @param resultSetType
	 *           the result set type
	 * @param maxRows
	 *           the max rows
	 * @param queryTimeout
	 *           the query timeout
	 */
	WPreparedStatement(final long uid, final Connection connection, final DecoratedCommandSink sink, final int resultSetType, final int maxRows,
			final int queryTimeout) {
		super(uid, connection, sink, resultSetType, maxRows, queryTimeout);
		batchCollector = new ArrayList<PreparedStatementParameter[]>();
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		StreamingResultSet result = null;
		try {
			reduceParam();
			result = (StreamingResultSet) sink.process(objectUid, new PreparedStatementQueryCommand(paramList, resultSetType));
			result.setStatement(this);
			result.setCommandSink(sink);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
		return result;
	}

	@Override
	public int executeUpdate() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		reduceParam();
		return sink.processWithIntResult(objectUid, new PreparedStatementUpdateCommand(paramList));
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
		setParam(parameterIndex, new NullParameter(sqlType, null));
	}

	@Override
	public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
		setParam(parameterIndex, new BooleanParameter(x));
	}

	@Override
	public void setByte(final int parameterIndex, final byte x) throws SQLException {
		setParam(parameterIndex, new ByteParameter(x));
	}

	@Override
	public void setShort(final int parameterIndex, final short x) throws SQLException {
		setParam(parameterIndex, new ShortParameter(x));
	}

	@Override
	public void setInt(final int parameterIndex, final int x) throws SQLException {
		setParam(parameterIndex, new IntegerParameter(x));
	}

	@Override
	public void setLong(final int parameterIndex, final long x) throws SQLException {
		setParam(parameterIndex, new LongParameter(x));
	}

	@Override
	public void setFloat(final int parameterIndex, final float x) throws SQLException {
		setParam(parameterIndex, new FloatParameter(x));
	}

	@Override
	public void setDouble(final int parameterIndex, final double x) throws SQLException {
		setParam(parameterIndex, new DoubleParameter(x));
	}

	@Override
	public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
		setParam(parameterIndex, new BigDecimalParameter(x));
	}

	@Override
	public void setString(final int parameterIndex, final String x) throws SQLException {
		setParam(parameterIndex, new StringParameter(x));
	}

	@Override
	public void setBytes(final int parameterIndex, final byte x[]) throws SQLException {
		setParam(parameterIndex, new ByteArrayParameter(x));
	}

	@Override
	public void setDate(final int parameterIndex, final Date x) throws SQLException {
		setParam(parameterIndex, new DateParameter(x, null));
	}

	@Override
	public void setTime(final int parameterIndex, final Time x) throws SQLException {
		setParam(parameterIndex, new TimeParameter(x, null));
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
		setParam(parameterIndex, new TimestampParameter(x, null));
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		setParam(parameterIndex, new ByteStreamParameter(ByteStreamParameter.TYPE_ASCII, x, length));
	}

	@Override
	public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		setParam(parameterIndex, new ByteStreamParameter(ByteStreamParameter.TYPE_UNICODE, x, length));
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		setParam(parameterIndex, new ByteStreamParameter(ByteStreamParameter.TYPE_BINARY, x, length));
	}

	@Override
	public void clearParameters() throws SQLException {
		for (int i = 0; i < paramList.length; ++i) {
			paramList[i] = null;
		}
	}

	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scale) throws SQLException {
		setParam(parameterIndex, new ObjectParameter(x, Integer.valueOf(targetSqlType), Integer.valueOf(scale)));
	}

	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
		setParam(parameterIndex, new ObjectParameter(x, Integer.valueOf(targetSqlType), null));
	}

	@Override
	public void setObject(final int parameterIndex, final Object x) throws SQLException {
		setParam(parameterIndex, new ObjectParameter(x, null, null));
	}

	@Override
	public boolean execute() throws SQLException {
		reduceParam();
		return sink.processWithBooleanResult(objectUid, new PreparedStatementExecuteCommand(paramList));
	}

	@Override
	public void addBatch() throws SQLException {
		reduceParam();
		final PreparedStatementParameter[] paramListClone = new PreparedStatementParameter[paramList.length];
		System.arraycopy(paramList, 0, paramListClone, 0, paramList.length);
		batchCollector.add(paramListClone);
		clearParameters();
	}

	@Override
	public int[] executeBatch() throws SQLException {
		try {
			return sink.process(objectUid, new PreparedStatementExecuteBatchCommand(batchCollector));
		} finally {
			batchCollector.clear();
		}
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
		setParam(parameterIndex, new CharStreamParameter(reader, length));
	}

	@Override
	public void setRef(final int i, final Ref x) throws SQLException {
		setParam(i, new RefParameter(x));
	}

	@Override
	public void setBlob(final int i, final Blob x) throws SQLException {
		setParam(i, new BlobParameter(x));
	}

	@Override
	public void setClob(final int i, final Clob x) throws SQLException {
		setParam(i, new ClobParameter(x));
	}

	@Override
	public void setArray(final int i, final Array x) throws SQLException {
		setParam(i, new ArrayParameter(x));
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		try {
			return (SerialResultSetMetaData) sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.PREPAREDSTATEMENT, "getMetaData"));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
		setParam(parameterIndex, new DateParameter(x, cal));
	}

	@Override
	public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
		setParam(parameterIndex, new TimeParameter(x, cal));
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
		setParam(parameterIndex, new TimestampParameter(x, cal));
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
		setParam(parameterIndex, new NullParameter(sqlType, typeName));
	}

	@Override
	public void setURL(final int parameterIndex, final URL x) throws SQLException {
		setParam(parameterIndex, new URLParameter(x));
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		throw new UnsupportedOperationException("getParameterMetaData");
	}

	/**
	 * Sets the param.
	 *
	 * @param index
	 *           the index
	 * @param parm
	 *           the parm
	 * @throws SQLException
	 *            the SQL exception
	 */
	protected void setParam(final int index, final PreparedStatementParameter parm) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		if (paramList.length < index) {
			final List<PreparedStatementParameter> tmp = Arrays.asList(paramList);
			final PreparedStatementParameter[] newArray = new PreparedStatementParameter[index * 2];
			paramList = tmp.toArray(newArray);
		}
		if (maxIndex < index) {
			maxIndex = index;
		}
		paramList[index - 1] = parm;
	}

	/**
	 * Reduce param.
	 */
	protected void reduceParam() {
		if (maxIndex > 0) {
			final PreparedStatementParameter[] tmpArray = new PreparedStatementParameter[maxIndex];
			System.arraycopy(paramList, 0, tmpArray, 0, maxIndex);
			paramList = tmpArray;
		} else {
			paramList = EMPTYPARAMS;
		}
	}

	/* start JDBC4 support */
	@Override
	public void setRowId(final int parameterIndex, final RowId rowId) throws SQLException {
		setParam(parameterIndex, new RowIdParameter(rowId));
	}

	@Override
	public void setNString(final int parameterIndex, final String value) throws SQLException {
		setParam(parameterIndex, new StringParameter(value));
	}

	/**
	 * Sets the n character stream.
	 *
	 * @param parameterIndex
	 *           the parameter index
	 * @param reader
	 *           the reader
	 * @param length
	 *           the length
	 * @throws SQLException
	 *            the SQL exception
	 */
	public void setNCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
		setParam(parameterIndex, new CharStreamParameter(reader, length));
	}

	@Override
	public void setNClob(final int i, final NClob x) throws SQLException {
		setParam(i, new ClobParameter(x));
	}

	@Override
	public void setClob(final int i, final Reader reader, final long length) throws SQLException {
		setParam(i, new ClobParameter(new SerialClob(reader, length)));
	}

	@Override
	public void setBlob(final int i, final InputStream inputStream, final long length) throws SQLException {
		setParam(i, new BlobParameter(new SerialBlob(inputStream, length)));
	}

	@Override
	public void setNClob(final int i, final Reader reader, final long length) throws SQLException {
		setParam(i, new ClobParameter(new SerialNClob(reader, length)));
	}

	@Override
	public void setSQLXML(final int i, final SQLXML xmlObject) throws SQLException {
		setParam(i, new SQLXMLParameter(new SerialSQLXML(xmlObject)));
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
		setAsciiStream(parameterIndex, x, -1);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		setParam(parameterIndex, new ByteStreamParameter(ByteStreamParameter.TYPE_ASCII, x, length));
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
		setBinaryStream(parameterIndex, x, -1);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		setParam(parameterIndex, new ByteStreamParameter(ByteStreamParameter.TYPE_BINARY, x, length));
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
		setParam(parameterIndex, new CharStreamParameter(reader));
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		setParam(parameterIndex, new CharStreamParameter(reader, length));
	}

	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
		setParam(parameterIndex, new CharStreamParameter(reader));
	}

	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		setParam(parameterIndex, new CharStreamParameter(reader, length));
	}

	@Override
	public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
		setParam(parameterIndex, new ClobParameter(new SerialClob(reader)));
	}

	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
		setParam(parameterIndex, new BlobParameter(new SerialBlob(inputStream)));
	}

	@Override
	public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
		setParam(parameterIndex, new ClobParameter(new SerialNClob(reader)));
	}
}
