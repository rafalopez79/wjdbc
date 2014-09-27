package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.RowId;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.SerialRowId;

public class RowIdParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 8647675527971168478L;

	private SerialRowId			value;

	public RowIdParameter() {
		// empty
	}

	public RowIdParameter(final RowId value) throws SQLException {
		this.value = new SerialRowId(value);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (SerialRowId) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setRowId(index, value);
	}

	@Override
	public String toString() {
		return "RowId: " + value;
	}
}
