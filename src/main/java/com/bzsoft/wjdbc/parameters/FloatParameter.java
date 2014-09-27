package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FloatParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= -2273786408954216402L;

	private float					value;

	public FloatParameter() {
		// empty
	}

	public FloatParameter(final float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = in.readFloat();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeFloat(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setFloat(index, value);
	}

	@Override
	public String toString() {
		return "float: " + value;
	}
}
