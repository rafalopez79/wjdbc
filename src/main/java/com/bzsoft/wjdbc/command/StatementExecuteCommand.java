package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementExecuteCommand implements Command<Boolean, Statement> {

	private static final long	serialVersionUID	= 3760844562717291058L;

	private String					sql;

	public StatementExecuteCommand() {
		// empty
	}

	public StatementExecuteCommand(final String sql) {
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
	public Boolean execute(final Statement target, final ConnectionContext ctx) throws SQLException {
		return Boolean.valueOf(target.execute(ctx.resolveOrCheckQuery(sql)));
	}

	@Override
	public String toString() {
		return "StatementExecuteCommand: " + sql;
	}
}
