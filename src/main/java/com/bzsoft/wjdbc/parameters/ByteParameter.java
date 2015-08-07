package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ByteParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= -6844809323174032034L;

	private byte					value;

	public ByteParameter() {
		// empty
	}

	public ByteParameter(final byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = in.readByte();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeByte(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setByte(index, value);
	}

	@Override
	public String toString() {
		return "byte: " + value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ByteParameter other = (ByteParameter) obj;
		if (value != other.value) {
			return false;
		}
		return true;
	}


}
