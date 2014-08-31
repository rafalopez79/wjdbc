// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.sql.SQLException;

import com.bzsoft.wjdbc.util.SQLExceptionHelper;

public class ReflectiveCommand<R, P> implements Command<R, P>, Externalizable {

	private static final long		serialVersionUID	= 1573361368678688726L;

	private static final Object[]	ZEROPARAMETERS		= new Object[0];

	private int							interfaceType;
	private String						cmd;
	private Object[]					parameters;
	private int							parameterTypes;
	private transient Class<?>		targetClass;

	public ReflectiveCommand() {
		// empty
	}

	public ReflectiveCommand(final int interfaceType, final String cmd) {
		this.interfaceType = interfaceType;
		this.cmd = cmd;
		parameters = ZEROPARAMETERS;
	}

	public ReflectiveCommand(final int interfaceType, final String cmd, final Object[] parms, final int parmTypes) {
		this.interfaceType = interfaceType;
		this.cmd = cmd;
		parameters = parms;
		parameterTypes = parmTypes;
	}

	public static <R, P> Command<R, P> of(final int interfaceType, final String cmdstr) {
		return new ReflectiveCommand<R, P>(interfaceType, cmdstr);
	}

	public static <R, P> Command<R, P> of(final int interfaceType, final String cmdstr, final Object[] parms, final int types) {
		return new ReflectiveCommand<R, P>(interfaceType, cmdstr, parms, types);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(interfaceType);
		out.writeUTF(cmd);
		out.writeInt(parameters.length);
		for (final Object parameter : parameters) {
			out.writeObject(parameter);
		}
		out.writeInt(parameterTypes);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		interfaceType = in.readInt();
		cmd = in.readUTF();
		final int len = in.readInt();
		parameters = new Object[len];
		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = in.readObject();
		}
		parameterTypes = in.readInt();
	}

	@SuppressWarnings("unchecked")
	@Override
	public R execute(final P target, final ConnectionContext ctx) throws SQLException {
		try {
			targetClass = JdbcInterfaceType.INTERFACES[interfaceType];
			final Method method = targetClass.getDeclaredMethod(cmd, ParameterTypeCombinations.TYPE_COMBINATIONS[parameterTypes]);
			return (R) method.invoke(target, parameters);
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	public int getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(final int interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getCommand() {
		return cmd;
	}

	public void setCommand(final String cmd) {
		this.cmd = cmd;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(final Object[] parameters) {
		this.parameters = parameters;
	}

	public int getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(final int parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ReflectiveCommand '").append(cmd).append("'");
		if (targetClass != null) {
			sb.append(" on object of class ").append(targetClass.getName());
		}
		if (parameters.length > 0) {
			sb.append(" with ").append(parameters.length).append(" parameters\n");
			for (int i = 0, n = parameters.length; i < n; i++) {
				sb.append("\t[").append(i).append("] ");
				if (parameters[i] != null) {
					final String value = parameters[i].toString();
					if (value.length() > 0) {
						sb.append(value);
					} else {
						sb.append("<empty>");
					}
				} else {
					sb.append("<null>");
				}
				if (i < n - 1) {
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
}
