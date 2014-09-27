package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

public class ConnectionRollbackWithSavepointCommand extends BaseCommand<Void, Connection> {

	private static final long	serialVersionUID	= -5189425307111618293L;

	private long					uidOfSavepoint;

	public ConnectionRollbackWithSavepointCommand() {
		// Empty
	}

	public ConnectionRollbackWithSavepointCommand(final long uidOfSavepoint) {
		this.uidOfSavepoint = uidOfSavepoint;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeLong(uidOfSavepoint);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		uidOfSavepoint = in.readLong();
	}

	@Override
	public Void execute(final Connection target, final ConnectionContext ctx) throws SQLException {
		final Savepoint sp = (Savepoint) ctx.getJDBCObject(uidOfSavepoint);
		target.rollback(sp);
		return null;
	}
}
