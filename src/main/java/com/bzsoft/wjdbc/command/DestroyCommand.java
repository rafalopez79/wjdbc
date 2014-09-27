package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

public class DestroyCommand extends BaseCommand<Void, Object> {

	private static final long	serialVersionUID	= 4457392123395584636L;

	private long					uid;
	private int						interfaceType;

	public DestroyCommand() {
		// empty
	}

	public DestroyCommand(final long uid, final int interfaceType) {
		this.uid = uid;
		this.interfaceType = interfaceType;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeLong(uid);
		out.writeInt(interfaceType);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		uid = in.readLong();
		interfaceType = in.readInt();
	}

	@Override
	public Void execute(final Object target, final ConnectionContext ctx) throws SQLException {
		if (target instanceof Connection) {
			ctx.closeAllRelatedJdbcObjects();
		}
		// now we are ready to go on and close this connection

		final Object removed = ctx.removeJDBCObject(uid);

		// Check for identity
		if (removed == target) {
			try {
				final Class<?> targetClass = JdbcInterfaceType.INTERFACES[interfaceType];
				final Method mth = targetClass.getDeclaredMethod("close", new Class[0]);
				mth.invoke(target, (Object[]) null);
			} catch (final NoSuchMethodException e) {
				// Object doesn't support close()
			} catch (final Exception e) {
				// empty
			}
		}
		return null;
	}
}
