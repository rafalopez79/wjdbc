package com.bzsoft.wjdbc.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.ArrayFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.BigDecimalFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.BlobFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.BooleanFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.ByteFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.BytesFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.ClobFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.DateFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.DoubleFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.FloatFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.IntFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.JavaObjectFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.LongFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.NClobFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.NStringFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.NullFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.RowIdFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.SQLXMLFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.ShortFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.StringFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.StructFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.TimeFlattenedColumnValuesParser;
import com.bzsoft.wjdbc.serial.FlattenedColumnValuesParser.TimestampFlattenedColumnValuesParser;

/**
 * A RowPacket contains the data of a part (or a whole) JDBC-ResultSet.
 */
public final class RowPacket implements Externalizable {

	private static final long													serialVersionUID			= 6366194574502000718L;

	private static final int													ORACLE_ROWID				= -8;
	private static final int													DEFAULT_ARRAY_SIZE		= 255;
	private static final Map<Integer, FlattenedColumnValuesParser>	PARSERMAP;

	private int																		rowCount						= 0;
	private boolean																forwardOnly					= false;
	private boolean																lastPart						= false;

	// Transient attributes
	private transient FlattenedColumnValues[]								flattenedColumnsValues	= null;
	private transient Object[][]												rows							= null;
	private transient FlattenedColumnValuesParser[]						fcvParser					= null;
	private transient int[]														columnTypes					= null;
	private transient int														offset						= 0;
	private transient int														maxrows						= 0;

	static {
		final Map<Integer, FlattenedColumnValuesParser> map = new HashMap<Integer, FlattenedColumnValuesParser>();
		map.put(Types.NULL, new NullFlattenedColumnValuesParser());

		FlattenedColumnValuesParser parser = new StringFlattenedColumnValuesParser();
		map.put(Types.CHAR, parser);
		map.put(Types.VARCHAR, parser);
		map.put(Types.LONGVARCHAR, parser);

		parser = new NStringFlattenedColumnValuesParser();
		map.put(Types.NCHAR, parser);
		map.put(Types.NVARCHAR, parser);
		map.put(Types.LONGNVARCHAR, parser);

		parser = new BigDecimalFlattenedColumnValuesParser();
		map.put(Types.NUMERIC, parser);
		map.put(Types.DECIMAL, parser);

		parser = new BooleanFlattenedColumnValuesParser();
		map.put(Types.BIT, parser);
		map.put(Types.BOOLEAN, parser);

		map.put(Types.TINYINT, new ByteFlattenedColumnValuesParser());
		map.put(Types.SMALLINT, new ShortFlattenedColumnValuesParser());
		map.put(Types.INTEGER, new IntFlattenedColumnValuesParser());
		map.put(Types.BIGINT, new LongFlattenedColumnValuesParser());
		map.put(Types.REAL, new FloatFlattenedColumnValuesParser());

		parser = new DoubleFlattenedColumnValuesParser();
		map.put(Types.DOUBLE, parser);
		map.put(Types.FLOAT, parser);
		map.put(Types.DATE, new DateFlattenedColumnValuesParser());
		map.put(Types.TIME, new TimeFlattenedColumnValuesParser());
		map.put(Types.TIMESTAMP, new TimestampFlattenedColumnValuesParser());

		parser = new BytesFlattenedColumnValuesParser();
		map.put(Types.BINARY, parser);
		map.put(Types.VARBINARY, parser);
		map.put(Types.LONGVARBINARY, parser);

		map.put(Types.JAVA_OBJECT, new JavaObjectFlattenedColumnValuesParser());
		map.put(Types.CLOB, new ClobFlattenedColumnValuesParser());
		map.put(Types.NCLOB, new NClobFlattenedColumnValuesParser());
		map.put(Types.BLOB, new BlobFlattenedColumnValuesParser());
		map.put(Types.ARRAY, new ArrayFlattenedColumnValuesParser());
		map.put(Types.STRUCT, new StructFlattenedColumnValuesParser());

		map.put(ORACLE_ROWID, new RowIdFlattenedColumnValuesParser());
		map.put(Types.SQLXML, new SQLXMLFlattenedColumnValuesParser());
		PARSERMAP = Collections.unmodifiableMap(map);
	}

	public RowPacket() {
		// empty
	}

	public RowPacket(final int packetsize, final boolean forwardOnly) {
		maxrows = packetsize;
		this.forwardOnly = forwardOnly;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeBoolean(forwardOnly);
		out.writeBoolean(lastPart);
		out.writeInt(rowCount);
		if (rowCount > 0) {
			out.writeInt(flattenedColumnsValues.length);
			if (rowCount != maxrows) {
				for (final FlattenedColumnValues fcv : flattenedColumnsValues) {
					FlattenedColumnValues.compact(fcv, rowCount).writeExternal(out);
				}
			} else {
				for (final FlattenedColumnValues fcv : flattenedColumnsValues) {
					fcv.writeExternal(out);
				}
			}
		}
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		forwardOnly = in.readBoolean();
		lastPart = in.readBoolean();
		rowCount = in.readInt();
		if (rowCount > 0) {
			final int len = in.readInt();
			final FlattenedColumnValues[] flattenedColumns = new FlattenedColumnValues[len];
			for (int i = 0; i < len; i++) {
				final FlattenedColumnValues fci = new FlattenedColumnValues();
				fci.readExternal(in);
				flattenedColumns[i] = fci;
			}
			rows = new Object[rowCount][];
			for (int i = 0; i < rowCount; i++) {
				final Object[] row = new Object[flattenedColumns.length];
				for (int j = 0; j < flattenedColumns.length; j++) {
					row[j] = flattenedColumns[j].getValue(i);
				}
				rows[i] = row;
			}
		} else {
			rows = new Object[0][0];
		}
	}

	public Object[] get(final int index) throws SQLException {
		final int adjustedIndex = index - offset;
		if (adjustedIndex < 0) {
			throw new SQLException("Index " + index + " is below the possible index");
		} else if (adjustedIndex >= rows.length) {
			throw new SQLException("Index " + index + " is above the possible index");
		}
		return rows[adjustedIndex];
	}

	public int size() {
		return offset + rowCount;
	}

	public boolean isLastPart() {
		return lastPart;
	}

	public final boolean populate(final ResultSet rs) throws SQLException {
		final ResultSetMetaData metaData = rs.getMetaData();
		final int columnCount = metaData.getColumnCount();
		prepareFlattenedColumns(metaData, columnCount);
		rowCount = 0;
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				final int j = i - 1;
				final FlattenedColumnValues fcv = flattenedColumnsValues[j];
				final FlattenedColumnValuesParser fcvp = fcvParser[j];
				fcvp.setObject(fcv, rs, i, rowCount);
			}
			rowCount++;
			if (maxrows > 0 && rowCount == maxrows) {
				break;
			}
		}
		lastPart = maxrows == 0 || rowCount < maxrows;
		return lastPart;
	}

	private final void prepareFlattenedColumns(final ResultSetMetaData metaData, final int columnCount) throws SQLException {
		columnTypes = new int[columnCount];
		flattenedColumnsValues = new FlattenedColumnValues[columnCount];
		fcvParser = new FlattenedColumnValuesParser[columnCount];
		for (int i = 1; i <= columnCount; i++) {
			final int j = i - 1;
			final int columnType = columnTypes[j] = metaData.getColumnType(i);
			final FlattenedColumnValuesParser fp = PARSERMAP.get(Integer.valueOf(columnType));
			if (fp == null) {
				throw new SQLException("Unsupported JDBC-Type: " + columnType);
			}
			final Class<?> componentType = fp.getSQLClass();
			flattenedColumnsValues[j] = new FlattenedColumnValues(componentType, maxrows == 0 ? DEFAULT_ARRAY_SIZE : maxrows);
			fcvParser[j] = fp;
		}
	}

	public void merge(final RowPacket rsp) {
		if (forwardOnly) {
			offset += rowCount;
			rowCount = rsp.rowCount;
			rows = rsp.rows;
		} else {
			final Object[][] nrows = new Object[rows.length + rsp.rows.length][];
			System.arraycopy(rows, 0, nrows, 0, rows.length);
			System.arraycopy(rsp.rows, 0, nrows, rows.length, rsp.rows.length);
			rows = nrows;
			rowCount = rows.length;
		}
	}
}
