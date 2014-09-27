package com.bzsoft.wjdbc.server.command;

import com.bzsoft.wjdbc.serial.CallingContext;

final class JdbcObjectHolder<V> {

	private final V					jdbcObject;
	private final CallingContext	callingContext;
	private final int					jdbcInterfaceType;

	JdbcObjectHolder(final V jdbcObject, final CallingContext ctx, final int jdbcInterfaceType) {
		this.jdbcObject = jdbcObject;
		this.callingContext = ctx;
		this.jdbcInterfaceType = jdbcInterfaceType;
	}

	CallingContext getCallingContext() {
		return this.callingContext;
	}

	V getJdbcObject() {
		return this.jdbcObject;
	}

	int getJdbcInterfaceType() {
		return this.jdbcInterfaceType;
	}

	@Override
	public String toString() {
		return "JdbcObjectHolder [jdbcObject=" + jdbcObject + ",  jdbcInterfaceType=" + jdbcInterfaceType + "]";
	}
}
