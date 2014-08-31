// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.serial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public final class StreamSerializer {

	private StreamSerializer() {
		// empty
	}

	public static byte[] toByteArray(final InputStream is) throws IOException {
		if (is == null) {
			return null;
		}
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(is.available());
		final byte[] buffer = new byte[1024];
		boolean loop = true;
		int offset = 0;
		while (loop) {
			final int readBytes = is.read(buffer);
			if (readBytes >= 0) {
				baos.write(buffer, offset, readBytes);
				offset += readBytes;
			} else {
				loop = false;
			}
		}
		return baos.toByteArray();
	}

	public static InputStream toInputStream(final byte[] buf) {
		if (buf == null) {
			return null;
		}
		return new ByteArrayInputStream(buf);
	}

	public static char[] toCharArray(final Reader reader) throws IOException {
		if (reader == null) {
			return null;
		}
		final CharArrayWriter caw = new CharArrayWriter(1024);
		final char[] buffer = new char[1024];
		int readChars;
		while ((readChars = reader.read(buffer)) > 0) {
			caw.write(buffer, 0, readChars);
		}
		return caw.toCharArray();
	}

	public static char[] toCharArray(final Reader reader, final long length) throws IOException {
		if (reader == null) {
			return null;
		}
		long l = length;
		final CharArrayWriter caw = new CharArrayWriter((int) l);
		final char[] buffer = new char[1024];
		while (l > 0) {
			final int readChars = reader.read(buffer);

			if (readChars >= 0) {
				caw.write(buffer, 0, (int) Math.min(readChars, l));
				l -= readChars;
			}
		}
		return caw.toCharArray();
	}

	public static Reader toReader(final char[] buf) {
		if (buf == null) {
			return null;
		}
		return new CharArrayReader(buf);
	}
}