package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.CallableStatement;
import java.sql.Ref;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.SerialRef;

public class CallableStatementGetRefCommand extends BaseCommand<Ref, CallableStatement> {

	private static final long	serialVersionUID	= 6253579473434177231L;

	private int						index;
	private String					parameterName;

	public CallableStatementGetRefCommand() {
		// empty
	}

	public CallableStatementGetRefCommand(final int index) {
		this.index = index;
		parameterName = null;
	}

	public CallableStatementGetRefCommand(final String paramName) {
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
	public Ref execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		Ref result;
		if (parameterName != null) {
			result = cstmt.getRef(parameterName);
		} else {
			result = cstmt.getRef(index);
		}
		return new SerialRef(result);
	}
}
