package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NStringParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 3458761646106361472L;

	private String					value;

	public NStringParameter() {
		// empty
	}

	public NStringParameter(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = in.readUTF();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeUTF(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setNString(index, value);
	}

	@Override
	public String toString() {
		return "NString: " + value;
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
		final NStringParameter other = (NStringParameter) obj;
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
