// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.util;

import java.io.Externalizable;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * SQLExceptionHelper wraps driver-specific exceptions in a generic
 * SQLException.
 */
public final class SQLExceptionHelper {

	private SQLExceptionHelper() {
		// empty
	}

	public static SQLException wrap(final Throwable t) {
		if (isExceptionGeneric(t)) {
			if (t instanceof SQLException) {
				return (SQLException) t;
			}
			return new SQLException(t.getMessage(), t);
		}
		return wrapThrowable(t);
	}

	public static SQLException wrap(final SQLException ex) {
		if (isSQLExceptionGeneric(ex)) {
			// yes a bit misleading but since this exception is already OK
			// for transport, its much simplier just to return it
			return ex;
		}
		return wrapSQLException(ex);
	}

	private static boolean isExceptionGeneric(final Throwable ex) {
		boolean exceptionIsGeneric = true;
		Throwable loop = ex;
		while (loop != null && exceptionIsGeneric) {
			exceptionIsGeneric = Serializable.class.isAssignableFrom(loop.getClass()) || Externalizable.class.isAssignableFrom(loop.getClass());
			loop = loop.getCause();
		}
		return exceptionIsGeneric;
	}

	private static boolean isSQLExceptionGeneric(final SQLException ex) {
		boolean exceptionIsGeneric = true;
		final Iterator<Throwable> it = ex.iterator();
		while (it.hasNext() && exceptionIsGeneric) {
			final Throwable t = it.next();
			exceptionIsGeneric = Serializable.class.isAssignableFrom(t.getClass()) || Externalizable.class.isAssignableFrom(t.getClass());
		}
		return exceptionIsGeneric;
	}

	private static SQLException wrapSQLException(final SQLException ex) {
		final SQLException ex2 = new SQLException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), wrap(ex.getCause()));
		if (ex.getNextException() != null) {
			ex2.setNextException(wrap(ex.getNextException()));
		}
		return ex2;
	}

	private static SQLException wrapThrowable(final Throwable t) {
		SQLException wrapped = null;
		if (t instanceof SQLException) {
			wrapped = wrapSQLException((SQLException) t);
		} else {
			wrapped = new SQLException(t.getMessage(), wrap(t.getCause()));
		}
		// REVIEW: doing some evil hackeration here, but only because I believe
		// that those that change stack traces deserve a special place in hell
		// If your code can be hacked by stack trace info, it deserves to
		// be hacked and will be cracked anyway
		wrapped.setStackTrace(wrapped.getStackTrace());
		return wrapped;
	}
}
