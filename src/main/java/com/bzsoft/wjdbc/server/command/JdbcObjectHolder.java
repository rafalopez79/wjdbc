package com.bzsoft.wjdbc.server.command;


final class JdbcObjectHolder<V> {

	private final V	jdbcObject;
	private final int	jdbcInterfaceType;

	JdbcObjectHolder(final V jdbcObject, final int jdbcInterfaceType) {
		this.jdbcObject = jdbcObject;
		this.jdbcInterfaceType = jdbcInterfaceType;
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
