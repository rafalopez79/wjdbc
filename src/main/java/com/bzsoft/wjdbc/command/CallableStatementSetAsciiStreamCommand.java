package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Arrays;

import com.bzsoft.wjdbc.serial.StreamSerializer;

public class CallableStatementSetAsciiStreamCommand extends BaseCommand<Void, CallableStatement> {
	private static final long	serialVersionUID	= -6772875360380241530L;

	private int						index;
	private int						length;
	private String					parameterName;
	private byte[]					byteArray;

	public CallableStatementSetAsciiStreamCommand() {
		// empty
	}

	public CallableStatementSetAsciiStreamCommand(final int index, final InputStream is) throws IOException {
		this.index = index;
		byteArray = StreamSerializer.toByteArray(is);
		length = byteArray.length;
	}

	public CallableStatementSetAsciiStreamCommand(final String paramName, final InputStream is) throws IOException {
		parameterName = paramName;
		byteArray = StreamSerializer.toByteArray(is);
		length = byteArray.length;
	}

	public CallableStatementSetAsciiStreamCommand(final int index, final InputStream is, final int len) throws IOException {
		this.index = index;
		length = len;
		byteArray = StreamSerializer.toByteArray(is);
	}

	public CallableStatementSetAsciiStreamCommand(final String paramName, final InputStream is, final int len) throws IOException {
		parameterName = paramName;
		length = len;
		byteArray = StreamSerializer.toByteArray(is);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(index);
		out.writeInt(length);
		if (parameterName == null) {
			out.writeBoolean(false);
		} else {
			out.writeBoolean(true);
			out.writeUTF(parameterName);
		}
		out.writeObject(byteArray);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		index = in.readInt();
		length = in.readInt();
		final boolean isNotNull = in.readBoolean();
		if (isNotNull) {
			parameterName = in.readUTF();
		} else {
			parameterName = null;
		}
		byteArray = (byte[]) in.readObject();
	}

	@Override
	public Void execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		final InputStream is = StreamSerializer.toInputStream(byteArray);
		if (parameterName != null) {
			cstmt.setAsciiStream(parameterName, is, length);
		} else {
			cstmt.setAsciiStream(index, is, length);
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(byteArray);
		result = prime * result + index;
		result = prime * result + length;
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
		final CallableStatementSetAsciiStreamCommand other = (CallableStatementSetAsciiStreamCommand) obj;
		if (!Arrays.equals(byteArray, other.byteArray)) {
			return false;
		}
		if (index != other.index) {
			return false;
		}
		if (length != other.length) {
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
