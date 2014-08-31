// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.Map;

public class SerialRef implements Ref, Externalizable {

	private static final long	serialVersionUID	= -9145419222061515405L;

	private String					baseTypeName;
	private Object					javaObject;

	public SerialRef(final Ref ref) throws SQLException {
		baseTypeName = ref.getBaseTypeName();
		javaObject = ref.getObject();
	}

	@Override
	public String getBaseTypeName() throws SQLException {
		return baseTypeName;
	}

	@Override
	public Object getObject(final Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException("Ref.getObject(Map) not supported");
	}

	@Override
	public Object getObject() throws SQLException {
		return javaObject;
	}

	@Override
	public void setObject(final Object value) throws SQLException {
		throw new UnsupportedOperationException("Ref.setObject(Object) not supported");
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeUTF(baseTypeName);
		out.writeObject(javaObject);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		baseTypeName = in.readUTF();
		javaObject = in.readObject();
	}
}
