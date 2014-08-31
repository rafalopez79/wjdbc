package com.bzsoft.wjdbc.rmi.sf;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TraceFilterOutputStream extends FilterOutputStream {

	public TraceFilterOutputStream(final OutputStream os) {
		super(os);
	}

	@Override
	public void write(final int b) throws IOException {
		super.write(b);
	}

	@Override
	public void write(final byte[] b, final int off, final int len) throws IOException {
		super.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		super.flush();
	}
}
