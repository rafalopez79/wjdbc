package com.bzsoft.wjdbc;

/**
 * The Class WJdbcException.
 */
public class WJdbcException extends Exception {

	private static final long	serialVersionUID	= -7552211448253764663L;

	/**
	 * Instantiates a new w jdbc exception.
	 *
	 * @param msg
	 *           the msg
	 */
	public WJdbcException(final String msg) {
		super(msg);
	}

	/**
	 * Instantiates a new w jdbc exception.
	 *
	 * @param msg
	 *           the msg
	 * @param ex
	 *           the ex
	 */
	public WJdbcException(final String msg, final Exception ex) {
		super(msg, ex);
	}
}
