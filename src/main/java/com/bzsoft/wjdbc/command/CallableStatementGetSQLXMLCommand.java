package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.SQLXML;

import com.bzsoft.wjdbc.serial.SerialSQLXML;

public class CallableStatementGetSQLXMLCommand extends BaseCommand<SQLXML, CallableStatement> {
	private static final long	serialVersionUID	= 4203440656745793953L;

	private int						index;
	private String					parameterName;

	public CallableStatementGetSQLXMLCommand() {
		// empty
	}

	public CallableStatementGetSQLXMLCommand(final int index) {
		this.index = index;
	}

	public CallableStatementGetSQLXMLCommand(final String paramName) {
		parameterName = paramName;
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
	}

	@Override
	public SQLXML execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		SQLXML result;
		if (parameterName != null) {
			result = cstmt.getSQLXML(parameterName);
		} else {
			result = cstmt.getSQLXML(index);
		}
		return new SerialSQLXML(result);
	}

}
