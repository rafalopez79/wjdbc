package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementGetGeneratedKeysCommand extends BaseCommand<ResultSet, Statement> implements ResultSetProducerCommand {

	private static final long	serialVersionUID	= -6529413105195105196L;

	public StatementGetGeneratedKeysCommand() {
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
	public int getResultSetType() {
		return ResultSet.TYPE_SCROLL_INSENSITIVE;
	}

	@Override
	public ResultSet execute(final Statement target, final ConnectionContext ctx) throws SQLException {
		return target.getGeneratedKeys();
	}
}
