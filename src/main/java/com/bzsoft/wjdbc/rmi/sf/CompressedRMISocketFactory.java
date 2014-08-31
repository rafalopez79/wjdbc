package com.bzsoft.wjdbc.rmi.sf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;

/**
 * An RMISocketFactory which enables compressed transmission. We use
 * {@link #CompressingInputStream} and {@link #CompressingOutputStream} for
 * this.
 * 
 * As we extend WrappingSocketFactory, this can be used on top of another
 * {@link RMISocketFactory}.
 */
public class CompressedRMISocketFactory extends WrappingSocketFactory {

	private static final long	serialVersionUID	= 1;

	private final int				compressionMode;

	public CompressedRMISocketFactory(final RMIClientSocketFactory cFac, final RMIServerSocketFactory sFac, final int compressionMode) {
		super(cFac, sFac);
		this.compressionMode = compressionMode;
	}

	public CompressedRMISocketFactory(final int compressionMode) {
		this.compressionMode = compressionMode;
	}

	@Override
	protected StreamPair wrap(final InputStream in, final OutputStream out) throws IOException {
		return new StreamPair(new DecompressingInputStream(in), new CompressingOutputStream(out, compressionMode));
	}
}
