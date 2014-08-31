// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.SerialArray;


public class CallableStatementGetArrayCommand extends BaseCommand<Array, CallableStatement> {

	private static final long	serialVersionUID	= 4247967467888689853L;

	private int						index;
	private String					parameterName;

	// No-Arg constructor for deserialization
	public CallableStatementGetArrayCommand() {
		// empty
	}

	public CallableStatementGetArrayCommand(final int index) {
		this.index = index;
	}

	public CallableStatementGetArrayCommand(final String paramName) {
		parameterName = paramName;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(index);
		if (parameterName == null) {
			out.writeBoolean(false);
		} else {
			out.writeBoolean(true);
			out.writeUTF(parameterName);
		}
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		index = in.readInt();
		final boolean isNotNull = in.readBoolean();
		if (isNotNull) {
			parameterName = in.readUTF();
		} else {
			parameterName = null;
		}
	}

	@Override
	public Array execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		Array result;
		if (parameterName != null) {
			result = cstmt.getArray(parameterName);
		} else {
			result = cstmt.getArray(index);
		}
		return new SerialArray(result);
	}
}
