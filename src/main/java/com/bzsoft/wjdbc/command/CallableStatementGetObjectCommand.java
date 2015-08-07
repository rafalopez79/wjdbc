package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.bzsoft.wjdbc.WCallableStatement;

public class CallableStatementGetObjectCommand extends BaseCommand<Object, CallableStatement> {

	private static final long		serialVersionUID	= 7045834396073252820L;

	private int							index;
	private String						parameterName;
	private Map<String, Class<?>>	map;
	private Class<?>					clazz;

	public CallableStatementGetObjectCommand() {
		// empty
	}

	public CallableStatementGetObjectCommand(final int index) {
		this.index = index;
		map = null;
		clazz = null;
	}

	public CallableStatementGetObjectCommand(final int index, final Class<?> clazz) {
		this.index = index;
		map = null;
		this.clazz = clazz;
	}

	public CallableStatementGetObjectCommand(final int index, final Map<String, Class<?>> map) {
		this.index = index;
		this.map = map;
		clazz = null;
	}

	public CallableStatementGetObjectCommand(final String paramName) {
		parameterName = paramName;
		map = null;
		clazz = null;
	}

	public CallableStatementGetObjectCommand(final String paramName, final Class<?> clazz) {
		parameterName = paramName;
		map = null;
		this.clazz = clazz;
	}

	public CallableStatementGetObjectCommand(final String paramName, final Map<String, Class<?>> map) {
		parameterName = paramName;
		this.map = map;
		clazz = null;
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
		out.writeObject(map);
		out.writeBoolean(clazz != null);
		if (clazz != null) {
			out.writeUTF(clazz.getName());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		index = in.readInt();
		final boolean isNotNull = in.readBoolean();
		if (isNotNull) {
			parameterName = in.readUTF();
		} else {
			parameterName = null;
		}
		map = (Map<String, Class<?>>) in.readObject();
		if (in.readBoolean()) {
			clazz = Class.forName(in.readUTF());
		}
	}

	@Override
	public Object execute(final CallableStatement cstmt, final ConnectionContext ctx) throws SQLException {
		Object result;
		if (parameterName != null) {
			if (map != null) {
				result = cstmt.getObject(parameterName, map);
			} else if (clazz != null) {
				result = ((WCallableStatement) cstmt).getObject(parameterName, clazz);
			} else {
				result = cstmt.getObject(parameterName);
			}
		} else {
			if (map != null) {
				result = cstmt.getObject(index, map);
			} else if (clazz != null) {
				result = ((WCallableStatement) cstmt).getObject(index, clazz);
			} else {
				result = cstmt.getObject(index);
			}
		}
		// ResultSets are handled by the caller
		if (result instanceof ResultSet) {
			return result;
		}
		// Any other type must be Serializable to be transported
		if (result == null || result instanceof Serializable) {
			return result;
		}
		throw new SQLException("Object of type " + result.getClass().getName() + " is not serializable");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + (map == null ? 0 : map.hashCode());
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
		final CallableStatementGetObjectCommand other = (CallableStatementGetObjectCommand) obj;
		if (index != other.index) {
			return false;
		}
		if (map == null) {
			if (other.map != null) {
				return false;
			}
		} else if (!map.equals(other.map)) {
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
