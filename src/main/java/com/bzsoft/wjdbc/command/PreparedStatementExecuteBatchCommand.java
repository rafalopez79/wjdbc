package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.bzsoft.wjdbc.parameters.PreparedStatementParameter;

public class PreparedStatementExecuteBatchCommand implements Command<int[], PreparedStatement> {

	private static final long							serialVersionUID	= 2439854950000135145L;

	private List<PreparedStatementParameter[]>	batchCommands;

	public PreparedStatementExecuteBatchCommand() {
		// empty
	}

	public PreparedStatementExecuteBatchCommand(final List<PreparedStatementParameter[]> batches) {
		batchCommands = batches;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(batchCommands);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		batchCommands = (List<PreparedStatementParameter[]>) in.readObject();
	}

	@Override
	public int[] execute(final PreparedStatement pstmt, final ConnectionContext ctx) throws SQLException {
		pstmt.clearBatch();
		for (int i = 0, n = batchCommands.size(); i < n; i++) {
			final PreparedStatementParameter[] parms = batchCommands.get(i);
			for (int j = 0; j < parms.length; j++) {
				parms[j].setParameter(pstmt, j + 1);
			}
			pstmt.addBatch();
		}

		return pstmt.executeBatch();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PreparedStatementExecuteBatchCommand\n");
		for (int i = 0; i < batchCommands.size(); i++) {
			final PreparedStatementParameter[] preparedStatementParameters = batchCommands.get(i);
			sb.append("Parameter-Set ").append(i).append(":\n");
			for (int j = 0; j < preparedStatementParameters.length; j++) {
				final PreparedStatementParameter preparedStatementParameter = preparedStatementParameters[j];
				sb.append("\t[").append(j).append("] = ").append(preparedStatementParameter.toString()).append("\n");
			}
		}

		return sb.toString();
	}
}
