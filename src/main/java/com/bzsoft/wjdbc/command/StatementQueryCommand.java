// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementQueryCommand implements Command<ResultSet, Statement>, ResultSetProducerCommand {
	private static final long	serialVersionUID	= -8463588846424302034L;

	private int						resultSetType;
	private String					sql;

	public StatementQueryCommand() {
	}

	public StatementQueryCommand(final String sql, final int resultSetType) {
		this.sql = sql;
		this.resultSetType = resultSetType;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(resultSetType);
		out.writeUTF(sql);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		resultSetType = in.readInt();
		sql = in.readUTF();
	}

	@Override
	public int getResultSetType() {
		return resultSetType;
	}

	@Override
	public ResultSet execute(final Statement target, final ConnectionContext ctx) throws SQLException {
		return target.executeQuery(ctx.resolveOrCheckQuery(sql));
	}

	@Override
	public String toString() {
		return "StatementQueryCommand: " + sql;
	}
}
