package com.bzsoft.wjdbc.rmi.sf;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipException;

/**
 * Modified from jdk
 * 
 * @see Inflater
 * @version 1.40, 04/07/06
 * @author David Connelly
 */
public class XInflaterInputStream extends FilterInputStream {
	/**
	 * Decompressor for this stream.
	 */
	protected Inflater	inf;

	/**
	 * Input buffer for decompression.
	 */
	protected byte[]		buf;

	/**
	 * Length of input buffer.
	 */
	protected int			len;

	private boolean		closed					= false;
	// this flag is set to true after EOF has reached
	private boolean		reachEOF					= false;

	private boolean		usesDefaultInflater	= false;

	private final byte[]	singleByteBuf			= new byte[1];

	private final byte[]	b							= new byte[512];

	public XInflaterInputStream(final InputStream in, final Inflater inf, final int size) {
		super(in);
		if (in == null || inf == null) {
			throw new NullPointerException();
		} else if (size <= 0) {
			throw new IllegalArgumentException("buffer size <= 0");
		}
		this.inf = inf;
		buf = new byte[size];
	}

	public XInflaterInputStream(final InputStream in, final Inflater inf) {
		this(in, inf, 512);
	}

	private void ensureOpen() throws IOException {
		if (closed) {
			throw new IOException("Stream closed");
		}
	}

	public XInflaterInputStream(final InputStream in) {
		this(in, new Inflater());
		usesDefaultInflater = true;
	}

	@Override
	public int read() throws IOException {
		ensureOpen();
		return read(singleByteBuf, 0, 1) == -1 ? -1 : singleByteBuf[0] & 0xff;
	}

	@Override
	public int read(final byte[] b, final int off, final int len) throws IOException {
		ensureOpen();
		if (b == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}
		try {
			int n;
			while ((n = inf.inflate(b, off, len)) == 0) {
				if (inf.finished() || inf.needsDictionary()) {
					reachEOF = true;
					return -1;
				}
				if (inf.needsInput()) {
					final int read = fill();
					if (read == -1) {
						return -1;
					}
				}
			}
			return n;
		} catch (final DataFormatException e) {
			final String s = e.getMessage();
			throw new ZipException(s != null ? s : "Invalid ZLIB data format");
		}
	}

	@Override
	public int available() throws IOException {
		ensureOpen();
		if (reachEOF) {
			return 0;
		}
		return 1;
	}

	@Override
	public long skip(final long n) throws IOException {
		if (n < 0) {
			throw new IllegalArgumentException("negative skip length");
		}
		ensureOpen();
		final int max = (int) Math.min(n, Integer.MAX_VALUE);
		int total = 0;
		while (total < max) {
			int len = max - total;
			if (len > b.length) {
				len = b.length;
			}
			len = read(b, 0, len);
			if (len == -1) {
				reachEOF = true;
				break;
			}
			total += len;
		}
		return total;
	}

	@Override
	public void close() throws IOException {
		if (!closed) {
			if (usesDefaultInflater) {
				inf.end();
			}
			in.close();
			closed = true;
		}
	}

	protected int fill() throws IOException {
		ensureOpen();
		len = in.read(buf, 0, buf.length);
		if (len != -1) {
			inf.setInput(buf, 0, len);
		}
		return len;
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public void mark(final int readlimit) {
		// empty
	}

	@Override
	public void reset() throws IOException {
		throw new IOException("mark/reset not supported");
	}
}
