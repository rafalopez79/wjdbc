// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.bzsoft.wjdbc.server.command.ResultSetHolder;


public class ResultSetGetMetaDataCommand extends BaseCommand<ResultSetMetaData, ResultSetHolder> {

	private static final long	serialVersionUID	= 3258411737794558008L;

	public ResultSetGetMetaDataCommand() {
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
	public ResultSetMetaData execute(final ResultSetHolder rsh, final ConnectionContext ctx) throws SQLException {
		return rsh.getMetaData();
	}
}
