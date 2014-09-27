package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class CallableStatementSetObjectCommand extends BaseCommand<Void, CallableStatement> {
	private static final long	serialVersionUID	= -9132697894345849726L;

	private int						index;
	private String					paramName;
	private Integer				targetSqlType;
	private Integer				scale;
	private Object					transport;

	public CallableStatementSetObjectCommand() {
		// empty
	}

	public CallableStatementSetObjectCommand(final int index, final Integer targetSqlType, final Integer scale) {
		this.index = index;
		this.targetSqlType = targetSqlType;
		this.scale = scale;
		transport = null;
	}

	public CallableStatementSetObjectCommand(final String paramName, final Integer targetSqlType, final Integer scale) {
		this.paramName = paramName;
		this.targetSqlType = targetSqlType;
		this.scale = scale;
		transport = null;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(index);
		if (paramName == null) {
			out.writeBoolean(false);
		} else {
			out.writeBoolean(true);
			out.writeUTF(paramName);
		}
		out.writeObject(targetSqlType);
		out.writeObject(scale);
		out.writeObject(transport);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		index = in.readInt();
		final boolean isNotNull = in.readBoolean();
		if (isNotNull) {
			paramName = in.readUTF();
		} else {
			paramName = null;
		}
		targetSqlType = (Integer) in.readObject();
		scale = (Integer) in.readObject();
		transport = in.readObject();
	}

	public void setObject(final Object obj) throws SQLException {
		if (obj instanceof Serializable) {
			transport = obj;
		} else {
			throw new SQLException("Object of type " + obj.getClass().getName() + " is not serializable");
		}
	}

	@Override
	public Void execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		final Object obj = transport;
		if (paramName != null) {
			if (targetSqlType != null) {
				if (scale != null) {
					cstmt.setObject(paramName, obj, targetSqlType.intValue(), scale.intValue());
				} else {
					cstmt.setObject(paramName, obj, targetSqlType.intValue());
				}
			} else {
				cstmt.setObject(paramName, obj);
			}
		} else {
			if (targetSqlType != null) {
				if (scale != null) {
					cstmt.setObject(index, obj, targetSqlType.intValue(), scale.intValue());
				} else {
					cstmt.setObject(index, obj, targetSqlType.intValue());
				}
			} else {
				cstmt.setObject(index, obj);
			}
		}
		return null;
	}
}
