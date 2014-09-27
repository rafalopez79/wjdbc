package com.bzsoft.wjdbc.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import java.util.Map;

public class SerialArray implements Array, Externalizable {

	private static final long	serialVersionUID	= 3256722892212418873L;

	private int						baseType;
	private String					baseTypeName;
	private Object					array;

	public SerialArray() {
		// empty
	}

	public SerialArray(final int baseType, final String typeName, final Object[] elements) {
		this.baseType = baseType;
		this.baseTypeName = typeName;
		this.array = elements;
	}

	public SerialArray(final String typeName, final Object[] elements) {
		if ("array".equalsIgnoreCase(typeName)) {
			baseType = Types.ARRAY;
		} else if ("bigint".equalsIgnoreCase(typeName)) {
			baseType = Types.BIGINT;
		} else if ("binary".equalsIgnoreCase(typeName)) {
			baseType = Types.BINARY;
		} else if ("bit".equalsIgnoreCase(typeName)) {
			baseType = Types.BIT;
		} else if ("blob".equalsIgnoreCase(typeName)) {
			baseType = Types.BLOB;
		} else if ("boolean".equalsIgnoreCase(typeName)) {
			baseType = Types.BOOLEAN;
		} else if ("char".equalsIgnoreCase(typeName)) {
			baseType = Types.CHAR;
		} else if ("clob".equalsIgnoreCase(typeName)) {
			baseType = Types.CLOB;
		} else if ("datalink".equalsIgnoreCase(typeName)) {
			baseType = Types.DATALINK;
		} else if ("date".equalsIgnoreCase(typeName)) {
			baseType = Types.DATE;
		} else if ("decimal".equalsIgnoreCase(typeName)) {
			baseType = Types.DECIMAL;
		} else if ("distinct".equalsIgnoreCase(typeName)) {
			baseType = Types.DISTINCT;
		} else if ("double".equalsIgnoreCase(typeName)) {
			baseType = Types.DOUBLE;
		} else if ("float".equalsIgnoreCase(typeName)) {
			baseType = Types.FLOAT;
		} else if ("integer".equalsIgnoreCase(typeName)) {
			baseType = Types.INTEGER;
		} else if ("java_object".equalsIgnoreCase(typeName)) {
			baseType = Types.JAVA_OBJECT;
		} else if ("longnvarchar".equalsIgnoreCase(typeName)) {
			baseType = Types.LONGNVARCHAR;
		} else if ("longvarbinary".equalsIgnoreCase(typeName)) {
			baseType = Types.LONGVARBINARY;
		} else if ("longvarchar".equalsIgnoreCase(typeName)) {
			baseType = Types.LONGVARCHAR;
		} else if ("nchar".equalsIgnoreCase(typeName)) {
			baseType = Types.NCHAR;
		} else if ("nclob".equalsIgnoreCase(typeName)) {
			baseType = Types.NCLOB;
		} else if ("null".equalsIgnoreCase(typeName)) {
			baseType = Types.NULL;
		} else if ("numeric".equalsIgnoreCase(typeName)) {
			baseType = Types.NUMERIC;
		} else if ("nvarchar".equalsIgnoreCase(typeName)) {
			baseType = Types.NVARCHAR;
		} else if ("other".equalsIgnoreCase(typeName)) {
			baseType = Types.OTHER;
		} else if ("real".equalsIgnoreCase(typeName)) {
			baseType = Types.REAL;
		} else if ("ref".equalsIgnoreCase(typeName)) {
			baseType = Types.REF;
		} else if ("rowid".equalsIgnoreCase(typeName)) {
			baseType = Types.ROWID;
		} else if ("smallint".equalsIgnoreCase(typeName)) {
			baseType = Types.SMALLINT;
		} else if ("sqlxml".equalsIgnoreCase(typeName)) {
			baseType = Types.SQLXML;
		} else if ("struct".equalsIgnoreCase(typeName)) {
			baseType = Types.STRUCT;
		} else if ("time".equalsIgnoreCase(typeName)) {
			baseType = Types.TIME;
		} else if ("timestamp".equalsIgnoreCase(typeName)) {
			baseType = Types.TIMESTAMP;
		} else if ("tinyint".equalsIgnoreCase(typeName)) {
			baseType = Types.TINYINT;
		} else if ("boolean".equalsIgnoreCase(typeName)) {
			baseType = Types.BOOLEAN;
		} else if ("varbinary".equalsIgnoreCase(typeName)) {
			baseType = Types.VARBINARY;
		} else if ("varchar".equalsIgnoreCase(typeName)) {
			baseType = Types.VARCHAR;
		}
		baseTypeName = typeName;
		array = elements;
	}

	public SerialArray(final Array arr) throws SQLException {
		baseType = arr.getBaseType();
		baseTypeName = arr.getBaseTypeName();
		array = arr.getArray();
		if (baseType == Types.STRUCT) {
			final Object[] orig = (Object[]) array;
			final Struct[] cpy = new SerialStruct[orig.length];
			for (int i = 0; i < orig.length; i++) {
				cpy[i] = new SerialStruct((Struct) orig[i]);
			}
			array = cpy;
		}
		arr.free();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(baseType);
		out.writeUTF(baseTypeName);
		out.writeObject(array);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		baseType = in.readInt();
		baseTypeName = in.readUTF();
		array = in.readObject();
	}

	/* start JDBC4 support */
	@Override
	public void free() throws SQLException {
		array = null;
	}

	/* end JDBC4 support */
	@Override
	public String getBaseTypeName() throws SQLException {
		return baseTypeName;
	}

	@Override
	public int getBaseType() throws SQLException {
		return baseType;
	}

	@Override
	public Object getArray() throws SQLException {
		return array;
	}

	@Override
	public Object getArray(final Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException("getArray(Map) not supported");
	}

	@Override
	public Object getArray(final long index, final int count) throws SQLException {
		throw new UnsupportedOperationException("getArray(index, count) not supported");
	}

	@Override
	public Object getArray(final long index, final int count, final Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException("getArray(index, count, Map) not supported");
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		throw new UnsupportedOperationException("getResultSet() not supported");
	}

	@Override
	public ResultSet getResultSet(final Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException("getResultSet(Map) not supported");
	}

	@Override
	public ResultSet getResultSet(final long index, final int count) throws SQLException {
		throw new UnsupportedOperationException("getResultSet(index, count) not supported");
	}

	@Override
	public ResultSet getResultSet(final long index, final int count, final Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException("getResultSet(index, count, Map) not supported");
	}
}
