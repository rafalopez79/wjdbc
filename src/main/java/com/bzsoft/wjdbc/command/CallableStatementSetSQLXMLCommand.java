package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.SQLXML;

public class CallableStatementSetSQLXMLCommand extends BaseCommand<Void, CallableStatement> {
	static final long	serialVersionUID	= 7396654168665073844L;

	private int			index;
	private String		parameterName;
	private SQLXML		sqlxml;

	public CallableStatementSetSQLXMLCommand() {
		// Empty
	}

	public CallableStatementSetSQLXMLCommand(final int index, final SQLXML sqlxml) throws IOException {
		this.index = index;
		this.sqlxml = sqlxml;
	}

	public CallableStatementSetSQLXMLCommand(final String paramName, final SQLXML sqlxml) throws IOException {
		parameterName = paramName;
		this.sqlxml = sqlxml;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(index);
		if (parameterName == null) {
			out.writeBoolean(false);
		} else {
			out.writeBoolean(true);
			out.writeUTF(parameterName);
		}
		out.writeObject(sqlxml);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		index = in.readInt();
		final boolean isNotNull = in.readBoolean();
		if (isNotNull) {
			parameterName = in.readUTF();
		} else {
			parameterName = null;
		}
		sqlxml = (SQLXML) in.readObject();
	}

	@Override
	public Void execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		if (parameterName != null) {
			cstmt.setSQLXML(parameterName, sqlxml);
		} else {
			cstmt.setSQLXML(index, sqlxml);
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + (parameterName == null ? 0 : parameterName.hashCode());
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
		final CallableStatementSetSQLXMLCommand other = (CallableStatementSetSQLXMLCommand) obj;
		if (index != other.index) {
			return false;
		}
		if (parameterName == null) {
			if (other.parameterName != null) {
				return false;
			}
		} else if (!parameterName.equals(other.parameterName)) {
			return false;
		}
		return true;
	}



}
