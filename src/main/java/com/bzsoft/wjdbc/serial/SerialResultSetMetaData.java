package com.bzsoft.wjdbc.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SerialResultSetMetaData implements ResultSetMetaData, Externalizable {

	private static final long	serialVersionUID	= 9034215340975782405L;

	private int						columnCount;

	private String[]				catalogName;
	private String[]				schemaName;
	private String[]				tableName;
	private String[]				columnClassName;
	private String[]				columnLabel;
	private String[]				columnName;
	private String[]				columnTypeName;

	private Integer[]				columnType;
	private Integer[]				columnDisplaySize;
	private Integer[]				precision;
	private Integer[]				scale;
	private Integer[]				nullable;

	private Boolean[]				autoIncrement;
	private Boolean[]				caseSensitive;
	private Boolean[]				currency;
	private Boolean[]				readOnly;
	private Boolean[]				searchable;
	private Boolean[]				signed;
	private Boolean[]				writable;
	private Boolean[]				definitivelyWritable;

	public SerialResultSetMetaData() {
		// empty
	}

	public SerialResultSetMetaData(final ResultSetMetaData rsmd) throws SQLException {
		columnCount = rsmd.getColumnCount();
		allocateArrays();
		fillArrays(rsmd);
	}

	private static String[] readStringArr(final ObjectInput in) throws IOException {
		final int numElems = in.readShort();
		if (numElems != -1) {
			final String ret[] = new String[numElems];
			for (int i = 0; i < numElems; i++) {
				final byte notNull = in.readByte();
				if (1 == notNull) {
					ret[i] = in.readUTF();
				} else {
					ret[i] = null;
				}
			}
			return ret;
		}
		return null;
	}

	private static void writeStringArr(final String arr[], final ObjectOutput out) throws IOException {
		if (arr != null) {
			out.writeShort(arr.length);
			for (final String element : arr) {
				if (element != null) {
					out.writeByte(1);
					out.writeUTF(element);
				} else {
					out.writeByte(0);
				}
			}
		} else {
			out.writeShort(-1);
		}
	}

	private static Integer[] readIntArr(final ObjectInput in) throws IOException {
		final int numElems = in.readShort();
		if (numElems != -1) {
			final Integer ret[] = new Integer[numElems];
			for (int i = 0; i < numElems; i++) {
				final byte notNull = in.readByte();
				if (notNull == 1) {
					ret[i] = Integer.valueOf(in.readInt());
				} else {
					ret[i] = null;
				}
			}
			return ret;
		}
		return null;
	}

	private static void writeIntArr(final Integer arr[], final ObjectOutput out) throws IOException {
		if (arr != null) {
			out.writeShort(arr.length);
			for (final Integer element : arr) {
				if (element != null) {
					out.writeByte(1);
					out.writeInt(element.intValue());
				} else {
					out.writeByte(0);
				}
			}
		} else {
			out.writeShort(-1);
		}
	}

	private static Boolean[] readBooleanArr(final ObjectInput in) throws IOException {
		final int numElems = in.readShort();
		if (numElems != -1) {
			final Boolean ret[] = new Boolean[numElems];
			for (int i = 0; i < numElems; i++) {
				final byte notNull = in.readByte();
				if (notNull == 1) {
					ret[i] = Boolean.valueOf(in.readBoolean());
				} else {
					ret[i] = null;
				}
			}
			return ret;
		}
		return null;
	}

	private static void writeBooleanArr(final Boolean arr[], final ObjectOutput out) throws IOException {
		if (arr != null) {
			out.writeShort(arr.length);
			for (final Boolean element : arr) {
				if (element != null) {
					out.writeByte(1);
					out.writeBoolean(element.booleanValue());
				} else {
					out.writeByte(0);
				}
			}
		} else {
			out.writeShort(-1);
		}
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		columnCount = in.readInt();

		catalogName = readStringArr(in);
		schemaName = readStringArr(in);
		tableName = readStringArr(in);
		columnClassName = readStringArr(in);
		columnLabel = readStringArr(in);
		columnName = readStringArr(in);
		columnTypeName = readStringArr(in);

		columnType = readIntArr(in);
		columnDisplaySize = readIntArr(in);
		precision = readIntArr(in);
		scale = readIntArr(in);
		nullable = readIntArr(in);

		autoIncrement = readBooleanArr(in);
		caseSensitive = readBooleanArr(in);
		currency = readBooleanArr(in);
		readOnly = readBooleanArr(in);
		searchable = readBooleanArr(in);
		signed = readBooleanArr(in);
		writable = readBooleanArr(in);
		definitivelyWritable = readBooleanArr(in);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(columnCount);

		writeStringArr(catalogName, out);
		writeStringArr(schemaName, out);
		writeStringArr(tableName, out);
		writeStringArr(columnClassName, out);
		writeStringArr(columnLabel, out);
		writeStringArr(columnName, out);
		writeStringArr(columnTypeName, out);

		writeIntArr(columnType, out);
		writeIntArr(columnDisplaySize, out);
		writeIntArr(precision, out);
		writeIntArr(scale, out);
		writeIntArr(nullable, out);

		writeBooleanArr(autoIncrement, out);
		writeBooleanArr(caseSensitive, out);
		writeBooleanArr(currency, out);
		writeBooleanArr(readOnly, out);
		writeBooleanArr(searchable, out);
		writeBooleanArr(signed, out);
		writeBooleanArr(writable, out);
		writeBooleanArr(definitivelyWritable, out);
	}

	private void allocateArrays() {
		catalogName = new String[columnCount];
		schemaName = new String[columnCount];
		tableName = new String[columnCount];
		columnClassName = new String[columnCount];
		columnLabel = new String[columnCount];
		columnName = new String[columnCount];
		columnTypeName = new String[columnCount];

		columnDisplaySize = new Integer[columnCount];
		columnType = new Integer[columnCount];
		precision = new Integer[columnCount];
		scale = new Integer[columnCount];
		nullable = new Integer[columnCount];

		autoIncrement = new Boolean[columnCount];
		caseSensitive = new Boolean[columnCount];
		currency = new Boolean[columnCount];
		readOnly = new Boolean[columnCount];
		searchable = new Boolean[columnCount];
		signed = new Boolean[columnCount];
		writable = new Boolean[columnCount];
		definitivelyWritable = new Boolean[columnCount];
	}

	private void fillArrays(final ResultSetMetaData rsmd) {
		for (int i = 0; i < columnCount; i++) {
			final int col = i + 1;
			try {
				catalogName[i] = rsmd.getCatalogName(col);
			} catch (final Exception e) {
				catalogName[i] = null;
			}
			try {
				schemaName[i] = rsmd.getSchemaName(col);
			} catch (final Exception e1) {
				schemaName[i] = null;
			}
			try {
				tableName[i] = rsmd.getTableName(col);
			} catch (final Exception e2) {
				tableName[i] = null;
			}
			try {
				columnLabel[i] = rsmd.getColumnLabel(col);
			} catch (final Exception e3) {
				columnLabel[i] = null;
			}
			try {
				columnName[i] = rsmd.getColumnName(col);
			} catch (final Exception e4) {
				columnName[i] = null;
			}
			try {
				columnClassName[i] = rsmd.getColumnClassName(col);
			} catch (final Exception e5) {
				columnClassName[i] = null;
			}
			try {
				columnTypeName[i] = rsmd.getColumnTypeName(col);
			} catch (final Exception e6) {
				columnTypeName[i] = null;
			}
			try {
				columnDisplaySize[i] = Integer.valueOf(rsmd.getColumnDisplaySize(col));
			} catch (final Exception e7) {
				columnDisplaySize[i] = null;
			}
			try {
				columnType[i] = Integer.valueOf(rsmd.getColumnType(col));
			} catch (final Exception e8) {
				columnType[i] = null;
			}
			try {
				precision[i] = Integer.valueOf(rsmd.getPrecision(col));
			} catch (final Exception e9) {
				precision[i] = null;
			}

			try {
				scale[i] = Integer.valueOf(rsmd.getScale(col));
			} catch (final Exception e10) {
				scale[i] = null;
			}

			try {
				nullable[i] = Integer.valueOf(rsmd.isNullable(col));
			} catch (final Exception e11) {
				nullable[i] = null;
			}

			try {
				autoIncrement[i] = rsmd.isAutoIncrement(col) ? Boolean.TRUE : Boolean.FALSE;
			} catch (final Exception e12) {
				autoIncrement[i] = null;
			}

			try {
				caseSensitive[i] = rsmd.isCaseSensitive(col) ? Boolean.TRUE : Boolean.FALSE;
			} catch (final Exception e13) {
				caseSensitive[i] = null;
			}

			try {
				currency[i] = rsmd.isCurrency(col) ? Boolean.TRUE : Boolean.FALSE;
			} catch (final Exception e14) {
				currency[i] = null;
			}

			try {
				readOnly[i] = rsmd.isReadOnly(col) ? Boolean.TRUE : Boolean.FALSE;
			} catch (final Exception e15) {
				readOnly[i] = null;
			}

			try {
				searchable[i] = rsmd.isSearchable(col) ? Boolean.TRUE : Boolean.FALSE;
			} catch (final Exception e16) {
				searchable[i] = null;
			}

			try {
				signed[i] = rsmd.isSigned(col) ? Boolean.TRUE : Boolean.FALSE;
			} catch (final Exception e17) {
				signed[i] = null;
			}

			try {
				writable[i] = rsmd.isWritable(col) ? Boolean.TRUE : Boolean.FALSE;
			} catch (final Exception e18) {
				writable[i] = null;
			}

			try {
				definitivelyWritable[i] = rsmd.isDefinitelyWritable(col) ? Boolean.TRUE : Boolean.FALSE;
			} catch (final Exception e18) {
				definitivelyWritable[i] = null;
			}
		}
	}

	@Override
	public int getColumnCount() throws SQLException {
		return columnCount;
	}

	@Override
	public boolean isAutoIncrement(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(autoIncrement[column - 1]);
		return autoIncrement[column - 1].booleanValue();
	}

	@Override
	public boolean isCaseSensitive(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(caseSensitive[column - 1]);
		return caseSensitive[column - 1].booleanValue();
	}

	@Override
	public boolean isSearchable(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(searchable[column - 1]);
		return searchable[column - 1].booleanValue();
	}

	@Override
	public boolean isCurrency(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(currency[column - 1]);
		return currency[column - 1].booleanValue();
	}

	@Override
	public int isNullable(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(nullable[column - 1]);
		return nullable[column - 1].intValue();
	}

	@Override
	public boolean isSigned(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(signed[column - 1]);
		return signed[column - 1].booleanValue();
	}

	@Override
	public int getColumnDisplaySize(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(columnDisplaySize[column - 1]);
		return columnDisplaySize[column - 1].intValue();
	}

	@Override
	public String getColumnLabel(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(columnLabel[column - 1]);
		return columnLabel[column - 1];
	}

	@Override
	public String getColumnName(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(columnName[column - 1]);
		return columnName[column - 1];
	}

	@Override
	public String getSchemaName(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(schemaName[column - 1]);
		return schemaName[column - 1];
	}

	@Override
	public int getPrecision(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(precision[column - 1]);
		return precision[column - 1].intValue();
	}

	@Override
	public int getScale(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(scale[column - 1]);
		return scale[column - 1].intValue();
	}

	@Override
	public String getTableName(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(tableName[column - 1]);
		return tableName[column - 1];
	}

	@Override
	public String getCatalogName(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(catalogName[column - 1]);
		return catalogName[column - 1];
	}

	@Override
	public int getColumnType(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(columnType[column - 1]);
		return columnType[column - 1].intValue();
	}

	@Override
	public String getColumnTypeName(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(columnTypeName[column - 1]);
		return columnTypeName[column - 1];
	}

	@Override
	public boolean isReadOnly(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(readOnly[column - 1]);
		return readOnly[column - 1].booleanValue();
	}

	@Override
	public boolean isWritable(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(writable[column - 1]);
		return writable[column - 1].booleanValue();
	}

	@Override
	public boolean isDefinitelyWritable(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(definitivelyWritable[column - 1]);
		return definitivelyWritable[column - 1].booleanValue();
	}

	@Override
	public String getColumnClassName(final int column) throws SQLException {
		checkColumnIndex(column);
		throwIfNull(columnClassName[column - 1]);
		return columnClassName[column - 1];
	}

	public void setColumnCount(final int columnCount) throws SQLException {
		if (columnCount < 0) {
			throw new SQLException("invalid number of columns " + columnCount);
		}
		this.columnCount = columnCount;
		allocateArrays();
	}

	public void setAutoIncrement(final int columnIndex, final boolean property) throws SQLException {
		checkColumnIndex(columnIndex);
		autoIncrement[columnIndex - 1] = Boolean.valueOf(property);
	}

	public void setCaseSensitive(final int columnIndex, final boolean property) throws SQLException {
		checkColumnIndex(columnIndex);
		caseSensitive[columnIndex - 1] = Boolean.valueOf(property);
	}

	public void setCatalogName(final int columnIndex, final String catalogName) throws SQLException {
		checkColumnIndex(columnIndex);
		this.catalogName[columnIndex - 1] = catalogName;
	}

	public void setColumnDisplaySize(final int columnIndex, final int size) throws SQLException {
		checkColumnIndex(columnIndex);
		columnDisplaySize[columnIndex - 1] = Integer.valueOf(size);
	}

	public void setColumnLabel(final int columnIndex, final String label) throws SQLException {
		checkColumnIndex(columnIndex);
		columnLabel[columnIndex - 1] = label;
	}

	public void setColumnName(final int columnIndex, final String columnName) throws SQLException {
		checkColumnIndex(columnIndex);
		this.columnName[columnIndex - 1] = columnName;
	}

	public void setColumnType(final int columnIndex, final int SQLType) throws SQLException {
		checkColumnIndex(columnIndex);
		columnType[columnIndex - 1] = Integer.valueOf(SQLType);
	}

	public void setColumnTypeName(final int columnIndex, final String typeName) throws SQLException {
		checkColumnIndex(columnIndex);
		columnTypeName[columnIndex - 1] = typeName;
	}

	public void setCurrency(final int columnIndex, final boolean property) throws SQLException {
		checkColumnIndex(columnIndex);
		currency[columnIndex - 1] = Boolean.valueOf(property);
	}

	public void setNullable(final int columnIndex, final int property) throws SQLException {
		checkColumnIndex(columnIndex);
		nullable[columnIndex - 1] = Integer.valueOf(property);
	}

	public void setPrecision(final int columnIndex, final int precision) throws SQLException {
		checkColumnIndex(columnIndex);
		this.precision[columnIndex - 1] = Integer.valueOf(precision);
	}

	public void setScale(final int columnIndex, final int scale) throws SQLException {
		checkColumnIndex(columnIndex);
		this.scale[columnIndex - 1] = Integer.valueOf(scale);
	}

	public void setSchemaName(final int columnIndex, final String schemaName) throws SQLException {
		checkColumnIndex(columnIndex);
		this.schemaName[columnIndex - 1] = schemaName;
	}

	public void setSearchable(final int columnIndex, final boolean property) throws SQLException {
		checkColumnIndex(columnIndex);
		searchable[columnIndex - 1] = Boolean.valueOf(property);
	}

	public void setSigned(final int columnIndex, final boolean property) throws SQLException {
		checkColumnIndex(columnIndex);
		signed[columnIndex - 1] = Boolean.valueOf(property);
	}

	public void setTableName(final int columnIndex, final String tableName) throws SQLException {
		checkColumnIndex(columnIndex);
		this.tableName[columnIndex - 1] = tableName;
	}

	public void setReadOnly(final int columnIndex, final boolean readOnly) throws SQLException {
		checkColumnIndex(columnIndex);
		this.readOnly[columnIndex - 1] = Boolean.valueOf(readOnly);
	}

	public void setWritable(final int columnIndex, final boolean writable) throws SQLException {
		checkColumnIndex(columnIndex);
		this.writable[columnIndex - 1] = Boolean.valueOf(writable);
	}

	public void setDefinitelyWritable(final int columnIndex, final boolean writable) throws SQLException {
		checkColumnIndex(columnIndex);
		definitivelyWritable[columnIndex - 1] = Boolean.valueOf(writable);
	}

	public void setColumnClassName(final int columnIndex, final String columnClassName) throws SQLException {
		checkColumnIndex(columnIndex);
		this.columnClassName[columnIndex - 1] = columnClassName;
	}

	private static void throwIfNull(final Object obj) throws SQLException {
		if (obj == null) {
			throw new SQLException("Method not supported");
		}
	}

	private void checkColumnIndex(final int columnIndex) throws SQLException {
		if (columnIndex < 1 || columnIndex > columnCount) {
			throw new SQLException("invalid column index " + columnIndex);
		}
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return iface.isAssignableFrom(SerialResultSetMetaData.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return (T) this;
	}
}
