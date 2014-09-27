package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ByteArrayParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= -850921577178865335L;

	private byte[]					value;

	public ByteArrayParameter() {
		// empty
	}

	public ByteArrayParameter(final byte[] value) {
		this.value = value;
	}

	public byte[] getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (byte[]) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setBytes(index, value);
	}

	@Override
	public String toString() {
		return "byte[]: " + value.length + " bytes";
	}
}
