package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.SerialClob;

public class ClobParameter implements PreparedStatementParameter {
	private static final long	serialVersionUID	= -8231456859022053216L;

	private SerialClob			value;

	public ClobParameter() {
		// empty
	}

	public ClobParameter(final Clob value) throws SQLException {
		this.value = new SerialClob(value);
	}

	public SerialClob getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (SerialClob) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setClob(index, value);
	}

	@Override
	public String toString() {
		return "Clob: " + value;
	}
}
