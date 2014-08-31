// VJDBC - Virtual JDBC
// Written by Hunter Payne
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.CallableStatement;
import java.sql.NClob;
import java.sql.SQLException;

public class CallableStatementSetNClobCommand extends BaseCommand<Void, CallableStatement> {
	private static final long	serialVersionUID	= 4264932633701227941L;

	private int						index;
	private String					parameterName;
	private NClob					clob;

	public CallableStatementSetNClobCommand() {
	}

	public CallableStatementSetNClobCommand(final int index, final NClob clob) throws IOException {
		this.index = index;
		this.clob = clob;
	}

	public CallableStatementSetNClobCommand(final String paramName, final NClob clob) throws IOException {
		parameterName = paramName;
		this.clob = clob;
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
		out.writeObject(clob);
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
		clob = (NClob) in.readObject();
	}

	@Override
	public Void execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		if (parameterName != null) {
			cstmt.setNClob(parameterName, clob);
		} else {
			cstmt.setNClob(index, clob);
		}

		return null;
	}
}
