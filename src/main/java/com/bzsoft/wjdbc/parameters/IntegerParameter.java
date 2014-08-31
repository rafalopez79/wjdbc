// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

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
}
