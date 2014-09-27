package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.bzsoft.wjdbc.transport.JdbcStatementTransport;
import com.bzsoft.wjdbc.transport.StatementLocalJdbcObjectTransport;

public class ConnectionPrepareCallCommand extends BaseCommand<JdbcStatementTransport<CallableStatement>, Connection> {
	private static final long	serialVersionUID	= 3258125843279655728L;

	private String					sql;
	private Integer				resultSetType;
	private Integer				resultSetConcurrency;
	private Integer				resultSetHoldability;

	public ConnectionPrepareCallCommand() {
		// empty
	}

	public ConnectionPrepareCallCommand(final String sql) {
		this.sql = sql;
	}

	public ConnectionPrepareCallCommand(final String sql, final int resultSetType, final int resultSetConcurrency) {
		this.sql = sql;
		this.resultSetType = Integer.valueOf(resultSetType);
		this.resultSetConcurrency = Integer.valueOf(resultSetConcurrency);
	}

	public ConnectionPrepareCallCommand(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) {
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
	public JdbcStatementTransport<CallableStatement> execute(final Connection target, final ConnectionContext ctx) throws SQLException {
		// Resolve and check the query
		final String sentence = ctx.resolveOrCheckQuery(this.sql);
		// Switch to the correct call
		final CallableStatement cstmt;
		if (resultSetType != null && resultSetConcurrency != null) {
			if (resultSetHoldability != null) {
				cstmt = target.prepareCall(sentence, resultSetType.intValue(), resultSetConcurrency.intValue(), resultSetHoldability.intValue());
			} else {
				cstmt = target.prepareCall(sentence, resultSetType.intValue(), resultSetConcurrency.intValue());
			}
		} else {
			cstmt = target.prepareCall(sentence);
		}
		return StatementLocalJdbcObjectTransport.of(cstmt);
	}
}
