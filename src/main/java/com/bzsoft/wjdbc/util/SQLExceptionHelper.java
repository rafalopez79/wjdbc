package com.bzsoft.wjdbc.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.SQLWarning;

/**
 * SQLExceptionHelper wraps driver-specific exceptions in a generic
 * SQLException.
 */
public final class SQLExceptionHelper {

	private SQLExceptionHelper() {
		// empty
	}

	public static final SQLException wrap(final Throwable t) {
		return wrapThrowable(t);
	}

	public static final SQLException wrap(final SQLException ex) {
		if (isSQLExceptionGeneric(ex)) {
			return ex;
		}
		return wrapSQLException(ex);
	}

	private static final boolean isSQLExceptionGeneric(final SQLException ex) {
		boolean exceptionIsGeneric = true;
		// Check here if all chained SQLExceptions can be serialized, there may be
		// vendor specific SQLException classes which can't be delivered to the
		// client
		SQLException loop = ex;
		while (loop != null && exceptionIsGeneric) {
			exceptionIsGeneric = loop.getClass().equals(SQLException.class) || loop.getClass().equals(SQLWarning.class);
			loop = loop.getNextException();
		}
		return exceptionIsGeneric;
	}

	private static final SQLException wrapSQLException(final SQLException ex) {
		final SQLException ex2 = new SQLException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode());
		if (ex.getNextException() != null) {
			ex2.setNextException(wrapSQLException(ex.getNextException()));
		}
		return ex2;
	}

	private static final SQLException wrapThrowable(final Throwable t) {
		// Nothing to do, wrap the thing in a generic SQLException
		final StringWriter sw = new StringWriter(256);
		final PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return new SQLException(sw.toString());
	}
}
