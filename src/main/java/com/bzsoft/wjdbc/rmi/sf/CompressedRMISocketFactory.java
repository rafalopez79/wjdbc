package com.bzsoft.wjdbc.rmi.sf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;

/**
 * An RMISocketFactory which enables compressed transmission. We use
 * {@link LZFCompressingInputStream} and {@link LZFCompressingOutputStream} for
 * this.
 *
 * As we extend WrappingSocketFactory, this can be used on top of another
 * {@link RMISocketFactory}.
 */
public class CompressedRMISocketFactory extends WrappingSocketFactory {

	private static final long		serialVersionUID	= 1;

	public CompressedRMISocketFactory(final RMIClientSocketFactory cFac, final RMIServerSocketFactory sFac) {
		super(cFac, sFac);
	}

	public CompressedRMISocketFactory() {
		// empty
	}

	@SuppressWarnings("resource")
	@Override
	protected StreamPair wrap(final InputStream in, final OutputStream out) throws IOException {
		return new StreamPair(new LZFCompressingInputStream(in), new LZFCompressingOutputStream(out));
	}
}
