package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementGetResultSetCommand extends BaseCommand<ResultSet, Statement> {

	private static final long	serialVersionUID	= 972478509574280291L;

	public StatementGetResultSetCommand() {
		// Empty
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
	public ResultSet execute(final Statement target, final ConnectionContext ctx) throws SQLException {
		return target.getResultSet();
	}

	@Override
	public String toString() {
		return "StatementGetResultSetCommand";
	}
}
