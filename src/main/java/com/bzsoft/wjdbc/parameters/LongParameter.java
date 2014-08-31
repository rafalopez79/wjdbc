// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LongParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 2047115344356276027L;

	private long					value;

	public LongParameter() {
		// empty
	}

	public LongParameter(final long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = in.readLong();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeLong(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setLong(index, value);
	}

	@Override
	public String toString() {
		return "long: " + value;
	}
}
