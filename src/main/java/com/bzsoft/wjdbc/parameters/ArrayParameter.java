package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.SerialArray;

public class ArrayParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 82417815012404533L;

	private SerialArray			value;

	public ArrayParameter() {
		// empty
	}

	public ArrayParameter(final Array value) throws SQLException {
		this.value = new SerialArray(value);
	}

	public SerialArray getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (SerialArray) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setArray(index, value);
	}

	@Override
	public String toString() {
		return "Array: " + value;
	}
}
