// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.SerialBlob;


public class BlobParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 7120087686097706094L;

	private SerialBlob			value;

	public BlobParameter() {
		// empty
	}

	public BlobParameter(final Blob value) throws SQLException {
		this.value = new SerialBlob(value);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (SerialBlob) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setBlob(index, value);
	}

	@Override
	public String toString() {
		return "Blob: " + value;
	}
}
