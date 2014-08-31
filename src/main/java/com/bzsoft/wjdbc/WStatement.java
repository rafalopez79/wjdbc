// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bzsoft.wjdbc.command.DecoratedCommandSink;
import com.bzsoft.wjdbc.command.DestroyCommand;
import com.bzsoft.wjdbc.command.JdbcInterfaceType;
import com.bzsoft.wjdbc.command.ParameterTypeCombinations;
import com.bzsoft.wjdbc.command.ReflectiveCommand;
import com.bzsoft.wjdbc.command.StatementCancelCommand;
import com.bzsoft.wjdbc.command.StatementExecuteBatchCommand;
import com.bzsoft.wjdbc.command.StatementExecuteCommand;
import com.bzsoft.wjdbc.command.StatementExecuteExtendedCommand;
import com.bzsoft.wjdbc.command.StatementGetGeneratedKeysCommand;
import com.bzsoft.wjdbc.command.StatementGetResultSetCommand;
import com.bzsoft.wjdbc.command.StatementQueryCommand;
import com.bzsoft.wjdbc.command.StatementUpdateCommand;
import com.bzsoft.wjdbc.command.StatementUpdateExtendedCommand;
import com.bzsoft.wjdbc.serial.StreamingResultSet;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;
import com.bzsoft.wjdbc.util.Validate;

public class WStatement extends WBase implements Statement {

	protected final Connection		connection;
	private final List<String>		batchCollector;
	protected int						maxRows;
	protected int						queryTimeout;
	protected StreamingResultSet	currentResultSet;
	protected int						resultSetType;
	protected volatile boolean		isClosed;
	protected boolean					isCloseOnCompletion;

	protected Integer					maxFieldSize;

	public WStatement(final long uid, final Connection connection, final DecoratedCommandSink theSink, final int resultSetType, final int maxRows,
			final int queryTimeout) {
		super(uid, theSink);
		this.connection = connection;
		this.resultSetType = resultSetType;
		this.maxRows = maxRows;
		this.queryTimeout = queryTimeout;
		this.batchCollector = new ArrayList<String>();
		isClosed = false;
		isCloseOnCompletion = false;

		maxFieldSize = null;
	}

	@Override
	protected void finalize() throws Throwable {
		if (!isClosed) {
			close();
		}
	}

	@Override
	public ResultSet executeQuery(final String sql) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		try {
			final StreamingResultSet srs = (StreamingResultSet) sink.process(objectUid, new StatementQueryCommand(sql, resultSetType), true);
			srs.setStatement(this);
			srs.setCommandSink(sink);
			currentResultSet = srs;
			return srs;
		} catch (final SQLException e) {
			throw e;
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public int executeUpdate(final String sql) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithIntResult(objectUid, new StatementUpdateCommand(sql));
	}

	@Override
	public void close() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		sink.process(objectUid, new DestroyCommand(objectUid, JdbcInterfaceType.STATEMENT));
		isClosed = true;
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		if (maxFieldSize == null) {
			maxFieldSize = sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.STATEMENT, "getMaxFieldSize"));
		}
		return maxFieldSize;
	}

	@Override
	public void setMaxFieldSize(final int max) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.STATEMENT, "setMaxFieldSize", new Object[] { Integer.valueOf(max) },
				ParameterTypeCombinations.INT));
		maxFieldSize = max;
	}

	@Override
	public int getMaxRows() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		if (maxRows < 0) {
			final int result = sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.STATEMENT, "getMaxRows"));
			maxRows = result;
		}
		return maxRows;
	}

	@Override
	public void setMaxRows(final int max) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		sink.process(objectUid,
				ReflectiveCommand.of(JdbcInterfaceType.STATEMENT, "setMaxRows", new Object[] { Integer.valueOf(max) }, ParameterTypeCombinations.INT));
		maxRows = max;
	}

	@Override
	public void setEscapeProcessing(final boolean enable) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.STATEMENT, "setEscapeProcessing", new Object[] { enable ? Boolean.TRUE
				: Boolean.FALSE }, ParameterTypeCombinations.BOL));
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		if (queryTimeout < 0) {
			final int result = sink.processWithIntResult(objectUid,
					ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.STATEMENT, "getQueryTimeout"));
			queryTimeout = result;
		}
		return queryTimeout;
	}

	@Override
	public void setQueryTimeout(final int seconds) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.STATEMENT, "setQueryTimeout", new Object[] { Integer.valueOf(seconds) },
				ParameterTypeCombinations.INT));
		queryTimeout = seconds;
	}

	@Override
	public void cancel() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		sink.process(objectUid, new StatementCancelCommand());
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return (SQLWarning) sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.STATEMENT, "getWarnings"));
	}

	@Override
	public void clearWarnings() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.STATEMENT, "clearWarnings"));
	}

	@Override
	public void setCursorName(final String name) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		sink.process(objectUid,
				ReflectiveCommand.of(JdbcInterfaceType.STATEMENT, "setCursorName", new Object[] { name }, ParameterTypeCombinations.STR));
	}

	@Override
	public boolean execute(final String sql) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		// Reset the current ResultSet before executing this command
		currentResultSet = null;
		return sink.processWithBooleanResult(objectUid, new StatementExecuteCommand(sql));
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		if (currentResultSet == null) {
			try {
				currentResultSet = (StreamingResultSet) sink.process(objectUid, new StatementGetResultSetCommand(), true);
				currentResultSet.setStatement(this);
				currentResultSet.setCommandSink(sink);
			} catch (final Exception e) {
				throw SQLExceptionHelper.wrap(e);
			}
		}
		return currentResultSet;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.STATEMENT, "getUpdateCount"));
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		try {
			return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.STATEMENT, "getMoreResults"));
		} finally {
			currentResultSet = null;
		}
	}

	@Override
	public void setFetchDirection(final int direction) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.STATEMENT, "setFetchDirection", new Object[] { Integer.valueOf(direction) },
				ParameterTypeCombinations.INT));
	}

	@Override
	public int getFetchDirection() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.STATEMENT, "getFetchDirection"));
	}

	@Override
	public void setFetchSize(final int rows) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		sink.process(objectUid,
				ReflectiveCommand.of(JdbcInterfaceType.STATEMENT, "setFetchSize", new Object[] { Integer.valueOf(rows) }, ParameterTypeCombinations.INT));
	}

	@Override
	public int getFetchSize() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.STATEMENT, "getFetchSize"));
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.STATEMENT, "getResultSetConcurrency"));
	}

	@Override
	public int getResultSetType() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.STATEMENT, "getResultSetType"));
	}

	@Override
	public void addBatch(final String sql) throws SQLException {
		Validate.isFalse(isClosed, "Statement closed");
		batchCollector.add(sql);
	}

	@Override
	public void clearBatch() throws SQLException {
		Validate.isFalse(isClosed, "Statement closed");
		batchCollector.clear();
	}

	@Override
	public int[] executeBatch() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		final String[] sql = batchCollector.toArray(new String[batchCollector.size()]);
		final int[] result = sink.process(objectUid, new StatementExecuteBatchCommand(sql));
		batchCollector.clear();
		return result;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return connection;
	}

	@Override
	public boolean getMoreResults(final int current) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.STATEMENT, "getMoreResults",
				new Object[] { Integer.valueOf(current) }, ParameterTypeCombinations.INT));
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		try {
			final StreamingResultSet srs = (StreamingResultSet) sink.process(objectUid, new StatementGetGeneratedKeysCommand(), true);
			srs.setStatement(this);
			srs.setCommandSink(sink);
			return srs;
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithIntResult(objectUid, new StatementUpdateExtendedCommand(sql, autoGeneratedKeys));
	}

	@Override
	public int executeUpdate(final String sql, final int columnIndexes[]) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithIntResult(objectUid, new StatementUpdateExtendedCommand(sql, columnIndexes));
	}

	@Override
	public int executeUpdate(final String sql, final String columnNames[]) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithIntResult(objectUid, new StatementUpdateExtendedCommand(sql, columnNames));
	}

	@Override
	public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithBooleanResult(objectUid, new StatementExecuteExtendedCommand(sql, autoGeneratedKeys));
	}

	@Override
	public boolean execute(final String sql, final int columnIndexes[]) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithBooleanResult(objectUid, new StatementExecuteExtendedCommand(sql, columnIndexes));
	}

	@Override
	public boolean execute(final String sql, final String columnNames[]) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithBooleanResult(objectUid, new StatementExecuteExtendedCommand(sql, columnNames));
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.STATEMENT, "getResultSetHoldability"));
	}

	/* start JDBC4 support */
	@Override
	public boolean isClosed() throws SQLException {
		return isClosed;
	}

	@Override
	public void setPoolable(final boolean poolable) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.STATEMENT, "setPoolable",
				new Object[] { poolable ? Boolean.TRUE : Boolean.FALSE }, ParameterTypeCombinations.BOL));
	}

	@Override
	public boolean isPoolable() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		Validate.isFalse(isClosed, "Statement closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.STATEMENT, "isPoolable"));
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return iface.isAssignableFrom(WStatement.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return (T) this;
	}

	public void closeOnCompletion() throws SQLException {
		isCloseOnCompletion = true;
	}

	public boolean isCloseOnCompletion() throws SQLException {
		return isCloseOnCompletion;
	}
}
