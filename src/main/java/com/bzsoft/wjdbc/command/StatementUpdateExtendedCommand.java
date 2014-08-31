// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementUpdateExtendedCommand implements Command<Integer, Statement> {

	private static final long	serialVersionUID	= 3690198762949851445L;

	private String					sql;
	private int						autoGeneratedKeys;
	private int[]					columnIndexes;
	private String[]				columnNames;

	public StatementUpdateExtendedCommand() {
		// empty
	}

	public StatementUpdateExtendedCommand(final String sql, final int autoGeneratedKeys) {
		this.sql = sql;
		this.autoGeneratedKeys = autoGeneratedKeys;
	}

	public StatementUpdateExtendedCommand(final String sql, final int[] columnIndexes) {
		this.sql = sql;
		this.columnIndexes = columnIndexes;
	}

	public StatementUpdateExtendedCommand(final String sql, final String[] columnNames) {
		this.sql = sql;
		this.columnNames = columnNames;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeUTF(sql);
		out.writeInt(autoGeneratedKeys);
		out.writeObject(columnIndexes);
		out.writeObject(columnNames);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		sql = in.readUTF();
		autoGeneratedKeys = in.readInt();
		columnIndexes = (int[]) in.readObject();
		columnNames = (String[]) in.readObject();
	}

	@Override
	public Integer execute(final Statement target, final ConnectionContext ctx) throws SQLException {
		final String sentence = ctx.resolveOrCheckQuery(this.sql);
		// Now make the descision what call to execute
		if (columnIndexes != null) {
			return Integer.valueOf(target.executeUpdate(sentence, columnIndexes));
		} else if (columnNames != null) {
			return Integer.valueOf(target.executeUpdate(sentence, columnNames));
		} else {
			return Integer.valueOf(target.executeUpdate(sentence, autoGeneratedKeys));
		}
	}

	@Override
	public String toString() {
		return "StatementUpdateExtendedCommand: " + sql;
	}
}
