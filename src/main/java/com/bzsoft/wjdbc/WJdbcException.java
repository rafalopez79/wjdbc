// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc;

public class WJdbcException extends Exception {

	private static final long	serialVersionUID	= -7552211448253764663L;

	public WJdbcException(final String msg) {
		super(msg);
	}

	public WJdbcException(final String msg, final Exception ex) {
		super(msg, ex);
	}
}
