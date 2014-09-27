package com.bzsoft.wjdbc.util;

import java.sql.SQLException;

public final class Validate {

	private Validate() {
		// empty
	}

	public static void notNull(final String s, final String msg) throws SQLException {
		if (s == null) {
			throw new SQLException(msg);
		}
	}

	public static void isTrue(final boolean value, final String msg) throws SQLException {
		if (!value) {
			throw new SQLException(msg);
		}
	}

	public static void isFalse(final boolean value, final String msg) throws SQLException {
		if (value) {
			throw new SQLException(msg);
		}
	}
}
