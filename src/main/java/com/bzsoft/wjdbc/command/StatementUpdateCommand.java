// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementUpdateCommand implements Command<Integer, Statement> {
	private static final long	serialVersionUID	= 3689069560279937335L;

	private String					sql;

	public StatementUpdateCommand() {
	}

	public StatementUpdateCommand(final String sql) {
		this.sql = sql;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeUTF(sql);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		sql = in.readUTF();
	}

	@Override
	public Integer execute(final Statement target, final ConnectionContext ctx) throws SQLException {
		return Integer.valueOf(target.executeUpdate(ctx.resolveOrCheckQuery(sql)));
	}

	@Override
	public String toString() {
		return "StatementUpdateCommand: " + sql;
	}
}
