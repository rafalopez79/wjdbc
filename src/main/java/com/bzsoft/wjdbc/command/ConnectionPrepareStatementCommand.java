package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bzsoft.wjdbc.transport.JdbcStatementTransport;
import com.bzsoft.wjdbc.transport.StatementLocalJdbcObjectTransport;

public class ConnectionPrepareStatementCommand extends BaseCommand<JdbcStatementTransport<PreparedStatement>, Connection> {

	private static final long	serialVersionUID	= 3905239013827949875L;

	private String					sql;
	private Integer				resultSetType;
	private Integer				resultSetConcurrency;
	private Integer				resultSetHoldability;

	public ConnectionPrepareStatementCommand() {
		// empty
	}

	public ConnectionPrepareStatementCommand(final String sql) {
		this.sql = sql;
	}

	public ConnectionPrepareStatementCommand(final String sql, final int resultSetType, final int resultSetConcurrency) {
		this.sql = sql;
		this.resultSetType = Integer.valueOf(resultSetType);
		this.resultSetConcurrency = Integer.valueOf(resultSetConcurrency);
	}

	public ConnectionPrepareStatementCommand(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) {
		this.sql = sql;
		this.resultSetType = Integer.valueOf(resultSetType);
		this.resultSetConcurrency = Integer.valueOf(resultSetConcurrency);
		this.resultSetHoldability = Integer.valueOf(resultSetHoldability);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeUTF(sql);
		out.writeObject(resultSetType);
		out.writeObject(resultSetConcurrency);
		out.writeObject(resultSetHoldability);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		sql = in.readUTF();
		resultSetType = (Integer) in.readObject();
		resultSetConcurrency = (Integer) in.readObject();
		resultSetHoldability = (Integer) in.readObject();
	}

	@Override
	public JdbcStatementTransport<PreparedStatement> execute(final Connection conn, final ConnectionContext ctx) throws SQLException {
		// Resolve and check the query
		final String sentence = ctx.resolveOrCheckQuery(this.sql);
		// Now choose the correct call
		final PreparedStatement pstmt;
		if (resultSetType != null && resultSetConcurrency != null) {
			if (resultSetHoldability != null) {
				pstmt = conn.prepareStatement(sentence, resultSetType.intValue(), resultSetConcurrency.intValue(), resultSetHoldability.intValue());
			} else {
				pstmt = conn.prepareStatement(sentence, resultSetType.intValue(), resultSetConcurrency.intValue());
			}
		} else {
			pstmt = conn.prepareStatement(sentence);
		}
		return StatementLocalJdbcObjectTransport.of(pstmt);
	}
}
