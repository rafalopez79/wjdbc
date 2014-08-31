package com.bzsoft.wjdbc.transport;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Statement;

public class StatementRemoteJdbcObjectTransport<T extends Statement> implements JdbcStatementTransport<T> {

	private long			uid;
	private int				maxRows;
	private int				queryTimeout;

	private transient T	stmt;

	public static <T extends Statement> StatementRemoteJdbcObjectTransport<T> of(final long uid, final T stmt, final int maxRows,
			final int queryTimeout) {
		return new StatementRemoteJdbcObjectTransport<T>(uid, stmt, maxRows, queryTimeout);
	}

	public StatementRemoteJdbcObjectTransport() {
		// empty
	}

	public StatementRemoteJdbcObjectTransport(final long uid, final T stmt, final int maxRows, final int queryTimeout) {
		this.uid = uid;
		this.stmt = stmt;
		this.maxRows = maxRows;
		this.queryTimeout = queryTimeout;
	}

	@Override
	public T getJDBCObject() {
		return stmt;
	}

	@Override
	public long getUID() {
		return uid;
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
		uid = in.readLong();
		maxRows = in.readInt();
		queryTimeout = in.readInt();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeLong(uid);
		out.writeInt(maxRows);
		out.writeInt(queryTimeout);
	}
}
