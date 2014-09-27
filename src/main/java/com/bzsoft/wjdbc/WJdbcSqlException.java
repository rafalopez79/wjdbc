package com.bzsoft.wjdbc;

import java.sql.SQLException;

public class WJdbcSqlException extends SQLException {

	private static final long	serialVersionUID	= 8544805378134067865L;

	public WJdbcSqlException(final Throwable reason) {
		super(reason);
	}

	public WJdbcSqlException(final String msg) {
		super(msg);
	}
}
