package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.SerialBlob;

public class CallableStatementGetBlobCommand extends BaseCommand<Blob, CallableStatement> {

	private static final long	serialVersionUID	= -2976001646644624286L;

	private int						index;
	private String					parameterName;

	public CallableStatementGetBlobCommand() {
		// empty
	}

	public CallableStatementGetBlobCommand(final int index) {
		this.index = index;
	}

	public CallableStatementGetBlobCommand(final String paramName) {
		this.parameterName = paramName;
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
	public Blob execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		Blob result;
		if (parameterName != null) {
			result = cstmt.getBlob(parameterName);
		} else {
			result = cstmt.getBlob(index);
		}
		return new SerialBlob(result);
	}
}
