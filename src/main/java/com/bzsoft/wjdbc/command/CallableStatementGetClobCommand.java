// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.SerialClob;


public class CallableStatementGetClobCommand extends BaseCommand<Clob, CallableStatement> {

	private static final long	serialVersionUID	= 8230491873823084213L;

	private int						index;
	private String					parameterName;

	public CallableStatementGetClobCommand() {
	}

	public CallableStatementGetClobCommand(final int index) {
		this.index = index;
	}

	public CallableStatementGetClobCommand(final String paramName) {
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
	public Clob execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		final Clob result;
		if (parameterName != null) {
			result = cstmt.getClob(parameterName);
		} else {
			result = cstmt.getClob(index);
		}
		return new SerialClob(result);
	}
}
