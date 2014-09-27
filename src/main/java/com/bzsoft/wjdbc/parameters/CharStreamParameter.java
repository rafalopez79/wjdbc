package com.bzsoft.wjdbc.parameters;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.StreamSerializer;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;

public class CharStreamParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= -3934051486806729706L;

	private char[]					value;

	public CharStreamParameter() {
		// empty
	}

	public CharStreamParameter(final Reader x) throws SQLException {
		try {
			value = StreamSerializer.toCharArray(x);
		} catch (final IOException e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	public CharStreamParameter(final Reader x, final long length) throws SQLException {
		try {
			value = StreamSerializer.toCharArray(x, length);
		} catch (final IOException e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	public char[] getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (char[]) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setCharacterStream(index, new CharArrayReader(value), value.length);
	}

	@Override
	public String toString() {
		return "CharStream: " + value.length + " chars";
	}
}
