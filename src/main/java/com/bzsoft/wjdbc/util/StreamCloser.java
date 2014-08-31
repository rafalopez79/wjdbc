// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for closing stream securely.
 * 
 * @author Mike
 * 
 */
public final class StreamCloser {

	private StreamCloser() {
		// empty
	}

	/**
	 * Closes an InputStream.
	 * 
	 * @param is
	 *           InputStream to close
	 */
	public static void close(final InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (final IOException e) {
				// empty
			}
		}
	}

	/**
	 * Closes an OutputStream
	 * 
	 * @param os
	 *           OutputStream to close
	 */
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
