package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= -8131525145406357230L;

	private String					value;

	public StringParameter() {
		// empty
	}

	public StringParameter(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		final boolean bool = in.readBoolean();
		if (bool) {
			value = null;
		} else {
			value = in.readUTF();
		}
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		if (value == null) {
			out.writeBoolean(true);
		} else {
			out.writeBoolean(false);
			out.writeUTF(value);
		}
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setString(index, value);
	}

	@Override
	public String toString() {
		return "String: " + value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (value == null ? 0 : value.hashCode());
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
		final StringParameter other = (StringParameter) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}


}
