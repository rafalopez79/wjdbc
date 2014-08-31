// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementExecuteBatchCommand implements Command<int[], Statement> {

	private static final long	serialVersionUID	= -995205757280796006L;

	private String[]				sql;

	public StatementExecuteBatchCommand() {
		// empty
	}

	public StatementExecuteBatchCommand(final String[] sql) {
		this.sql = sql;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(sql.length);
		for (final String s : sql) {
			out.writeUTF(s);
		}
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		final int count = in.readInt();
		sql = new String[count];
		for (int i = 0; i < count; i++) {
			sql[i] = in.readUTF();
		}
	}

	@Override
	public int[] execute(final Statement stmt, final ConnectionContext ctx) throws SQLException {
		stmt.clearBatch();
		for (final String element : sql) {
			stmt.addBatch(ctx.resolveOrCheckQuery(element));
		}
		return stmt.executeBatch();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("StatementExecuteBatchCommand:\n");
		for (final String element : sql) {
			sb.append(element);
			sb.append('\n');
		}
		return sb.toString();
	}
}
