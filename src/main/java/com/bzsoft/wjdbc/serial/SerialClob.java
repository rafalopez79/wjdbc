// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.serial;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

import com.bzsoft.wjdbc.util.SQLExceptionHelper;

public class SerialClob implements Clob, Externalizable {

	private static final long	serialVersionUID	= 3904682695287452212L;

	protected char[]				data;

	public SerialClob() {
	}

	public SerialClob(final Clob other) throws SQLException {
		try {
			final StringBuilder sw = new StringBuilder();
			final Reader rd = other.getCharacterStream();
			final char[] buff = new char[1024];
			int len;
			while ((len = rd.read(buff)) > 0) {
				sw.append(buff, 0, len);
			}
			data = sw.toString().toCharArray();
			other.free();
		} catch (final IOException e) {
			throw new SQLException("Can't retrieve contents of Clob", e.toString());
		}
	}

	public SerialClob(final Reader rd) throws SQLException {
		try {
			init(rd);
		} catch (final IOException e) {
			throw new SQLException("Can't retrieve contents of Clob", e.toString());
		}
	}

	public SerialClob(final Reader rd, final long length) throws SQLException {
		try {
			init(rd, length);
		} catch (final IOException e) {
			throw new SQLException("Can't retrieve contents of Clob", e.toString());
		}
	}

	public void init(final Reader rd) throws IOException {
		final StringBuilder sw = new StringBuilder();
		final char[] buff = new char[1024];
		int len;
		while ((len = rd.read(buff)) > 0) {
			sw.append(buff, 0, len);
		}
		data = sw.toString().toCharArray();
	}

	public void init(final Reader rd, final long length) throws IOException {
		final StringBuilder sw = new StringBuilder();
		final char[] buff = new char[1024];
		int len;
		long toRead = length;
		while (toRead > 0 && (len = rd.read(buff, 0, (int) (toRead > 1024 ? 1024 : toRead))) > 0) {
			sw.append(buff, 0, len);
			toRead -= len;
		}
		data = sw.toString().toCharArray();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(data);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		data = (char[]) in.readObject();
	}

	@Override
	public long length() throws SQLException {
		return data.length;
	}

	@Override
	public String getSubString(final long pos, final int length) throws SQLException {
		if (pos <= Integer.MAX_VALUE) {
			return new String(data, (int) pos - 1, length);
		}
		// very slow but gets around problems with the pos being represented
		// as long instead of an int in most java.io and other byte copying
		// APIs
		final CharArrayWriter writer = new CharArrayWriter(length);
		for (long i = 0; i < length; ++i) {
			writer.write(data[(int) (pos + i)]);
		}
		return writer.toString();
	}

	@Override
	public Reader getCharacterStream() throws SQLException {
		return new StringReader(new String(data));
	}

	@Override
	public InputStream getAsciiStream() throws SQLException {
		try {
			return new ByteArrayInputStream(new String(data).getBytes("US-ASCII"));
		} catch (final UnsupportedEncodingException e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public long position(final String searchstr, final long start) throws SQLException {
		throw new UnsupportedOperationException("SerialClob.position");
	}

	@Override
	public long position(final Clob searchstr, final long start) throws SQLException {
		throw new UnsupportedOperationException("SerialClob.position");
	}

	@Override
	public int setString(final long pos, final String str) throws SQLException {
		throw new UnsupportedOperationException("SerialClob.setString");
	}

	@Override
	public int setString(final long pos, final String str, final int offset, final int len) throws SQLException {
		throw new UnsupportedOperationException("SerialClob.setString");
	}

	@Override
	public OutputStream setAsciiStream(final long pos) throws SQLException {
		throw new UnsupportedOperationException("SerialClob.setAsciiStream");
	}

	@Override
	public Writer setCharacterStream(final long pos) throws SQLException {
		throw new UnsupportedOperationException("SerialClob.setCharacterStream");
	}

	@Override
	public void truncate(final long len) throws SQLException {
		throw new UnsupportedOperationException("SerialClob.truncate");
	}

	@Override
	public Reader getCharacterStream(final long pos, final long length) throws SQLException {
		if (pos <= Integer.MAX_VALUE && length <= Integer.MAX_VALUE) {
			return new CharArrayReader(data, (int) pos, (int) length);
		}
		// very slow but gets around problems with the pos being represented
		// as long instead of an int in most java.io and other byte copying
		// APIs
		final CharArrayWriter writer = new CharArrayWriter((int) length);
		for (long i = 0; i < length; ++i) {
			writer.write(data[(int) (pos + i)]);
		}
		return new CharArrayReader(writer.toCharArray());
	}

	@Override
	public void free() throws SQLException {
		data = null;
	}
}
