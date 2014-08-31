// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionCommitCommand extends BaseCommand<Boolean, Connection> {

	private static final long	serialVersionUID	= 6221665321426908025L;

	public ConnectionCommitCommand() {
		// empty
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		// empty
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		// empty
	}

	@Override
	public Boolean execute(final Connection conn, final ConnectionContext ctx) throws SQLException {
		conn.commit();
		return conn.getWarnings() != null ? Boolean.TRUE : Boolean.FALSE;
	}
}
