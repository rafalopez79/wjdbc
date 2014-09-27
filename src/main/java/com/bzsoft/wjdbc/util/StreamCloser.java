package com.bzsoft.wjdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class StreamCloser {

	private StreamCloser() {
		// empty
	}

	public static void close(final InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (final IOException e) {
				// empty
			}
		}
	}

	public static void close(final OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (final IOException e) {
				// empty
			}
		}
	}
}
