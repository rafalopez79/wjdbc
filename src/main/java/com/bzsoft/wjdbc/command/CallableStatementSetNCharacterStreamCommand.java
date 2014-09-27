package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.StreamSerializer;

public class CallableStatementSetNCharacterStreamCommand extends BaseCommand<Void, CallableStatement> {
	private static final long	serialVersionUID	= 8952810867158345906L;

	private int						index;
	private int						length;
	private String					parameterName;
	private char[]					charArray;

	public CallableStatementSetNCharacterStreamCommand() {
		// empty
	}

	public CallableStatementSetNCharacterStreamCommand(final int index, final Reader reader) throws IOException {
		this.index = index;
		charArray = StreamSerializer.toCharArray(reader);
		length = charArray.length;
	}

	public CallableStatementSetNCharacterStreamCommand(final String paramName, final Reader reader) throws IOException {
		parameterName = paramName;
		charArray = StreamSerializer.toCharArray(reader);
		length = charArray.length;
	}

	public CallableStatementSetNCharacterStreamCommand(final int index, final Reader reader, final int len) throws IOException {
		this.index = index;
		length = len;
		charArray = StreamSerializer.toCharArray(reader, len);
	}

	public CallableStatementSetNCharacterStreamCommand(final String paramName, final Reader reader, final int len) throws IOException {
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
			cstmt.setNCharacterStream(parameterName, reader, length);
		} else {
			cstmt.setNCharacterStream(index, reader, length);
		}

		return null;
	}
}
