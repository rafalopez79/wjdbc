package com.bzsoft.wjdbc.rmi.sf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.util.zip.Deflater;

/**
 * An RMISocketFactory which enables compressed transmission. We use
 * {@link #CompressingInputStream} and {@link #CompressingOutputStream} for
 * this.
 *
 * As we extend WrappingSocketFactory, this can be used on top of another
 * {@link RMISocketFactory}.
 */
public class CompressedRMISocketFactory extends WrappingSocketFactory {

	private static final long		serialVersionUID	= 1;

	private static final boolean	USELZF				= true;

	public CompressedRMISocketFactory(final RMIClientSocketFactory cFac, final RMIServerSocketFactory sFac) {
		super(cFac, sFac);
	}

	public CompressedRMISocketFactory() {
		// empty
	}

	@Override
	protected StreamPair wrap(final InputStream in, final OutputStream out) throws IOException {
		if (USELZF) {
			return new StreamPair(new LZFCompressingInputStream(in), new LZFCompressingOutputStream(out));
		}
		return new StreamPair(new DecompressingInputStream(in), new CompressingOutputStream(out, Deflater.BEST_SPEED));
	}
}
