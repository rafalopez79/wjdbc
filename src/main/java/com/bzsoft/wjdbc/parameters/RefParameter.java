// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.SerialRef;


public class RefParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 8647675527971168478L;

	private SerialRef				value;

	public RefParameter() {
		// empty
	}

	public RefParameter(final Ref value) throws SQLException {
		this.value = new SerialRef(value);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (SerialRef) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setRef(index, value);
	}

	@Override
	public String toString() {
		return "Ref: " + value;
	}
}
