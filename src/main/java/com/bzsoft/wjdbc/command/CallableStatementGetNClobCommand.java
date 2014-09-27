package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.CallableStatement;
import java.sql.NClob;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.SerialNClob;

public class CallableStatementGetNClobCommand extends BaseCommand<NClob, CallableStatement> {

	private static final long	serialVersionUID	= 8230491873823084213L;

	private int						index;
	private String					parameterName;

	public CallableStatementGetNClobCommand() {
		// Empty
	}

	public CallableStatementGetNClobCommand(final int index) {
		this.index = index;
	}

	public CallableStatementGetNClobCommand(final String paramName) {
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
	public NClob execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		NClob result;
		if (parameterName != null) {
			result = cstmt.getNClob(parameterName);
		} else {
			result = cstmt.getNClob(index);
		}
		return new SerialNClob(result);
	}
}
