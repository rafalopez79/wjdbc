package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Arrays;

import com.bzsoft.wjdbc.serial.StreamSerializer;

public class CallableStatementSetCharacterStreamCommand extends BaseCommand<Void, CallableStatement> {
	private static final long	serialVersionUID	= 8952810867158345906L;

	private int						index;
	private int						length;
	private String					parameterName;
	private char[]					charArray;

	public CallableStatementSetCharacterStreamCommand() {
		// empty
	}

	public CallableStatementSetCharacterStreamCommand(final int index, final Reader reader) throws IOException {
		this.index = index;
		charArray = StreamSerializer.toCharArray(reader);
		length = charArray.length;
	}

	public CallableStatementSetCharacterStreamCommand(final String paramName, final Reader reader) throws IOException {
		parameterName = paramName;
		charArray = StreamSerializer.toCharArray(reader);
		length = charArray.length;
	}

	public CallableStatementSetCharacterStreamCommand(final int index, final Reader reader, final int len) throws IOException {
		this.index = index;
		length = len;
		charArray = StreamSerializer.toCharArray(reader, len);
	}

	public CallableStatementSetCharacterStreamCommand(final String paramName, final Reader reader, final int len) throws IOException {
		parameterName = paramName;
		length = len;
		charArray = StreamSerializer.toCharArray(reader, len);
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
		out.writeObject(charArray);
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
		charArray = (char[]) in.readObject();
	}

	@Override
	public Void execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		final Reader reader = StreamSerializer.toReader(charArray);
		if (parameterName != null) {
			cstmt.setCharacterStream(parameterName, reader, length);
		} else {
			cstmt.setCharacterStream(index, reader, length);
		}

		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(charArray);
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
		final CallableStatementSetCharacterStreamCommand other = (CallableStatementSetCharacterStreamCommand) obj;
		if (!Arrays.equals(charArray, other.charArray)) {
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
