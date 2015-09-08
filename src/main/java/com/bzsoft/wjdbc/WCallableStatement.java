package com.bzsoft.wjdbc;

import java.io.CharArrayReader;
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
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import com.bzsoft.wjdbc.command.CallableStatementGetArrayCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetBlobCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetCharacterStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetClobCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetNCharacterStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetNClobCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetObjectCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetRefCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetSQLXMLCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetAsciiStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetBinaryStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetBlobCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetCharacterStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetClobCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetNCharacterStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetNClobCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetObjectCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetRowIdCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetSQLXMLCommand;
import com.bzsoft.wjdbc.command.DecoratedCommandSink;
import com.bzsoft.wjdbc.command.JdbcInterfaceType;
import com.bzsoft.wjdbc.command.ParameterTypeCombinations;
import com.bzsoft.wjdbc.command.ReflectiveCommand;
import com.bzsoft.wjdbc.serial.SerialBlob;
import com.bzsoft.wjdbc.serial.SerialClob;
import com.bzsoft.wjdbc.serial.SerialNClob;
import com.bzsoft.wjdbc.serial.SerialRowId;
import com.bzsoft.wjdbc.serial.SerialSQLXML;
import com.bzsoft.wjdbc.serial.StreamingResultSet;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;
import com.bzsoft.wjdbc.util.Validate;

/**
 * The Class WCallableStatement.
 */
public class WCallableStatement extends WPreparedStatement implements CallableStatement {

	/**
	 * Instantiates a new w callable statement.
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
	WCallableStatement(final long uid, final Connection connection, final DecoratedCommandSink sink, final int resultSetType, final int maxRows,
			final int queryTimeout) {
		super(uid, connection, sink, resultSetType, maxRows, queryTimeout);
	}

	@Override
	protected void finalize() throws Throwable {
		close();
	}

	@Override
	public void registerOutParameter(final int parameterIndex, final int sqlType) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(
				objectUid,
				ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "registerOutParameter",
						new Object[] { Integer.valueOf(parameterIndex), Integer.valueOf(sqlType) }, ParameterTypeCombinations.INTINT));
	}

	@Override
	public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "registerOutParameter",
				new Object[] { Integer.valueOf(parameterIndex), Integer.valueOf(sqlType), Integer.valueOf(scale) }, ParameterTypeCombinations.INTINTINT));
	}

	@Override
	public boolean wasNull() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "wasNull"));
	}

	@Override
	public String getString(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getString",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean getBoolean(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getBoolean",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public byte getByte(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithByteResult(objectUid, ReflectiveCommand.<Byte, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getByte",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public short getShort(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithShortResult(objectUid, ReflectiveCommand.<Short, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getShort",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public int getInt(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getInt",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public long getLong(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithLongResult(objectUid, ReflectiveCommand.<Long, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getLong",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public float getFloat(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithFloatResult(objectUid, ReflectiveCommand.<Float, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getFloat",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public double getDouble(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithDoubleResult(objectUid, ReflectiveCommand.<Double, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getDouble",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(
				objectUid,
				ReflectiveCommand.<BigDecimal, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getBigDecimal",
						new Object[] { Integer.valueOf(parameterIndex), Integer.valueOf(scale) }, ParameterTypeCombinations.INTINT));
	}

	@Override
	public byte[] getBytes(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<byte[], Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getBytes",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public Date getDate(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<Date, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getDate",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public Time getTime(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<Time, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getTime",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(
				objectUid,
				ReflectiveCommand.<Timestamp, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getTimestamp",
						new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public Object getObject(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final Object transportee = sink.process(objectUid, new CallableStatementGetObjectCommand(parameterIndex));
			checkTransporteeForStreamingResultSet(transportee);
			return transportee;
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(
				objectUid,
				ReflectiveCommand.<BigDecimal, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getBigDecimal",
						new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public Object getObject(final int i, final Map<String, Class<?>> map) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final Object transportee = sink.process(objectUid, new CallableStatementGetObjectCommand(i, map));
			checkTransporteeForStreamingResultSet(transportee);
			return transportee;
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}

	}

	@Override
	public Ref getRef(final int i) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			return sink.process(objectUid, new CallableStatementGetRefCommand(i));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}

	}

	@Override
	public Blob getBlob(final int i) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			return sink.process(objectUid, new CallableStatementGetBlobCommand(i));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public Clob getClob(final int i) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			return sink.process(objectUid, new CallableStatementGetClobCommand(i));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public Array getArray(final int i) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			return sink.process(objectUid, new CallableStatementGetArrayCommand(i));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public Date getDate(final int parameterIndex, final Calendar cal) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<Date, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getDate",
				new Object[] { Integer.valueOf(parameterIndex), cal }, ParameterTypeCombinations.INTCAL));
	}

	@Override
	public Time getTime(final int parameterIndex, final Calendar cal) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<Time, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getTime",
				new Object[] { Integer.valueOf(parameterIndex), cal }, ParameterTypeCombinations.INTCAL));
	}

	@Override
	public Timestamp getTimestamp(final int parameterIndex, final Calendar cal) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(
				objectUid,
				ReflectiveCommand.<Timestamp, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getTimestamp",
						new Object[] { Integer.valueOf(parameterIndex), cal }, ParameterTypeCombinations.INTCAL));
	}

	@Override
	public void registerOutParameter(final int paramIndex, final int sqlType, final String typeName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(
				objectUid,
				ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "registerOutParameter",
						new Object[] { Integer.valueOf(paramIndex), Integer.valueOf(sqlType), typeName }, ParameterTypeCombinations.INTINTSTR));
	}

	@Override
	public void registerOutParameter(final String parameterName, final int sqlType) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "registerOutParameter",
				new Object[] { parameterName, Integer.valueOf(sqlType) }, ParameterTypeCombinations.STRINT));
	}

	@Override
	public void registerOutParameter(final String parameterName, final int sqlType, final int scale) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(
				objectUid,
				ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "registerOutParameter", new Object[] { parameterName, Integer.valueOf(sqlType),
						Integer.valueOf(scale) }, ParameterTypeCombinations.STRINTINT));
	}

	@Override
	public void registerOutParameter(final String parameterName, final int sqlType, final String typeName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(
				objectUid,
				ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "registerOutParameter", new Object[] { parameterName, Integer.valueOf(sqlType),
						typeName }, ParameterTypeCombinations.STRINTSTR));
	}

	@Override
	public URL getURL(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<URL, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getURL",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public void setURL(final String parameterName, final URL val) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setURL", new Object[] { parameterName, val },
				ParameterTypeCombinations.STRURL));
	}

	@Override
	public void setNull(final String parameterName, final int sqlType) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setNull",
				new Object[] { parameterName, Integer.valueOf(sqlType) }, ParameterTypeCombinations.STRINT));
	}

	@Override
	public void setBoolean(final String parameterName, final boolean x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setBoolean", new Object[] { parameterName,
				x ? Boolean.TRUE : Boolean.FALSE }, ParameterTypeCombinations.STRBOL));
	}

	@Override
	public void setByte(final String parameterName, final byte x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setByte", new Object[] { parameterName, Byte.valueOf(x) },
				ParameterTypeCombinations.STRBYT));
	}

	@Override
	public void setShort(final String parameterName, final short x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setShort", new Object[] { parameterName, Short.valueOf(x) },
				ParameterTypeCombinations.STRSHT));
	}

	@Override
	public void setInt(final String parameterName, final int x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setInt", new Object[] { parameterName, Integer.valueOf(x) },
				ParameterTypeCombinations.STRINT));
	}

	@Override
	public void setLong(final String parameterName, final long x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setLong", new Object[] { parameterName, Long.valueOf(x) },
				ParameterTypeCombinations.STRLNG));
	}

	@Override
	public void setFloat(final String parameterName, final float x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setFloat", new Object[] { parameterName, Float.valueOf(x) },
				ParameterTypeCombinations.STRFLT));
	}

	@Override
	public void setDouble(final String parameterName, final double x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setDouble",
				new Object[] { parameterName, Double.valueOf(x) }, ParameterTypeCombinations.STRDBL));
	}

	@Override
	public void setBigDecimal(final String parameterName, final BigDecimal x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setBigDecimal", new Object[] { parameterName, x },
				ParameterTypeCombinations.STRBID));
	}

	@Override
	public void setString(final String parameterName, final String x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setString", new Object[] { parameterName, x },
				ParameterTypeCombinations.STRSTR));
	}

	@Override
	public void setBytes(final String parameterName, final byte x[]) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setBytes", new Object[] { parameterName, x },
				ParameterTypeCombinations.STRBYTA));
	}

	@Override
	public void setDate(final String parameterName, final Date x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid,
				ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setDate", new Object[] { parameterName, x }, ParameterTypeCombinations.STRDAT));
	}

	@Override
	public void setTime(final String parameterName, final Time x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid,
				ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setTime", new Object[] { parameterName, x }, ParameterTypeCombinations.STRTIM));
	}

	@Override
	public void setTimestamp(final String parameterName, final Timestamp x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setTimestamp", new Object[] { parameterName, x },
				ParameterTypeCombinations.STRTMS));
	}

	@Override
	public void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			sink.process(objectUid, new CallableStatementSetAsciiStreamCommand(parameterName, x, length));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			sink.process(objectUid, new CallableStatementSetBinaryStreamCommand(parameterName, x, length));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setObject(final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		final CallableStatementSetObjectCommand cmd = new CallableStatementSetObjectCommand(parameterName, Integer.valueOf(targetSqlType),
				Integer.valueOf(scale));
		cmd.setObject(x);
		sink.process(objectUid, cmd);
	}

	@Override
	public void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		final CallableStatementSetObjectCommand cmd = new CallableStatementSetObjectCommand(parameterName, Integer.valueOf(targetSqlType), null);
		cmd.setObject(x);
		sink.process(objectUid, cmd);
	}

	@Override
	public void setObject(final String parameterName, final Object x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		final CallableStatementSetObjectCommand cmd = new CallableStatementSetObjectCommand(parameterName, null, null);
		cmd.setObject(x);
		sink.process(objectUid, cmd);
	}

	@Override
	public void setCharacterStream(final String parameterName, final Reader reader, final int length) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetCharacterStreamCommand cmd = new CallableStatementSetCharacterStreamCommand(parameterName, reader, length);
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setDate", new Object[] { parameterName, x, cal },
				ParameterTypeCombinations.STRDATCAL));
	}

	@Override
	public void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setTime", new Object[] { parameterName, x, cal },
				ParameterTypeCombinations.STRTIMCAL));
	}

	@Override
	public void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setTimestamp", new Object[] { parameterName, x, cal },
				ParameterTypeCombinations.STRTMSCAL));
	}

	@Override
	public void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setNull",
				new Object[] { parameterName, Integer.valueOf(sqlType), typeName }, ParameterTypeCombinations.STRINTSTR));
	}

	@Override
	public String getString(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return (String) sink.process(objectUid,
				ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "getString", new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public boolean getBoolean(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getBoolean",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public byte getByte(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithByteResult(objectUid, ReflectiveCommand.<Byte, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getByte",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public short getShort(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithShortResult(objectUid, ReflectiveCommand.<Short, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getShort",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public int getInt(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getInt",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public long getLong(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithLongResult(objectUid, ReflectiveCommand.<Long, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getLong",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public float getFloat(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithFloatResult(objectUid, ReflectiveCommand.<Float, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getFloat",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public double getDouble(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithDoubleResult(objectUid, ReflectiveCommand.<Double, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getDouble",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public byte[] getBytes(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<byte[], Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getBytes",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public Date getDate(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<Date, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getDate",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public Time getTime(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<Time, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getTime",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public Timestamp getTimestamp(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<Timestamp, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getTimestamp",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public Object getObject(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetObjectCommand cmd = new CallableStatementGetObjectCommand(parameterName);
			final Object transportee = sink.process(objectUid, cmd);
			checkTransporteeForStreamingResultSet(transportee);
			return transportee;
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<BigDecimal, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getBigDecimal",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public Object getObject(final String parameterName, final Map<String, Class<?>> map) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetObjectCommand cmd = new CallableStatementGetObjectCommand(parameterName, map);
			final Object transportee = sink.process(objectUid, cmd);
			checkTransporteeForStreamingResultSet(transportee);
			return transportee;
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public Ref getRef(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetRefCommand cmd = new CallableStatementGetRefCommand(parameterName);
			return sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public Blob getBlob(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetBlobCommand cmd = new CallableStatementGetBlobCommand(parameterName);
			return sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public Clob getClob(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetClobCommand cmd = new CallableStatementGetClobCommand(parameterName);
			return sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public Array getArray(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetArrayCommand cmd = new CallableStatementGetArrayCommand(parameterName);
			return sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public Date getDate(final String parameterName, final Calendar cal) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<Date, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getDate", new Object[] {
				parameterName, cal }, ParameterTypeCombinations.STRCAL));
	}

	@Override
	public Time getTime(final String parameterName, final Calendar cal) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<Time, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getTime", new Object[] {
				parameterName, cal }, ParameterTypeCombinations.STRCAL));
	}

	@Override
	public Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<Timestamp, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getTimestamp", new Object[] {
				parameterName, cal }, ParameterTypeCombinations.STRCAL));
	}

	@Override
	public URL getURL(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<URL, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getURL",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	private void checkTransporteeForStreamingResultSet(final Object transportee) {
		// The transportee might be a StreamingResultSet (i.e. Oracle can return
		// database cursors). Thus
		// we must check the transportee and set some references correspondingly
		// when it is a ResultSet.
		if (transportee instanceof StreamingResultSet) {
			final StreamingResultSet srs = (StreamingResultSet) transportee;
			srs.setStatement(this);
			srs.setCommandSink(sink);
		}
	}

	/* start JDBC4 support */
	@Override
	public RowId getRowId(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<RowId, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getRowId",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public RowId getRowId(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<RowId, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getRowId",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public void setRowId(final String parameterName, final RowId x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetRowIdCommand cmd = new CallableStatementSetRowIdCommand(parameterName, new SerialRowId(x));
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setNString(final String parameterName, final String x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CALLABLESTATEMENT, "setNString", new Object[] { parameterName, x },
				ParameterTypeCombinations.STRSTR));
	}

	@Override
	public void setNCharacterStream(final String parameterName, final Reader reader, final long length) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetNCharacterStreamCommand cmd = new CallableStatementSetNCharacterStreamCommand(parameterName, reader, (int) length);
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setNClob(final String parameterName, final NClob value) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetNClobCommand cmd = new CallableStatementSetNClobCommand(parameterName, value);
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetClobCommand cmd = new CallableStatementSetClobCommand(parameterName, new SerialClob(reader, length));
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetBlobCommand cmd = new CallableStatementSetBlobCommand(parameterName, new SerialBlob(inputStream, length));
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetNClobCommand cmd = new CallableStatementSetNClobCommand(parameterName, new SerialNClob(reader, length));
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public NClob getNClob(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetNClobCommand cmd = new CallableStatementGetNClobCommand(parameterIndex);
			return sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public NClob getNClob(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetNClobCommand cmd = new CallableStatementGetNClobCommand(parameterName);
			return sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetSQLXMLCommand cmd = new CallableStatementSetSQLXMLCommand(parameterName, new SerialSQLXML(xmlObject));
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetSQLXMLCommand cmd = new CallableStatementGetSQLXMLCommand(parameterIndex);
			return sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public SQLXML getSQLXML(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetSQLXMLCommand cmd = new CallableStatementGetSQLXMLCommand(parameterName);
			return sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public String getNString(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getNString",
				new Object[] { parameterIndex }, ParameterTypeCombinations.INT));
	}

	@Override
	public String getNString(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.CALLABLESTATEMENT, "getNString",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@Override
	public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetNCharacterStreamCommand cmd = new CallableStatementGetNCharacterStreamCommand(parameterIndex);
			return new CharArrayReader(sink.process(objectUid, cmd));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public Reader getNCharacterStream(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetNCharacterStreamCommand cmd = new CallableStatementGetNCharacterStreamCommand(parameterName);
			return new CharArrayReader(sink.process(objectUid, cmd));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public Reader getCharacterStream(final int parameterIndex) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetCharacterStreamCommand cmd = new CallableStatementGetCharacterStreamCommand(parameterIndex);
			return new CharArrayReader(sink.process(objectUid, cmd));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public Reader getCharacterStream(final String parameterName) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetCharacterStreamCommand cmd = new CallableStatementGetCharacterStreamCommand(parameterName);
			return new CharArrayReader(sink.process(objectUid, cmd));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setClob(final String parameterName, final Clob clob) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetClobCommand cmd = new CallableStatementSetClobCommand(parameterName, clob);
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setBlob(final String parameterName, final Blob blob) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetBlobCommand cmd = new CallableStatementSetBlobCommand(parameterName, blob);
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setAsciiStream(final String parameterName, final InputStream x, final long length) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			sink.process(objectUid, new CallableStatementSetAsciiStreamCommand(parameterName, x, (int) length));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setBinaryStream(final String parameterName, final InputStream x, final long length) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			sink.process(objectUid, new CallableStatementSetBinaryStreamCommand(parameterName, x, (int) length));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setCharacterStream(final String parameterName, final Reader reader, final long length) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetCharacterStreamCommand cmd = new CallableStatementSetCharacterStreamCommand(parameterName, reader, (int) length);
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setAsciiStream(final String parameterName, final InputStream x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			sink.process(objectUid, new CallableStatementSetAsciiStreamCommand(parameterName, x));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setBinaryStream(final String parameterName, final InputStream x) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			sink.process(objectUid, new CallableStatementSetBinaryStreamCommand(parameterName, x));
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setCharacterStream(final String parameterName, final Reader reader) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetCharacterStreamCommand cmd = new CallableStatementSetCharacterStreamCommand(parameterName, reader);
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setNCharacterStream(final String parameterName, final Reader reader) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetNCharacterStreamCommand cmd = new CallableStatementSetNCharacterStreamCommand(parameterName, reader);
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setClob(final String parameterName, final Reader reader) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetClobCommand cmd = new CallableStatementSetClobCommand(parameterName, new SerialClob(reader));
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetBlobCommand cmd = new CallableStatementSetBlobCommand(parameterName, new SerialBlob(inputStream));
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void setNClob(final String parameterName, final Reader reader) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementSetNClobCommand cmd = new CallableStatementSetNClobCommand(parameterName, new SerialNClob(reader));
			sink.process(objectUid, cmd);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getObject(final int parameterIndex, final Class<T> clazz) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final Object transportee = sink.process(objectUid, new CallableStatementGetObjectCommand(parameterIndex, clazz));
			checkTransporteeForStreamingResultSet(transportee);
			return (T) transportee;
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getObject(final String parameterName, final Class<T> clazz) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final CallableStatementGetObjectCommand cmd = new CallableStatementGetObjectCommand(parameterName, clazz);
			final Object transportee = sink.process(objectUid, cmd);
			checkTransporteeForStreamingResultSet(transportee);
			return (T) transportee;
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}
}
