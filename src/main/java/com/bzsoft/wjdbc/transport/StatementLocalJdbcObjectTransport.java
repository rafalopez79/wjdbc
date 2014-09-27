package com.bzsoft.wjdbc.transport;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementLocalJdbcObjectTransport<T extends Statement> implements JdbcStatementTransport<T> {

	private T	value;
	private int	maxRows;
	private int	queryTimeout;

	public static <T extends Statement> StatementLocalJdbcObjectTransport<T> of(final T stmt) {
		return new StatementLocalJdbcObjectTransport<T>(stmt);
	}

	public StatementLocalJdbcObjectTransport() {
		// empty
	}

	public StatementLocalJdbcObjectTransport(final T stmt) {
		this.value = stmt;
		try {
			maxRows = stmt.getMaxRows();
		} catch (final SQLException e) {
			maxRows = -1;
		}
		try {
			queryTimeout = stmt.getQueryTimeout();
		} catch (final SQLException e) {
			queryTimeout = -1;
		}
	}

	@Override
	public T getJDBCObject() {
		return value;
	}

	@Override
	public long getUID() {
		return 0;
	}

	@Override
	public int getQueryTimeout() {
		return queryTimeout;
	}

	@Override
	public int getMaxRows() {
		return maxRows;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		// empty
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		// empty
	}
}
