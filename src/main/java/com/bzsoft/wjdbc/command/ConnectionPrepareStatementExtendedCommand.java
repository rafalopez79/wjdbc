// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bzsoft.wjdbc.transport.JdbcStatementTransport;
import com.bzsoft.wjdbc.transport.StatementLocalJdbcObjectTransport;


public class ConnectionPrepareStatementExtendedCommand extends BaseCommand<JdbcStatementTransport<PreparedStatement>, Connection> {
	private static final long	serialVersionUID	= 3760559793366120249L;

	private String					sql;
	private int						autoGeneratedKeys;
	private int[]					columnIndexes;
	private String[]				columnNames;

	public ConnectionPrepareStatementExtendedCommand() {
	}

	public ConnectionPrepareStatementExtendedCommand(final String sql, final int autoGeneratedKeys) {
		this.sql = sql;
		this.autoGeneratedKeys = autoGeneratedKeys;
	}

	public ConnectionPrepareStatementExtendedCommand(final String sql, final int[] columnIndexes) {
		this.sql = sql;
		this.columnIndexes = columnIndexes;
	}

	public ConnectionPrepareStatementExtendedCommand(final String sql, final String[] columnNames) {
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
	public JdbcStatementTransport<PreparedStatement> execute(final Connection target, final ConnectionContext ctx) throws SQLException {
		// Resolve and check the query
		final String sentence = ctx.resolveOrCheckQuery(this.sql);
		// Now make the descision what call to execute
		final PreparedStatement pstmt;
		if (columnIndexes != null) {
			pstmt = target.prepareStatement(sentence, columnIndexes);
		} else if (columnNames != null) {
			pstmt = target.prepareStatement(sentence, columnNames);
		} else {
			pstmt = target.prepareStatement(sentence, autoGeneratedKeys);
		}
		return StatementLocalJdbcObjectTransport.of(pstmt);
	}
}
