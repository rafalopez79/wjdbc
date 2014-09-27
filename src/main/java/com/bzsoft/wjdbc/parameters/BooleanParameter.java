package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BooleanParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 1915488329736405680L;

	private boolean				value;

	public BooleanParameter() {
		// empty
	}

	public BooleanParameter(final boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = in.readBoolean();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeBoolean(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setBoolean(index, value);
	}

	@Override
	public String toString() {
		return "boolean: " + Boolean.toString(value);
	}
}
