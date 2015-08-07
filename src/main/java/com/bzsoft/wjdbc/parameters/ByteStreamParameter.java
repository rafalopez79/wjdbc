package com.bzsoft.wjdbc.parameters;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bzsoft.wjdbc.util.StreamCloser;

public class ByteStreamParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= 8868161011164192986L;

	public static final int		TYPE_ASCII			= 1;
	public static final int		TYPE_UNICODE		= 2;
	public static final int		TYPE_BINARY			= 3;

	private int						type;
	private byte[]					value;
	private long					length;

	public ByteStreamParameter() {
		// empty
	}

	@SuppressWarnings("resource")
	public ByteStreamParameter(final int type, final InputStream x, final long length) throws SQLException {
		this.type = type;
		this.length = length;
		final BufferedInputStream s = new BufferedInputStream(x);
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream((int) (length >= 0 ? length : 1024));
			final byte buf[] = new byte[1024];
			int br;
			while ((br = s.read(buf)) >= 0) {
				if (br > 0) {
					bos.write(buf, 0, br);
				}
			}
			this.value = bos.toByteArray();
			// Adjust length to the amount of read bytes if the user provided
			// -1 as the length parameter
			if (this.length < 0) {
				this.length = this.value.length;
			}
		} catch (final IOException e) {
			throw new SQLException("InputStream conversion to byte-array failed");
		} finally {
			StreamCloser.close(s);
		}
	}

	public byte[] getValue() {
		return this.value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		this.type = in.readInt();
		this.value = (byte[]) in.readObject();
		this.length = in.readLong();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(type);
		out.writeObject(value);
		out.writeLong(length);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(value);
		switch (type) {
		case TYPE_ASCII:
			pstmt.setAsciiStream(index, bais, length);
			break;
		case TYPE_UNICODE:
			// its ok to downcast here as there is no setUnicodeStream()
			// variant with a long length value
			pstmt.setUnicodeStream(index, bais, (int) length);
			break;
		case TYPE_BINARY:
			pstmt.setBinaryStream(index, bais, length);
			break;
		}
	}

	@Override
	public String toString() {
		return "ByteStream: " + length + " bytes";
	}
}
