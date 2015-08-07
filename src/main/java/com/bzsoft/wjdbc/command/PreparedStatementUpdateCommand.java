package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import com.bzsoft.wjdbc.parameters.PreparedStatementParameter;

public class PreparedStatementUpdateCommand extends BaseCommand<Integer, PreparedStatement> {

	private static final long					serialVersionUID	= 8987200111317750567L;

	protected PreparedStatementParameter[]	params;

	public PreparedStatementUpdateCommand() {
		// Empty
	}

	public PreparedStatementUpdateCommand(final PreparedStatementParameter[] params) {
		this.params = params;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(params);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		params = (PreparedStatementParameter[]) in.readObject();
	}

	@Override
	public Integer execute(final PreparedStatement pstmt, final ConnectionContext ctx) throws SQLException {
		for (int i = 0; i < params.length; i++) {
			if (params[i] != null) {
				params[i].setParameter(pstmt, i + 1);
			}
		}
		return Integer.valueOf(pstmt.executeUpdate());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(params);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PreparedStatementUpdateCommand other = (PreparedStatementUpdateCommand) obj;
		if (!Arrays.equals(params, other.params)) {
			return false;
		}
		return true;
	}


}
