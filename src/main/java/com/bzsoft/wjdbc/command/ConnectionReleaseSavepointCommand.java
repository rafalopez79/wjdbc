package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

public class ConnectionReleaseSavepointCommand extends BaseCommand<Void, Connection> {

	private static final long	serialVersionUID	= 6221665321426908025L;

	private long					uidOfSavepoint;

	public ConnectionReleaseSavepointCommand() {
		// empty
	}

	public ConnectionReleaseSavepointCommand(final long uidOfSavepoint) {
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
	public Void execute(final Connection conn, final ConnectionContext ctx) throws SQLException {
		final Savepoint sp = (Savepoint) ctx.getJDBCObject(uidOfSavepoint);
		conn.releaseSavepoint(sp);
		return null;
	}

}
