// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Map;

public class SerialStruct implements Struct, Externalizable {

	private static final long	serialVersionUID	= 3256444694312792625L;

	private String					sqlTypeName;
	private Object[]				attributes;

	public SerialStruct() {
		// Empty
	}

	public SerialStruct(final String typeName, final Object[] attr) {
		sqlTypeName = typeName;
		attributes = attr;
	}

	public SerialStruct(final Struct struct) throws SQLException {
		sqlTypeName = struct.getSQLTypeName();
		attributes = struct.getAttributes();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeUTF(sqlTypeName);
		out.writeObject(attributes);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		sqlTypeName = in.readUTF();
		attributes = (Object[]) in.readObject();
	}

	@Override
	public String getSQLTypeName() throws SQLException {
		return sqlTypeName;
	}

	@Override
	public Object[] getAttributes() throws SQLException {
		return attributes;
	}

	@Override
	public Object[] getAttributes(final Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException("getAttributes(Map)");
	}
}
