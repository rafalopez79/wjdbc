package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.CallableStatement;
import java.sql.RowId;
import java.sql.SQLException;

public class CallableStatementSetRowIdCommand extends BaseCommand<Void, CallableStatement> {
	private static final long	serialVersionUID	= -2847792562974087927L;

	private int						index;
	private String					parameterName;
	private RowId					rowId;

	public CallableStatementSetRowIdCommand() {
		// empty
	}

	public CallableStatementSetRowIdCommand(final int index, final RowId rowId) throws IOException {
		this.index = index;
		this.rowId = rowId;
	}

	public CallableStatementSetRowIdCommand(final String paramName, final RowId rowId) throws IOException {
		parameterName = paramName;
		this.rowId = rowId;
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
		out.writeObject(rowId);
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
		rowId = (RowId) in.readObject();
	}

	@Override
	public Void execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		if (parameterName != null) {
			cstmt.setRowId(parameterName, rowId);
		} else {
			cstmt.setRowId(index, rowId);
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + (parameterName == null ? 0 : parameterName.hashCode());
		result = prime * result + (rowId == null ? 0 : rowId.hashCode());
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
		final CallableStatementSetRowIdCommand other = (CallableStatementSetRowIdCommand) obj;
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
		if (rowId == null) {
			if (other.rowId != null) {
				return false;
			}
		} else if (!rowId.equals(other.rowId)) {
			return false;
		}
		return true;
	}


}
