package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShortParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 5384886497454301576L;

	private short					value;

	public ShortParameter() {
		// empty
	}

	public ShortParameter(final short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = in.readShort();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeShort(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setShort(index, value);
	}

	@Override
	public String toString() {
		return "short: " + value;
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
		final ShortParameter other = (ShortParameter) obj;
		if (value != other.value) {
			return false;
		}
		return true;
	}

}
