package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DoubleParameter implements PreparedStatementParameter {
	private static final long	serialVersionUID	= -8304299062026994797L;

	private double					value;

	public DoubleParameter() {
		// empty
	}

	public DoubleParameter(final double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = in.readDouble();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeDouble(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setDouble(index, value);
	}

	@Override
	public String toString() {
		return "double: " + value;
	}
}
