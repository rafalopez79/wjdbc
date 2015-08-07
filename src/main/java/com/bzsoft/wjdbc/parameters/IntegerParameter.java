package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntegerParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 7906650418670821329L;

	private int						value;

	public IntegerParameter() {
		// empty
	}

	public IntegerParameter(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = in.readInt();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setInt(index, value);
	}

	@Override
	public String toString() {
		return "int: " + value;
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
		final IntegerParameter other = (IntegerParameter) obj;
		if (value != other.value) {
			return false;
		}
		return true;
	}


}
