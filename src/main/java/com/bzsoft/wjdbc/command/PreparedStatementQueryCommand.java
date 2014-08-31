// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bzsoft.wjdbc.parameters.PreparedStatementParameter;


public class PreparedStatementQueryCommand implements Command<ResultSet, PreparedStatement>, ResultSetProducerCommand {

	private static final long					serialVersionUID	= -7028150330288724130L;

	protected PreparedStatementParameter[]	params;
	protected int									resultSetType;

	public PreparedStatementQueryCommand() {
		// empty
	}

	public PreparedStatementQueryCommand(final PreparedStatementParameter[] params, final int resultSetType) {
		this.params = params;
		this.resultSetType = resultSetType;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(resultSetType);
		out.writeObject(params);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		resultSetType = in.readInt();
		params = (PreparedStatementParameter[]) in.readObject();
	}

	@Override
	public int getResultSetType() {
		return resultSetType;
	}

	@Override
	public ResultSet execute(final PreparedStatement pstmt, final ConnectionContext ctx) throws SQLException {
		for (int i = 0; i < params.length; i++) {
			params[i].setParameter(pstmt, i + 1);
		}
		return pstmt.executeQuery();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PreparedStatementQueryCommand");
		if (params != null && params.length > 0) {
			sb.append(" with parameters\n");
			for (int i = 0, n = params.length; i < n; i++) {
				sb.append("\t[").append(i + 1).append("] = ").append(params[i]);
				if (i < n - 1) {
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
}
