package com.bzsoft.wjdbc.serial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class SerialBlob implements Blob, Externalizable {
	private static final long	serialVersionUID	= 3258134639489857079L;

	private byte[]					data;

	public SerialBlob() {
		// empty
	}

	public SerialBlob(final Blob other) throws SQLException {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final InputStream is = other.getBinaryStream();
			final byte[] buff = new byte[1024];
			int len;
			while ((len = is.read(buff)) > 0) {
				baos.write(buff, 0, len);
			}
			data = baos.toByteArray();
			other.free();
		} catch (final IOException e) {
			throw new SQLException("Can't retrieve contents of Blob", e.toString());
		}
	}

	public SerialBlob(final InputStream is) throws SQLException {
		try {
			init(is);
		} catch (final IOException e) {
			throw new SQLException("Can't retrieve contents of Clob", e.toString());
		}
	}

	public SerialBlob(final InputStream is, final long length) throws SQLException {
		try {
			init(is, length);
		} catch (final IOException e) {
			throw new SQLException("Can't retrieve contents of Clob", e.toString());
		}
	}

	public void init(final InputStream is) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final byte[] buff = new byte[1024];
		int len;
		while ((len = is.read(buff)) > 0) {
			baos.write(buff, 0, len);
		}
		data = baos.toByteArray();
	}

	public void init(final InputStream is, final long length) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final byte[] buff = new byte[1024];
		int len;
		long toRead = length;
		while (toRead > 0 && (len = is.read(buff, 0, (int) (toRead > 1024 ? 1024 : toRead))) > 0) {
			baos.write(buff, 0, len);
			toRead -= len;
		}
		data = baos.toByteArray();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(data.length);
		for (final byte element : data) {
			out.writeByte(element);
		}
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		final int len = in.readInt();
		data = new byte[len];
		for (int i = 0; i < len; i++) {
			data[i] = in.readByte();
		}
	}

	@Override
	public long length() throws SQLException {
		return data.length;
	}

	@Override
	public byte[] getBytes(final long pos, final int length) throws SQLException {
		if (pos <= Integer.MAX_VALUE) {
			final byte[] result = new byte[length];
			System.arraycopy(data, (int) pos - 1, result, 0, length);
			return result;
		}

		// very slow but gets around problems with the pos being represented
		// as long instead of an int in most java.io and other byte copying
		// APIs
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (long i = 0; i < length; ++i) {
			baos.write(data[(int) (pos + i)]);
		}
		return baos.toByteArray();
	}

	@Override
	public InputStream getBinaryStream() throws SQLException {
		return new ByteArrayInputStream(data);
	}

	@Override
	public long position(final byte pattern[], final long start) throws SQLException {
		throw new UnsupportedOperationException("Blob.position");
	}

	@Override
	public long position(final Blob pattern, final long start) throws SQLException {
		throw new UnsupportedOperationException("Blob.position");
	}

	@Override
	public int setBytes(final long pos, final byte[] bytes) throws SQLException {
		throw new UnsupportedOperationException("Blob.setBytes");
	}

	@Override
	public int setBytes(final long pos, final byte[] bytes, final int offset, final int len) throws SQLException {
		throw new UnsupportedOperationException("Blob.setBytes");
	}

	@Override
	public OutputStream setBinaryStream(final long pos) throws SQLException {
		throw new UnsupportedOperationException("Blob.setBinaryStream");
	}

	@Override
	public void truncate(final long len) throws SQLException {
		throw new UnsupportedOperationException("Blob.truncate");
	}

	/* start JDBC4 support */
	@Override
	public InputStream getBinaryStream(final long pos, final long length) throws SQLException {
		if (pos <= Integer.MAX_VALUE && length <= Integer.MAX_VALUE) {
			return new ByteArrayInputStream(data, (int) pos, (int) length);
		}

		// very slow but gets around problems with the pos being represented
		// as long instead of an int in most java.io and other byte copying
		// APIs
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (long i = 0; i < length; ++i) {
			baos.write(data[(int) (i + pos)]);
		}
		return new ByteArrayInputStream(baos.toByteArray());
	}

	@Override
	public void free() throws SQLException {
		data = null;
	}
	/* end JDBC4 support */
}
