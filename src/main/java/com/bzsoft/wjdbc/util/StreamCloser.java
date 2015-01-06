package com.bzsoft.wjdbc.util;

import java.io.Closeable;
import java.io.IOException;

public final class StreamCloser {

	private StreamCloser() {
		// empty
	}

	public static void close(final Closeable is) {
		if (is != null) {
			try {
				is.close();
			} catch (final IOException e) {
				// empty
			}
		}
	}
}
