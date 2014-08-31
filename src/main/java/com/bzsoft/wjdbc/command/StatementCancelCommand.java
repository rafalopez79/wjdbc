// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementCancelCommand extends BaseCommand<Void, Statement> {

	private static final long	serialVersionUID	= 5602747945115861740L;

	public StatementCancelCommand() {
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
	public Void execute(final Statement target, final ConnectionContext ctx) throws SQLException {
		target.cancel();
		return null;
	}

}
