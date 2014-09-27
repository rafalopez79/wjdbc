package com.bzsoft.wjdbc.serial;

import java.io.ByteArrayInputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
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
import java.sql.Types;
import java.util.Calendar;
import java.util.Map;

import com.bzsoft.wjdbc.WStatement;
import com.bzsoft.wjdbc.command.DecoratedCommandSink;
import com.bzsoft.wjdbc.command.DestroyCommand;
import com.bzsoft.wjdbc.command.JdbcInterfaceType;
import com.bzsoft.wjdbc.command.NextRowPacketCommand;
import com.bzsoft.wjdbc.command.ParameterTypeCombinations;
import com.bzsoft.wjdbc.command.ReflectiveCommand;
import com.bzsoft.wjdbc.command.ResultSetGetMetaDataCommand;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;

public class StreamingResultSet implements ResultSet, Externalizable {

	private static final long					serialVersionUID		= 8291019975153433161L;

	private int[]									columnTypes;
	private String[]								columnNames;
	private String[]								columnLabels;
	private RowPacket								rows;
	private int										rowPacketSize;
	private boolean								forwardOnly;
	private String									charset;
	private boolean								lastPartReached		= true;
	private Long									remainingResultSet	= null;
	private ResultSetMetaData					metaData					= null;

	private transient DecoratedCommandSink	commandSink				= null;
	private transient int						cursor					= -1;
	private transient int						lastReadColumn			= 0;
	private transient Object[]					actualRow;
	private transient int						fetchDirection;
	private transient boolean					prefetchMetaData;
	private transient Statement				statement;

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (remainingResultSet != null) {
			close();
		}
	}

	public StreamingResultSet() {
		// Empty
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(columnTypes);
		out.writeObject(columnNames);
		out.writeObject(columnLabels);
		out.writeObject(rows);
		out.writeInt(rowPacketSize);
		out.writeBoolean(forwardOnly);
		out.writeUTF(charset);
		out.writeBoolean(lastPartReached);
		out.writeObject(remainingResultSet);
		out.writeObject(metaData);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		columnTypes = (int[]) in.readObject();
		columnNames = (String[]) in.readObject();
		columnLabels = (String[]) in.readObject();
		rows = (RowPacket) in.readObject();
		rowPacketSize = in.readInt();
		forwardOnly = in.readBoolean();
		charset = in.readUTF();
		lastPartReached = in.readBoolean();
		remainingResultSet = (Long) in.readObject();
		metaData = (ResultSetMetaData) in.readObject();
		cursor = -1;
	}

	public StreamingResultSet(final int rowPacketSize, final boolean forwardOnly, final boolean prefetchMetaData, final String charset) {
		this.rowPacketSize = rowPacketSize;
		this.forwardOnly = forwardOnly;
		this.prefetchMetaData = prefetchMetaData;
		this.charset = charset;
	}

	public void setStatement(final Statement stmt) {
		statement = stmt;
	}

	public void setCommandSink(final DecoratedCommandSink sink) {
		commandSink = sink;
	}

	public void setRemainingResultSetUID(final long reg) {
		remainingResultSet = reg;
	}

	public boolean populate(final ResultSet rs) throws SQLException {
		final ResultSetMetaData md = rs.getMetaData();
		// Fetch the meta data immediately if required. Succeeding getMetaData()
		// calls on the ResultSet won't require an additional remote call
		if (prefetchMetaData) {
			this.metaData = new SerialResultSetMetaData(md);
		}
		final int columnCount = md.getColumnCount();
		columnTypes = new int[columnCount];
		columnNames = new String[columnCount];
		columnLabels = new String[columnCount];

		for (int i = 1; i <= columnCount; i++) {
			columnTypes[i - 1] = md.getColumnType(i);
			columnNames[i - 1] = md.getColumnName(i).toLowerCase();
			columnLabels[i - 1] = md.getColumnLabel(i).toLowerCase();
		}

		// Create first ResultSet-Part
		rows = new RowPacket(rowPacketSize, forwardOnly);
		// Populate it
		rows.populate(rs);

		lastPartReached = rows.isLastPart();

		return lastPartReached;
	}

	@Override
	public boolean next() throws SQLException {
		boolean result = false;
		if (++cursor < rows.size()) {
			actualRow = rows.get(cursor);
			result = true;
		} else {
			if (!lastPartReached) {
				try {
					final RowPacket rsp = commandSink.process(remainingResultSet, new NextRowPacketCommand());
					if (rsp.isLastPart()) {
						lastPartReached = true;
					}
					if (rsp.size() > 0) {
						rows.merge(rsp);
						actualRow = rows.get(cursor);
						result = true;
					}
				} catch (final Exception e) {
					throw SQLExceptionHelper.wrap(e);
				}
			}
		}

		return result;
	}

	@Override
	public void close() throws SQLException {
		cursor = -1;
		if (remainingResultSet != null) {
			// The server-side created StreamingResultSet is garbage-collected
			// after it was send over the wire. Thus
			// we have to check here if it is such a server object because in this
			// case we don't have to try the remote
			// call which indeed causes a NPE.
			if (commandSink != null) {
				final long uid = remainingResultSet.longValue();
				commandSink.process(remainingResultSet, new DestroyCommand(uid, JdbcInterfaceType.RESULTSETHOLDER));
			}
			remainingResultSet = null;
		}
		if (statement != null && ((WStatement) statement).isCloseOnCompletion()) {
			statement.close();
		}
	}

	@Override
	public boolean wasNull() throws SQLException {
		return actualRow[lastReadColumn] == null;
	}

	@Override
	public String getString(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return actualRow[ci].toString();
		}
		return null;
	}

	@Override
	public boolean getBoolean(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			final Object value = actualRow[ci];
			switch (columnTypes[ci]) {
			case Types.BIT:
			case Types.BOOLEAN:
				return ((Boolean) value).booleanValue();
			case Types.TINYINT:
				return ((Byte) value).byteValue() != 0;
			case Types.SMALLINT:
				return ((Short) value).shortValue() != 0;
			case Types.INTEGER:
				return ((Integer) value).intValue() != 0;
			case Types.BIGINT:
				return ((Long) value).longValue() != 0;
			case Types.REAL:
				return ((Float) value).floatValue() != 0.0f;
			case Types.FLOAT:
			case Types.DOUBLE:
				return ((Double) value).doubleValue() != 0.0f;
			case Types.NUMERIC:
			case Types.DECIMAL:
				return ((BigDecimal) value).intValue() != 0;
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				try {
					return Integer.parseInt((String) value) != 0;
				} catch (final NumberFormatException e) {
					throw new SQLException("Can't convert String value '" + value + "' to boolean, must be an integer");
				}
			default:
				break;
			}
			throw new SQLException("Can't convert type to boolean: " + value.getClass());
		}
		return false;
	}

	@Override
	public byte getByte(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			final Object value = actualRow[ci];
			switch (columnTypes[ci]) {
			case Types.BIT:
			case Types.BOOLEAN:
				return ((Boolean) value).booleanValue() ? (byte) 1 : (byte) 0;
			case Types.TINYINT:
				return ((Byte) value).byteValue();
			case Types.SMALLINT:
				return ((Short) value).byteValue();
			case Types.INTEGER:
				return ((Integer) value).byteValue();
			case Types.BIGINT:
				return ((Long) value).byteValue();
			case Types.REAL:
				return ((Float) value).byteValue();
			case Types.FLOAT:
			case Types.DOUBLE:
				return ((Double) value).byteValue();
			case Types.NUMERIC:
			case Types.DECIMAL:
				return ((BigDecimal) value).byteValue();
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				try {
					return Byte.parseByte((String) value);
				} catch (final NumberFormatException e) {
					throw new SQLException("Can't convert String value '" + value + "' to byte");
				}
			default:
				break;
			}
			throw new SQLException("Can't convert type to byte: " + value.getClass());
		}
		return 0;
	}

	@Override
	public short getShort(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			final Object value = actualRow[ci];
			switch (columnTypes[ci]) {
			case Types.BIT:
			case Types.BOOLEAN:
				return ((Boolean) value).booleanValue() ? (short) 1 : (short) 0;
			case Types.TINYINT:
				return ((Byte) value).shortValue();
			case Types.SMALLINT:
				return ((Short) value).shortValue();
			case Types.INTEGER:
				return ((Integer) value).shortValue();
			case Types.BIGINT:
				return ((Long) value).shortValue();
			case Types.REAL:
				return ((Float) value).shortValue();
			case Types.FLOAT:
			case Types.DOUBLE:
				return ((Double) value).shortValue();
			case Types.NUMERIC:
			case Types.DECIMAL:
				return ((BigDecimal) value).shortValue();
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				try {
					return Short.parseShort((String) value);
				} catch (final NumberFormatException e) {
					throw new SQLException("Can't convert String value '" + value + "' to short");
				}
			default:
				break;
			}
			throw new SQLException("Can't convert type to short: " + value.getClass());
		}
		return 0;
	}

	@Override
	public int getInt(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			final Object value = actualRow[ci];
			switch (columnTypes[ci]) {
			case Types.BIT:
			case Types.BOOLEAN:
				return ((Boolean) value).booleanValue() ? (int) 1 : (int) 0;
			case Types.TINYINT:
				return ((Byte) value).intValue();
			case Types.SMALLINT:
				return ((Short) value).intValue();
			case Types.INTEGER:
				return ((Integer) value).intValue();
			case Types.BIGINT:
				return ((Long) value).intValue();
			case Types.REAL:
				return ((Float) value).intValue();
			case Types.FLOAT:
			case Types.DOUBLE:
				return ((Double) value).intValue();
			case Types.NUMERIC:
			case Types.DECIMAL:
				return ((BigDecimal) value).intValue();
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				try {
					return Integer.parseInt((String) value);
				} catch (final NumberFormatException e) {
					throw new SQLException("Can't convert String value '" + value + "' to integer");
				}
			default:
				break;
			}
			throw new SQLException("Can't convert type to integer: " + value.getClass());
		}
		return 0;
	}

	@Override
	public long getLong(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			final Object value = actualRow[ci];
			switch (columnTypes[ci]) {
			case Types.BIT:
			case Types.BOOLEAN:
				return ((Boolean) value).booleanValue() ? 1 : 0;
			case Types.TINYINT:
				return ((Byte) value).longValue();
			case Types.SMALLINT:
				return ((Short) value).longValue();
			case Types.INTEGER:
				return ((Integer) value).longValue();
			case Types.BIGINT:
				return ((Long) value).longValue();
			case Types.REAL:
				return ((Float) value).longValue();
			case Types.FLOAT:
			case Types.DOUBLE:
				return ((Double) value).longValue();
			case Types.NUMERIC:
			case Types.DECIMAL:
				return ((BigDecimal) value).longValue();
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				try {
					return Long.parseLong((String) value);
				} catch (final NumberFormatException e) {
					throw new SQLException("Can't convert String value '" + value + "' to long");
				}
			default:
				break;
			}
			throw new SQLException("Can't convert type to long: " + value.getClass());
		}
		return 0;
	}

	@Override
	public float getFloat(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			final Object value = actualRow[ci];
			switch (columnTypes[ci]) {
			case Types.BIT:
			case Types.BOOLEAN:
				return ((Boolean) value).booleanValue() ? 1.0f : 0.0f;
			case Types.TINYINT:
				return ((Byte) value).floatValue();
			case Types.SMALLINT:
				return ((Short) value).floatValue();
			case Types.INTEGER:
				return ((Integer) value).floatValue();
			case Types.BIGINT:
				return ((Long) value).floatValue();
			case Types.REAL:
				return ((Float) value).floatValue();
			case Types.FLOAT:
			case Types.DOUBLE:
				return ((Double) value).floatValue();
			case Types.NUMERIC:
			case Types.DECIMAL:
				return ((BigDecimal) value).floatValue();
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				try {
					return Float.parseFloat((String) value);
				} catch (final NumberFormatException e) {
					throw new SQLException("Can't convert String value '" + value + "' to float");
				}
			default:
				break;
			}
			throw new SQLException("Can't convert type to float: " + value.getClass());
		}
		return 0.0f;
	}

	@Override
	public double getDouble(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			final Object value = actualRow[ci];
			switch (columnTypes[ci]) {
			case Types.BIT:
			case Types.BOOLEAN:
				return ((Boolean) value).booleanValue() ? 1.0 : 0.0;
			case Types.TINYINT:
				return ((Byte) value).doubleValue();
			case Types.SMALLINT:
				return ((Short) value).doubleValue();
			case Types.INTEGER:
				return ((Integer) value).doubleValue();
			case Types.BIGINT:
				return ((Long) value).doubleValue();
			case Types.REAL:
				return ((Float) value).doubleValue();
			case Types.FLOAT:
			case Types.DOUBLE:
				return ((Double) value).doubleValue();
			case Types.NUMERIC:
			case Types.DECIMAL:
				return ((BigDecimal) value).doubleValue();
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				try {
					return Double.parseDouble((String) value);
				} catch (final NumberFormatException e) {
					throw new SQLException("Can't convert String value '" + value + "' to double");
				}
			default:
				break;
			}
			throw new SQLException("Can't convert type to double: " + value.getClass());
		}
		return 0.0;
	}

	@Override
	public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return internalGetBigDecimal(actualRow[ci], columnTypes[ci], scale);
		}
		return null;
	}

	@Override
	public byte[] getBytes(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return (byte[]) actualRow[ci];
		}
		return null;
	}

	@Override
	public Date getDate(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			switch (columnTypes[ci]) {
			case Types.DATE:
				return (Date) actualRow[ci];
			case Types.TIME:
				return getCleanDate(((Time) actualRow[ci]).getTime());
			case Types.TIMESTAMP:
				return getCleanDate(((Timestamp) actualRow[ci]).getTime());
			}
			throw new SQLException("Can't convert type to Date: " + actualRow[ci].getClass());
		}
		return null;
	}

	@Override
	public Time getTime(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			switch (columnTypes[ci]) {
			case Types.TIME:
				return (Time) actualRow[ci];
			case Types.DATE:
				final Date date = (Date) actualRow[ci];
				return getCleanTime(date.getTime());
			case Types.TIMESTAMP:
				final Timestamp timestamp = (Timestamp) actualRow[ci];
				return getCleanTime(timestamp.getTime());
			}
			throw new SQLException("Can't convert type to Time: " + actualRow[ci].getClass());
		}
		return null;
	}

	@Override
	public Timestamp getTimestamp(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			switch (columnTypes[ci]) {
			case Types.TIME:
				return new Timestamp(((Time) actualRow[ci]).getTime());
			case Types.DATE:
				return new Timestamp(((Date) actualRow[ci]).getTime());
			case Types.TIMESTAMP:
				return (Timestamp) actualRow[ci];
			}
			throw new SQLException("Can't convert type to Timestamp: " + actualRow[ci].getClass());
		}
		return null;
	}

	@Override
	public InputStream getAsciiStream(final int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("getAsciiStream");
	}

	@Override
	public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("getUnicodeStream");
	}

	@Override
	public InputStream getBinaryStream(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			final Object obj = actualRow[ci];
			byte[] bytes;
			if (obj instanceof byte[]) {
				bytes = (byte[]) obj;
			} else if (obj instanceof String) {
				try {
					bytes = ((String) obj).getBytes(charset);
				} catch (final UnsupportedEncodingException e) {
					throw SQLExceptionHelper.wrap(e);
				}
			} else {
				final String msg = "StreamingResultSet.getBinaryStream(): Can't convert object of type '" + obj.getClass() + "' to InputStream";
				throw new SQLException(msg);
			}
			return new ByteArrayInputStream(bytes);
		}
		return null;
	}

	@Override
	public String getString(final String columnName) throws SQLException {
		return getString(getIndexForName(columnName));
	}

	@Override
	public boolean getBoolean(final String columnName) throws SQLException {
		return getBoolean(getIndexForName(columnName));
	}

	@Override
	public byte getByte(final String columnName) throws SQLException {
		return getByte(getIndexForName(columnName));
	}

	@Override
	public short getShort(final String columnName) throws SQLException {
		return getShort(getIndexForName(columnName));
	}

	@Override
	public int getInt(final String columnName) throws SQLException {
		return getInt(getIndexForName(columnName));
	}

	@Override
	public long getLong(final String columnName) throws SQLException {
		return getLong(getIndexForName(columnName));
	}

	@Override
	public float getFloat(final String columnName) throws SQLException {
		return getFloat(getIndexForName(columnName));
	}

	@Override
	public double getDouble(final String columnName) throws SQLException {
		return getDouble(getIndexForName(columnName));
	}

	@Override
	public BigDecimal getBigDecimal(final String columnName, final int scale) throws SQLException {
		return getBigDecimal(getIndexForName(columnName), scale);
	}

	@Override
	public byte[] getBytes(final String columnName) throws SQLException {
		return getBytes(getIndexForName(columnName));
	}

	@Override
	public Date getDate(final String columnName) throws SQLException {
		return getDate(getIndexForName(columnName));
	}

	@Override
	public Time getTime(final String columnName) throws SQLException {
		return getTime(getIndexForName(columnName));
	}

	@Override
	public Timestamp getTimestamp(final String columnName) throws SQLException {
		return getTimestamp(getIndexForName(columnName));
	}

	@Override
	public InputStream getAsciiStream(final String columnName) throws SQLException {
		throw new UnsupportedOperationException("getAsciiStream");
	}

	@Override
	public InputStream getUnicodeStream(final String columnName) throws SQLException {
		throw new UnsupportedOperationException("getUnicodeStream");
	}

	@Override
	public InputStream getBinaryStream(final String columnName) throws SQLException {
		return getBinaryStream(getIndexForName(columnName));
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		if (cursor < 0) {
			throw new SQLException("ResultSet already closed");
		}
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		// empty
	}

	@Override
	public String getCursorName() throws SQLException {
		throw new UnsupportedOperationException("getCursorName");
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		if (metaData == null) {
			try {
				metaData = commandSink.process(remainingResultSet, new ResultSetGetMetaDataCommand());
			} catch (final Exception e) {
				throw new SQLException("Can't get ResultSetMetaData");
			}
		}
		return metaData;
	}

	@Override
	public Object getObject(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return actualRow[ci];
		}
		return null;
	}

	@Override
	public Object getObject(final String columnName) throws SQLException {
		return getObject(getIndexForName(columnName));
	}

	@Override
	public int findColumn(final String columnName) throws SQLException {
		return getIndexForName(columnName);
	}

	@Override
	public Reader getCharacterStream(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return new StringReader((String) actualRow[ci]);
		}
		return null;
	}

	@Override
	public Reader getCharacterStream(final String columnName) throws SQLException {
		return getCharacterStream(getIndexForName(columnName));
	}

	@Override
	public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return internalGetBigDecimal(actualRow[ci], columnTypes[ci], -1);
		}
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(final String columnName) throws SQLException {
		return getBigDecimal(getIndexForName(columnName));
	}

	private static BigDecimal internalGetBigDecimal(final Object value, final int columnType, final int scale) throws SQLException {
		BigDecimal result = null;

		if (value != null) {
			switch (columnType) {
			case Types.BIT:
			case Types.BOOLEAN:
				// Boolean
				result = new BigDecimal(((Boolean) value).booleanValue() ? 1.0 : 0.0);
				break;
			case Types.TINYINT:
				// Byte
				result = new BigDecimal(((Byte) value).doubleValue());
				break;
			case Types.SMALLINT:
				// Short
				result = new BigDecimal(((Short) value).doubleValue());
				break;
			case Types.INTEGER:
				// Integer
				result = new BigDecimal(((Integer) value).doubleValue());
				break;
			case Types.BIGINT:
				// Long
				result = new BigDecimal(((Long) value).doubleValue());
				break;
			case Types.REAL:
				// Float
				result = new BigDecimal(((Float) value).doubleValue());
				break;
			case Types.FLOAT:
			case Types.DOUBLE:
				// Double
				result = new BigDecimal(((Double) value).doubleValue());
				break;
			case Types.NUMERIC:
			case Types.DECIMAL:
				// BigDecimal
				result = (BigDecimal) value;
				break;
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				// String
				try {
					result = new BigDecimal(Double.parseDouble((String) value));
				} catch (final NumberFormatException e) {
					throw new SQLException("Can't convert String value '" + value + "' to double");
				}
			default:
				break;
			}

			// Set scale if necessary
			if (result != null) {
				if (scale >= 0) {
					result = result.setScale(scale);
				}
			} else {
				throw new SQLException("Can't convert type to BigDecimal: " + value.getClass());
			}
		}

		return result;
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return cursor < 0;
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		return rows.isLastPart() && cursor == rows.size();
	}

	@Override
	public boolean isFirst() throws SQLException {
		return cursor == 0;
	}

	@Override
	public boolean isLast() throws SQLException {
		return rows.isLastPart() && cursor == rows.size() - 1;
	}

	@Override
	public void beforeFirst() throws SQLException {
		cursor = -1;
		actualRow = null;
	}

	@Override
	public void afterLast() throws SQLException {
		// Request all remaining Row-Packets
		while (requestNextRowPacket()) {
			// empty
		}
		cursor = rows.size();
		actualRow = null;
	}

	@Override
	public boolean first() throws SQLException {
		try {
			cursor = 0;
			actualRow = rows.get(cursor);
			return true;
		} catch (final SQLException e) {
			return false;
		}
	}

	@Override
	public boolean last() throws SQLException {
		try {
			// Request all remaining Row-Packets
			while (requestNextRowPacket()) {
				// empty
			}
			cursor = rows.size() - 1;
			actualRow = rows.get(cursor);
			return true;
		} catch (final SQLException e) {
			return false;
		}
	}

	@Override
	public int getRow() throws SQLException {
		return cursor + 1;
	}

	@Override
	public boolean absolute(final int row) throws SQLException {
		return setCursor(row - 1);
	}

	@Override
	public boolean relative(final int step) throws SQLException {
		return setCursor(cursor + step);
	}

	@Override
	public boolean previous() throws SQLException {
		if (forwardOnly) {
			throw new SQLException("previous() not possible on Forward-Only-ResultSet");
		}
		if (cursor > 0) {
			actualRow = rows.get(--cursor);
			return true;
		}
		return false;
	}

	@Override
	public void setFetchDirection(final int direction) throws SQLException {
		fetchDirection = direction;
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return fetchDirection;
	}

	@Override
	public void setFetchSize(final int rows) throws SQLException {
		// empty
	}

	@Override
	public int getFetchSize() throws SQLException {
		return 0;
	}

	@Override
	public int getType() throws SQLException {
		return forwardOnly ? ResultSet.TYPE_FORWARD_ONLY : ResultSet.TYPE_SCROLL_INSENSITIVE;
	}

	@Override
	public int getConcurrency() throws SQLException {
		return ResultSet.CONCUR_READ_ONLY;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		return false;
	}

	@Override
	public void updateNull(final int columnIndex) throws SQLException {
		throw new UnsupportedOperationException("updateNull");
	}

	@Override
	public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
		throw new UnsupportedOperationException("updateBoolean");
	}

	@Override
	public void updateByte(final int columnIndex, final byte x) throws SQLException {
		throw new UnsupportedOperationException("updateByte");
	}

	@Override
	public void updateShort(final int columnIndex, final short x) throws SQLException {
		throw new UnsupportedOperationException("updateShort");
	}

	@Override
	public void updateInt(final int columnIndex, final int x) throws SQLException {
		throw new UnsupportedOperationException("updateInt");
	}

	@Override
	public void updateLong(final int columnIndex, final long x) throws SQLException {
		throw new UnsupportedOperationException("updateLong");
	}

	@Override
	public void updateFloat(final int columnIndex, final float x) throws SQLException {
		throw new UnsupportedOperationException("updateFloat");
	}

	@Override
	public void updateDouble(final int columnIndex, final double x) throws SQLException {
		throw new UnsupportedOperationException("updateDouble");
	}

	@Override
	public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
		throw new UnsupportedOperationException("updateBigDecimal");
	}

	@Override
	public void updateString(final int columnIndex, final String x) throws SQLException {
		throw new UnsupportedOperationException("updateString");
	}

	@Override
	public void updateBytes(final int columnIndex, final byte x[]) throws SQLException {
		throw new UnsupportedOperationException("updateBytes");
	}

	@Override
	public void updateDate(final int columnIndex, final Date x) throws SQLException {
		throw new UnsupportedOperationException("updateDate");
	}

	@Override
	public void updateTime(final int columnIndex, final Time x) throws SQLException {
		throw new UnsupportedOperationException("updateTime");
	}

	@Override
	public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
		throw new UnsupportedOperationException("updateTimestamp");
	}

	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
		throw new UnsupportedOperationException("updateAsciiStream");
	}

	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
		throw new UnsupportedOperationException("updateBinaryStream");
	}

	@Override
	public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
		throw new UnsupportedOperationException("updateCharacterStream");
	}

	@Override
	public void updateObject(final int columnIndex, final Object x, final int scale) throws SQLException {
		throw new UnsupportedOperationException("updateObject");
	}

	@Override
	public void updateObject(final int columnIndex, final Object x) throws SQLException {
		throw new UnsupportedOperationException("updateObject");
	}

	@Override
	public void updateNull(final String columnName) throws SQLException {
		throw new UnsupportedOperationException("updateNull");
	}

	@Override
	public void updateBoolean(final String columnName, final boolean x) throws SQLException {
		throw new UnsupportedOperationException("updateBoolean");
	}

	@Override
	public void updateByte(final String columnName, final byte x) throws SQLException {
		throw new UnsupportedOperationException("updateByte");
	}

	@Override
	public void updateShort(final String columnName, final short x) throws SQLException {
		throw new UnsupportedOperationException("updateShort");
	}

	@Override
	public void updateInt(final String columnName, final int x) throws SQLException {
		throw new UnsupportedOperationException("updateInt");
	}

	@Override
	public void updateLong(final String columnName, final long x) throws SQLException {
		throw new UnsupportedOperationException("updateLong");
	}

	@Override
	public void updateFloat(final String columnName, final float x) throws SQLException {
		throw new UnsupportedOperationException("updateFloat");
	}

	@Override
	public void updateDouble(final String columnName, final double x) throws SQLException {
		throw new UnsupportedOperationException("updateDouble");
	}

	@Override
	public void updateBigDecimal(final String columnName, final BigDecimal x) throws SQLException {
		throw new UnsupportedOperationException("updateBigDecimal");
	}

	@Override
	public void updateString(final String columnName, final String x) throws SQLException {
		throw new UnsupportedOperationException("updateString");
	}

	@Override
	public void updateBytes(final String columnName, final byte x[]) throws SQLException {
		throw new UnsupportedOperationException("updateBytes");
	}

	@Override
	public void updateDate(final String columnName, final Date x) throws SQLException {
		throw new UnsupportedOperationException("updateDate");
	}

	@Override
	public void updateTime(final String columnName, final Time x) throws SQLException {
		throw new UnsupportedOperationException("updateTime");
	}

	@Override
	public void updateTimestamp(final String columnName, final Timestamp x) throws SQLException {
		throw new UnsupportedOperationException("updateTimestamp");
	}

	@Override
	public void updateAsciiStream(final String columnName, final InputStream x, final int length) throws SQLException {
		throw new UnsupportedOperationException("updateAsciiStream");
	}

	@Override
	public void updateBinaryStream(final String columnName, final InputStream x, final int length) throws SQLException {
		throw new UnsupportedOperationException("updateBinaryStream");
	}

	@Override
	public void updateCharacterStream(final String columnName, final Reader reader, final int length) throws SQLException {
		throw new UnsupportedOperationException("updateCharacterStream");
	}

	@Override
	public void updateObject(final String columnName, final Object x, final int scale) throws SQLException {
		throw new UnsupportedOperationException("updateObject");
	}

	@Override
	public void updateObject(final String columnName, final Object x) throws SQLException {
		throw new UnsupportedOperationException("updateObject");
	}

	@Override
	public void insertRow() throws SQLException {
		throw new UnsupportedOperationException("insertRow");
	}

	@Override
	public void updateRow() throws SQLException {
		throw new UnsupportedOperationException("updateRow");
	}

	@Override
	public void deleteRow() throws SQLException {
		throw new UnsupportedOperationException("deleteRow");
	}

	@Override
	public void refreshRow() throws SQLException {
		throw new UnsupportedOperationException("refreshRow");
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		throw new UnsupportedOperationException("cancelRowUpdates");
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		throw new UnsupportedOperationException("moveToInsertRow");
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		throw new UnsupportedOperationException("moveToCurrentRow");
	}

	@Override
	public Statement getStatement() throws SQLException {
		return statement;
	}

	@Override
	public Object getObject(final int i, final Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException("getObject");
	}

	@Override
	public Ref getRef(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return (Ref) actualRow[ci];
		}
		return null;
	}

	@Override
	public Blob getBlob(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return (Blob) actualRow[ci];
		}
		return null;
	}

	@Override
	public Clob getClob(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return (Clob) actualRow[ci];
		}
		return null;
	}

	@Override
	public Array getArray(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return (Array) actualRow[ci];
		}
		return null;
	}

	@Override
	public Object getObject(final String colName, final Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException("getObject");
	}

	@SuppressWarnings("static-method")
	public <T> T getObject(final String columnName, final Class<T> clazz) {
		throw new UnsupportedOperationException("getObject(String, Class)");
	}

	@SuppressWarnings("static-method")
	public <T> T getObject(final int columnIndex, final Class<T> clazz) {
		throw new UnsupportedOperationException("getObject(int, Class)");
	}

	@Override
	public Ref getRef(final String colName) throws SQLException {
		return getRef(getIndexForName(colName));
	}

	@Override
	public Blob getBlob(final String colName) throws SQLException {
		return getBlob(getIndexForName(colName));
	}

	@Override
	public Clob getClob(final String colName) throws SQLException {
		return getClob(getIndexForName(colName));
	}

	@Override
	public Array getArray(final String colName) throws SQLException {
		return getArray(getIndexForName(colName));
	}

	@Override
	public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			cal.setTime(getDate(ci));
			return new Date(cal.getTimeInMillis());
		}
		return null;
	}

	@Override
	public Date getDate(final String columnName, final Calendar cal) throws SQLException {
		return getDate(getIndexForName(columnName), cal);
	}

	@Override
	public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			final Time time = (Time) actualRow[ci];
			cal.setTime(time);
			return (Time) cal.getTime();
		}
		return null;
	}

	@Override
	public Time getTime(final String columnName, final Calendar cal) throws SQLException {
		return getTime(getIndexForName(columnName), cal);
	}

	@Override
	public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
		final Timestamp timestamp = getTimestamp(columnIndex);
		if (timestamp != null) {
			cal.setTime(timestamp);
			return (Timestamp) cal.getTime();
		}
		return null;
	}

	@Override
	public Timestamp getTimestamp(final String columnName, final Calendar cal) throws SQLException {
		return getTimestamp(getIndexForName(columnName), cal);
	}

	@Override
	public URL getURL(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return (URL) actualRow[ci];
		}
		return null;
	}

	@Override
	public URL getURL(final String columnName) throws SQLException {
		return getURL(getIndexForName(columnName));
	}

	@Override
	public void updateRef(final int columnIndex, final Ref x) throws SQLException {
		throw new UnsupportedOperationException("updateRef");
	}

	@Override
	public void updateRef(final String columnName, final Ref x) throws SQLException {
		throw new UnsupportedOperationException("updateRef");
	}

	@Override
	public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
		throw new UnsupportedOperationException("updateBlob");
	}

	@Override
	public void updateBlob(final String columnName, final Blob x) throws SQLException {
		throw new UnsupportedOperationException("updateBlob");
	}

	@Override
	public void updateClob(final int columnIndex, final Clob x) throws SQLException {
		throw new UnsupportedOperationException("updateClob");
	}

	@Override
	public void updateClob(final String columnName, final Clob x) throws SQLException {
		throw new UnsupportedOperationException("updateClob");
	}

	@Override
	public void updateArray(final int columnIndex, final Array x) throws SQLException {
		throw new UnsupportedOperationException("updateArray");
	}

	@Override
	public void updateArray(final String columnName, final Array x) throws SQLException {
		throw new UnsupportedOperationException("updateArray");
	}

	private int getIndexForName(final String name) throws SQLException {
		int result = -1;
		final String nameLowercase = name.toLowerCase();
		// first search in the columns names (hit is very likely)
		for (int i = 0; i < columnNames.length; ++i) {
			if (columnNames[i].equals(nameLowercase)) {
				result = i;
				break;
			}
		}
		// not found ? then search in the labels
		if (result < 0) {
			for (int i = 0; i < columnLabels.length; ++i) {
				if (columnLabels[i].equals(nameLowercase)) {
					result = i;
					break;
				}
			}
		}
		if (result < 0) {
			throw new SQLException("Unknown column " + name);
		}
		lastReadColumn = result;
		return result + 1;
	}

	private boolean preGetCheckNull(final int index) {
		lastReadColumn = index;
		final boolean wasNull = actualRow[lastReadColumn] == null;
		return !wasNull;
	}

	private boolean requestNextRowPacket() throws SQLException {
		if (!lastPartReached) {
			try {
				final RowPacket rsp = commandSink.process(remainingResultSet, new NextRowPacketCommand());
				if (rsp.isLastPart()) {
					lastPartReached = true;
				}
				if (rsp.size() > 0) {
					rows.merge(rsp);
					return true;
				}
				return false;
			} catch (final Exception e) {
				throw SQLExceptionHelper.wrap(e);
			}
		}
		return false;
	}

	private boolean setCursor(final int row) throws SQLException {
		if (row >= 0) {
			if (row < rows.size()) {
				cursor = row;
				actualRow = rows.get(cursor);
				return true;
			}
			// If new row is not in the range of the actually available
			// rows then try to load the next row packets successively
			while (requestNextRowPacket()) {
				if (row < rows.size()) {
					cursor = row;
					actualRow = rows.get(cursor);
					return true;
				}
			}
			return false;
		}
		return false;
	}

	private static Date getCleanDate(final long millis) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	private static Time getCleanTime(final long millis) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MILLISECOND, 0);
		return new Time(cal.getTimeInMillis());
	}

	/* start JDBC4 support */
	@Override
	public RowId getRowId(final int parameterIndex) throws SQLException {
		return commandSink.process(remainingResultSet, ReflectiveCommand.<RowId, Object> of(JdbcInterfaceType.RESULTSETHOLDER, "getRowId",
				new Object[] { Integer.valueOf(parameterIndex) }, ParameterTypeCombinations.INT));
	}

	@Override
	public RowId getRowId(final String parameterName) throws SQLException {
		return commandSink.process(remainingResultSet, ReflectiveCommand.<RowId, Object> of(JdbcInterfaceType.RESULTSETHOLDER, "getRowId",
				new Object[] { parameterName }, ParameterTypeCombinations.STR));
	}

	@SuppressWarnings("static-method")
	public void setRowId(final String parameterName, final RowId x) throws SQLException {
		throw new UnsupportedOperationException("setRowId");
	}

	@Override
	public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
		throw new UnsupportedOperationException("updateRowId");
	}

	@Override
	public void updateRowId(final String columnLabel, final RowId x) throws SQLException {
		throw new UnsupportedOperationException("updateRowId");
	}

	@Override
	public int getHoldability() throws SQLException {
		return commandSink.processWithIntResult(remainingResultSet,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.RESULTSETHOLDER, "getHoldability"));
	}

	@Override
	public boolean isClosed() throws SQLException {
		return cursor < 0;
	}

	@Override
	public void updateNString(final int columnIndex, final String nString) throws SQLException {
		throw new UnsupportedOperationException("updateNString");
	}

	@Override
	public void updateNString(final String columnLabel, final String nString) throws SQLException {
		throw new UnsupportedOperationException("updateNString");
	}

	@Override
	public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
		throw new UnsupportedOperationException("updateNClob");
	}

	@Override
	public void updateNClob(final String columnLabel, final NClob nClob) throws SQLException {
		throw new UnsupportedOperationException("updateNClob");
	}

	@Override
	public NClob getNClob(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return (NClob) actualRow[ci];
		}
		return null;
	}

	@Override
	public NClob getNClob(final String columnLabel) throws SQLException {
		return getNClob(getIndexForName(columnLabel));
	}

	@Override
	public SQLXML getSQLXML(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return (SQLXML) actualRow[ci];
		}
		return null;
	}

	@Override
	public SQLXML getSQLXML(final String columnLabel) throws SQLException {
		return getSQLXML(getIndexForName(columnLabel));
	}

	@Override
	public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException {
		throw new UnsupportedOperationException("updateSQLXML");
	}

	@Override
	public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException {
		throw new UnsupportedOperationException("updateSQLXML");
	}

	@Override
	public String getNString(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return actualRow[ci].toString();
		}
		return null;
	}

	@Override
	public String getNString(final String columnLabel) throws SQLException {
		return getNString(getIndexForName(columnLabel));
	}

	@Override
	public Reader getNCharacterStream(final int columnIndex) throws SQLException {
		final int ci = columnIndex - 1;
		if (preGetCheckNull(ci)) {
			return new StringReader((String) actualRow[ci]);
		}
		return null;
	}

	@Override
	public Reader getNCharacterStream(final String columnLabel) throws SQLException {
		return getNCharacterStream(getIndexForName(columnLabel));
	}

	@Override
	public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateNCharacterStream");
	}

	@Override
	public void updateNCharacterStream(final String columnLabel, final Reader x, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateNCharacterStream");
	}

	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateAsciiStream");
	}

	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateBinaryStream");
	}

	@Override
	public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateCharacterStream");
	}

	@Override
	public void updateAsciiStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateAsciiStream");
	}

	@Override
	public void updateBinaryStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateBinaryStream");
	}

	@Override
	public void updateCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateCharacterStream");
	}

	@Override
	public void updateBlob(final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateBlob");
	}

	@Override
	public void updateBlob(final String columnLabel, final InputStream inputStream, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateBlob");
	}

	@Override
	public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateClob");
	}

	@Override
	public void updateClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateClob");
	}

	@Override
	public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateNClob");
	}

	@Override
	public void updateNClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
		throw new UnsupportedOperationException("updateNClob");
	}

	@Override
	public void updateNCharacterStream(final int columnIndex, final Reader reader) throws SQLException {
		throw new UnsupportedOperationException("updateNCharacterStream");
	}

	@Override
	public void updateNCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
		throw new UnsupportedOperationException("updateNCharacterStream");
	}

	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
		throw new UnsupportedOperationException("updateAsciiStream");
	}

	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
		throw new UnsupportedOperationException("updateBinaryStream");
	}

	@Override
	public void updateCharacterStream(final int columnIndex, final Reader reader) throws SQLException {
		throw new UnsupportedOperationException("updateCharacterStream");
	}

	@Override
	public void updateCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
		throw new UnsupportedOperationException("updateCharacterStream");
	}

	@Override
	public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
		throw new UnsupportedOperationException("updateAsciiStream");
	}

	@Override
	public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
		throw new UnsupportedOperationException("updateBinaryStream");
	}

	@Override
	public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException {
		throw new UnsupportedOperationException("updateBlob");
	}

	@Override
	public void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException {
		throw new UnsupportedOperationException("updateBlob");
	}

	@Override
	public void updateClob(final int columnIndex, final Reader reader) throws SQLException {
		throw new UnsupportedOperationException("updateClob");
	}

	@Override
	public void updateClob(final String columnLabel, final Reader reader) throws SQLException {
		throw new UnsupportedOperationException("updateClob");
	}

	@Override
	public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
		throw new UnsupportedOperationException("updateNClob");
	}

	@Override
	public void updateNClob(final String columnLabel, final Reader reader) throws SQLException {
		throw new UnsupportedOperationException("updateNClob");
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return iface.isAssignableFrom(StreamingResultSet.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return (T) this;
	}
}
