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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + (parameterName == null ? 0 : parameterName.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CallableStatementGetBlobCommand other = (CallableStatementGetBlobCommand) obj;
		if (index != other.index) {
			return false;
		}
		if (parameterName == null) {
			if (other.parameterName != null) {
				return false;
			}
		} else if (!parameterName.equals(other.parameterName)) {
			return false;
		}
		return true;
	}


}
