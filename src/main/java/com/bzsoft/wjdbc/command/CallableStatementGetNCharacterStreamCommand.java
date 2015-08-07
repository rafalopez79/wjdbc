package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.StreamSerializer;
import com.bzsoft.wjdbc.util.StreamCloser;

public class CallableStatementGetNCharacterStreamCommand extends BaseCommand<char[], CallableStatement> {

	private static final long	serialVersionUID	= -8218845136435435097L;

	private int						index;
	private String					parameterName;

	public CallableStatementGetNCharacterStreamCommand() {
		// Empty
	}

	public CallableStatementGetNCharacterStreamCommand(final int index) {
		this.index = index;
	}

	public CallableStatementGetNCharacterStreamCommand(final String paramName) {
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

	@SuppressWarnings("resource")
	@Override
	public char[] execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		Reader result;
		if (parameterName != null) {
			result = cstmt.getNCharacterStream(parameterName);
		} else {
			result = cstmt.getNCharacterStream(index);
		}
		try {
			// read reader and return as a char[]
			return StreamSerializer.toCharArray(result);
		} catch (final IOException ioe) {
			throw new SQLException(ioe);
		}finally{
			StreamCloser.close(result);
		}
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
		final CallableStatementGetNCharacterStreamCommand other = (CallableStatementGetNCharacterStreamCommand) obj;
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
