package com.bzsoft.wjdbc;

import java.sql.SQLException;

/**
 * The Class WJdbcSqlException.
 */
public class WJdbcSqlException extends SQLException {

	private static final long	serialVersionUID	= 8544805378134067865L;

	/**
	 * Instantiates a new w jdbc sql exception.
	 *
	 * @param reason
	 *           the reason
	 */
	public WJdbcSqlException(final Throwable reason) {
		super(reason);
	}

	/**
	 * Instantiates a new w jdbc sql exception.
	 *
	 * @param msg
	 *           the msg
	 */
	public WJdbcSqlException(final String msg) {
		super(msg);
	}
}
