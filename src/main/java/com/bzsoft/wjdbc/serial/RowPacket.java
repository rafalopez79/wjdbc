package com.bzsoft.wjdbc.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * A RowPacket contains the data of a part (or a whole) JDBC-ResultSet.
 */
public class RowPacket implements Externalizable {

	private static final long						serialVersionUID			= 6366194574502000718L;

	private static final int						ORACLE_ROWID				= -8;
	private static final int						DEFAULT_ARRAY_SIZE		= 100;

	private int											rowCount						= 0;
	private boolean									forwardOnly					= false;
	private boolean									lastPart						= false;

	// Transient attributes
	private transient FlattenedColumnValues[]	flattenedColumnsValues	= null;
	private transient List<Object[]>				rows							= null;
	private transient int[]							columnTypes					= null;
	private transient int							offset						= 0;
	private transient int							maxrows						= 0;

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
					out.writeObject(FlattenedColumnValues.compress(fcv, rowCount));
				}
			} else {
				for (final FlattenedColumnValues fcv : flattenedColumnsValues) {
					out.writeObject(fcv);
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
				flattenedColumns[i] = (FlattenedColumnValues) in.readObject();
			}
			rows = new ArrayList<Object[]>(rowCount);
			for (int i = 0; i < rowCount; i++) {
				final Object[] row = new Object[flattenedColumns.length];
				for (int j = 0; j < flattenedColumns.length; j++) {
					row[j] = flattenedColumns[j].getValue(i);
				}
				rows.add(row);
			}
		} else {
			rows = new ArrayList<Object[]>(0);
		}
	}

	public Object[] get(final int index) throws SQLException {
		final int adjustedIndex = index - offset;
		if (adjustedIndex < 0) {
			throw new SQLException("Index " + index + " is below the possible index");
		} else if (adjustedIndex >= rows.size()) {
			throw new SQLException("Index " + index + " is above the possible index");
		} else {
			return rows.get(adjustedIndex);
		}
	}

	public int size() {
		return offset + rowCount;
	}

	public boolean isLastPart() {
		return lastPart;
	}

	public boolean populate(final ResultSet rs) throws SQLException {
		final ResultSetMetaData metaData = rs.getMetaData();
		final int columnCount = metaData.getColumnCount();
		rowCount = 0;
		while (rs.next()) {
			if (rowCount == 0) {
				prepareFlattenedColumns(metaData, columnCount);
			}
			for (int i = 1; i <= columnCount; i++) {
				boolean foundMatch = true;
				final int internalIndex = i - 1;
				switch (columnTypes[internalIndex]) {
				case Types.NULL:
					flattenedColumnsValues[internalIndex].setObject(rowCount, null);
					break;
				case Types.CHAR:
				case Types.VARCHAR:
				case Types.LONGVARCHAR:
					flattenedColumnsValues[internalIndex].setObject(rowCount, rs.getString(i));
					break;
				case Types.NCHAR:
				case Types.NVARCHAR:
				case Types.LONGNVARCHAR:
					flattenedColumnsValues[internalIndex].setObject(rowCount, rs.getNString(i));
					break;
				case Types.NUMERIC:
				case Types.DECIMAL:
					flattenedColumnsValues[internalIndex].setObject(rowCount, rs.getBigDecimal(i));
					break;
				case Types.BIT:
				case Types.BOOLEAN:
					flattenedColumnsValues[internalIndex].setBoolean(rowCount, rs.getBoolean(i));
					break;
				case Types.TINYINT:
					flattenedColumnsValues[internalIndex].setByte(rowCount, rs.getByte(i));
					break;
				case Types.SMALLINT:
					flattenedColumnsValues[internalIndex].setShort(rowCount, rs.getShort(i));
					break;

				case Types.INTEGER:
					flattenedColumnsValues[internalIndex].setInt(rowCount, rs.getInt(i));
					break;

				case Types.BIGINT:
					flattenedColumnsValues[internalIndex].setLong(rowCount, rs.getLong(i));
					break;

				case Types.REAL:
					flattenedColumnsValues[internalIndex].setFloat(rowCount, rs.getFloat(i));
					break;

				case Types.FLOAT:
				case Types.DOUBLE:
					flattenedColumnsValues[internalIndex].setDouble(rowCount, rs.getDouble(i));
					break;

				case Types.DATE:
					flattenedColumnsValues[internalIndex].setObject(rowCount, rs.getDate(i));
					break;

				case Types.TIME:
					flattenedColumnsValues[internalIndex].setObject(rowCount, rs.getTime(i));
					break;

				case Types.TIMESTAMP:
					flattenedColumnsValues[internalIndex].setObject(rowCount, rs.getTimestamp(i));
					break;

				case Types.BINARY:
				case Types.VARBINARY:
				case Types.LONGVARBINARY:
					flattenedColumnsValues[internalIndex].setObject(rowCount, rs.getBytes(i));
					break;

				case Types.JAVA_OBJECT:
					flattenedColumnsValues[internalIndex].setObject(rowCount, new SerialJavaObject(rs.getObject(i)));
					break;

				case Types.CLOB:
					flattenedColumnsValues[internalIndex].setObject(rowCount, new SerialClob(rs.getClob(i)));
					break;

				case Types.NCLOB:
					flattenedColumnsValues[internalIndex].setObject(rowCount, new SerialNClob(rs.getNClob(i)));
					break;

				case Types.BLOB:
					flattenedColumnsValues[internalIndex].setObject(rowCount, new SerialBlob(rs.getBlob(i)));
					break;

				case Types.ARRAY:
					flattenedColumnsValues[internalIndex].setObject(rowCount, new SerialArray(rs.getArray(i)));
					break;

				case Types.STRUCT:
					flattenedColumnsValues[internalIndex].setObject(rowCount, new SerialStruct((Struct) rs.getObject(i)));
					break;

				case ORACLE_ROWID:
					flattenedColumnsValues[internalIndex].setObject(rowCount, new SerialRowId(rs.getRowId(i)));
					break;

				// what oracle does instead of SQLXML in their 1.6 driver,
				// don't ask me why, commented out so we don't need
				// an oracle driver to compile this class
				// case 2007:
				// flattenedColumnsValues[internalIndex].setObject(rowCount, new
				// XMLType(((OracleResultSet)rs).getOPAQUE(i)));
				case Types.SQLXML:
					flattenedColumnsValues[internalIndex].setObject(rowCount, new SerialSQLXML(rs.getSQLXML(i)));
					break;

				default:
					foundMatch = false;
					break;
				}

				if (foundMatch) {
					if (rs.wasNull()) {
						flattenedColumnsValues[internalIndex].setIsNull(rowCount);
					}
				} else {
					throw new SQLException("Unsupported JDBC-Type: " + columnTypes[internalIndex]);
				}
			}
			rowCount++;
			if (maxrows > 0 && rowCount == maxrows) {
				break;
			}
		}
		lastPart = maxrows == 0 || rowCount < maxrows;
		return lastPart;
	}

	private void prepareFlattenedColumns(final ResultSetMetaData metaData, final int columnCount) throws SQLException {
		columnTypes = new int[columnCount];
		flattenedColumnsValues = new FlattenedColumnValues[columnCount];
		for (int i = 1; i <= columnCount; i++) {
			final int columnType = columnTypes[i - 1] = metaData.getColumnType(i);
			Class<?> componentType = null;
			switch (columnType) {
			case Types.BIT:
			case Types.BOOLEAN:
				componentType = boolean.class;
				break;
			case Types.TINYINT:
				componentType = byte.class;
				break;
			case Types.SMALLINT:
				componentType = short.class;
				break;
			case Types.INTEGER:
				componentType = int.class;
				break;
			case Types.BIGINT:
				componentType = long.class;
				break;
			case Types.REAL:
				componentType = float.class;
				break;
			case Types.FLOAT:
			case Types.DOUBLE:
				componentType = double.class;
				break;
			default:
				componentType = Object.class;
				break;
			}
			flattenedColumnsValues[i - 1] = new FlattenedColumnValues(componentType, maxrows == 0 ? DEFAULT_ARRAY_SIZE : maxrows);
		}
	}

	public void merge(final RowPacket rsp) {
		if (forwardOnly) {
			offset += rowCount;
			rowCount = rsp.rowCount;
			rows = rsp.rows;
		} else {
			rows.addAll(rsp.rows);
			rowCount = rows.size();
		}
	}
}
