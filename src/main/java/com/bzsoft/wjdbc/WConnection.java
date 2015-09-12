package com.bzsoft.wjdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.bzsoft.wjdbc.command.ConnectionCommitCommand;
import com.bzsoft.wjdbc.command.ConnectionPrepareCallCommand;
import com.bzsoft.wjdbc.command.ConnectionPrepareStatementCommand;
import com.bzsoft.wjdbc.command.ConnectionPrepareStatementExtendedCommand;
import com.bzsoft.wjdbc.command.ConnectionReleaseSavepointCommand;
import com.bzsoft.wjdbc.command.ConnectionRollbackWithSavepointCommand;
import com.bzsoft.wjdbc.command.DecoratedCommandSink;
import com.bzsoft.wjdbc.command.DestroyCommand;
import com.bzsoft.wjdbc.command.JdbcInterfaceType;
import com.bzsoft.wjdbc.command.ParameterTypeCombinations;
import com.bzsoft.wjdbc.command.ReflectiveCommand;
import com.bzsoft.wjdbc.serial.SerialArray;
import com.bzsoft.wjdbc.serial.SerialBlob;
import com.bzsoft.wjdbc.serial.SerialClob;
import com.bzsoft.wjdbc.serial.SerialNClob;
import com.bzsoft.wjdbc.serial.SerialSQLXML;
import com.bzsoft.wjdbc.serial.SerialStruct;
import com.bzsoft.wjdbc.server.concurrent.Executor;
import com.bzsoft.wjdbc.transport.JdbcObjectTransport;
import com.bzsoft.wjdbc.transport.JdbcStatementTransport;
import com.bzsoft.wjdbc.util.ClientInfo;
import com.bzsoft.wjdbc.util.Validate;

/**
 * The Class WConnection.
 */
public class WConnection extends WBase implements Connection {

	private static final int	MAX_TIMEOUT	= 20000;

	private Boolean				isAutoCommit;
	private Boolean				isReadOnly;
	private Integer				transactionIsolation;
	private WDatabaseMetaData	databaseMetaData;
	private String					catalog;
	private String					schema;
	private Integer				holdability;

	private volatile boolean	isClosed;

	private final Executor		executor;

	/**
	 * Instantiates a new w connection.
	 *
	 * @param connuid
	 *           the connuid
	 * @param sink
	 *           the sink
	 * @param exec
	 *           the exec
	 */
	WConnection(final long connuid, final DecoratedCommandSink sink, final Executor exec) {
		super(connuid, sink);
		isClosed = false;
		isAutoCommit = null;
		isReadOnly = null;
		transactionIsolation = null;
		catalog = null;
		schema = null;
		holdability = null;
		executor = exec;
	}

	@Override
	protected void finalize() throws Throwable {
		if (!isClosed) {
			close();
		}
	}

	@Override
	public Statement createStatement() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<Statement> srt = sink.process(objectUid,
				ReflectiveCommand.<JdbcStatementTransport<Statement>, Object> of(JdbcInterfaceType.CONNECTION, "createStatement"));
		return new WStatement(srt.getUID(), this, sink, ResultSet.TYPE_FORWARD_ONLY, srt.getMaxRows(), srt.getQueryTimeout());
	}

	@Override
	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<PreparedStatement> result = sink.process(objectUid, new ConnectionPrepareStatementCommand(sql));
		return new WPreparedStatement(result.getUID(), this, sink, ResultSet.TYPE_FORWARD_ONLY, result.getMaxRows(), result.getQueryTimeout());
	}

	@Override
	public CallableStatement prepareCall(final String sql) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<CallableStatement> result = sink.process(objectUid, new ConnectionPrepareCallCommand(sql));
		return new WCallableStatement(result.getUID(), this, sink, ResultSet.TYPE_FORWARD_ONLY, result.getMaxRows(), result.getQueryTimeout());
	}

	@Override
	public String nativeSQL(final String sql) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(isClosed, "Connection closed");
		return sink.process(objectUid,
				ReflectiveCommand.<String, String> of(JdbcInterfaceType.CONNECTION, "nativeSQL", new Object[] { sql }, ParameterTypeCombinations.STR));
	}

	@Override
	public void setAutoCommit(final boolean autoCommit) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CONNECTION, "setAutoCommit", new Object[] { autoCommit ? Boolean.TRUE
				: Boolean.FALSE }, ParameterTypeCombinations.BOL));
		isAutoCommit = Boolean.valueOf(autoCommit);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		if (isAutoCommit == null) {
			final boolean autoCommit = sink.processWithBooleanResult(objectUid,
					ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.CONNECTION, "getAutoCommit"));
			isAutoCommit = Boolean.valueOf(autoCommit);
		}
		return isAutoCommit.booleanValue();
	}

	@Override
	public void commit() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		sink.processWithBooleanResult(objectUid, new ConnectionCommitCommand());
	}

	@Override
	public void rollback() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CONNECTION, "rollback"));
	}

	@Override
	public void close() throws SQLException {
		if (!isClosed) {
			if (databaseMetaData != null) {
				final long metadataUid = databaseMetaData.getObjectUID();
				sink.process(metadataUid, new DestroyCommand(metadataUid, JdbcInterfaceType.DATABASEMETADATA));
				databaseMetaData = null;
			}
			sink.process(objectUid, new DestroyCommand(objectUid, JdbcInterfaceType.CONNECTION));
			sink.close();
			executor.close();
			isClosed = true;
		}
	}

	@Override
	public boolean isClosed() throws SQLException {
		return isClosed;
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		if (databaseMetaData == null) {
			final JdbcObjectTransport<DatabaseMetaData> metadataUid = sink.process(objectUid,
					ReflectiveCommand.<JdbcObjectTransport<DatabaseMetaData>, Object> of(JdbcInterfaceType.CONNECTION, "getMetaData"));
			databaseMetaData = new WDatabaseMetaData(this, metadataUid.getUID(), sink);
		}
		return databaseMetaData;
	}

	@Override
	public void setReadOnly(final boolean readOnly) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CONNECTION, "setReadOnly", new Object[] { readOnly ? Boolean.TRUE
				: Boolean.FALSE }, ParameterTypeCombinations.BOL));
		isReadOnly = readOnly;
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		if (isReadOnly == null) {
			isReadOnly = sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.CONNECTION, "isReadOnly"));
		}
		return isReadOnly;
	}

	@Override
	public void setCatalog(final String catalog) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		sink.process(objectUid,
				ReflectiveCommand.of(JdbcInterfaceType.CONNECTION, "setCatalog", new Object[] { catalog }, ParameterTypeCombinations.STR));
		this.catalog = catalog;
	}

	@Override
	public String getCatalog() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		if (catalog == null) {
			catalog = sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.CONNECTION, "getCatalog"));
		}
		return catalog;
	}

	@Override
	public void setTransactionIsolation(final int level) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CONNECTION, "setTransactionIsolation", new Object[] { Integer.valueOf(level) },
				ParameterTypeCombinations.INT));
		transactionIsolation = level;
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		if (transactionIsolation == null) {
			transactionIsolation = sink.processWithIntResult(objectUid,
					ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.CONNECTION, "getTransactionIsolation"));
		}
		return transactionIsolation;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		//empty
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<Statement> result = sink.process(
				objectUid,
				ReflectiveCommand.<JdbcStatementTransport<Statement>, Object> of(JdbcInterfaceType.CONNECTION, "createStatement",
						new Object[] { Integer.valueOf(resultSetType), Integer.valueOf(resultSetConcurrency) }, ParameterTypeCombinations.INTINT));
		return new WStatement(result.getUID(), this, sink, resultSetType, result.getMaxRows(), result.getQueryTimeout());
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<PreparedStatement> result = sink.process(objectUid, new ConnectionPrepareStatementCommand(sql, resultSetType,
				resultSetConcurrency));
		return new WPreparedStatement(result.getUID(), this, sink, resultSetType, result.getMaxRows(), result.getQueryTimeout());
	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<CallableStatement> result = sink.process(objectUid, new ConnectionPrepareCallCommand(sql, resultSetType,
				resultSetConcurrency));
		return new WCallableStatement(result.getUID(), this, sink, resultSetType, result.getMaxRows(), result.getQueryTimeout());
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<Map<String, Class<?>>, Object> of(JdbcInterfaceType.CONNECTION, "getTypeMap"));
	}

	@Override
	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CONNECTION, "setTypeMap", new Object[] { map }, ParameterTypeCombinations.MAP));
	}

	@Override
	public void setHoldability(final int holdability) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CONNECTION, "setHoldability", new Object[] { Integer.valueOf(holdability) },
				ParameterTypeCombinations.INT));
		this.holdability = holdability;
	}

	@Override
	public int getHoldability() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		if (holdability == null) {
			holdability = sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.CONNECTION, "getHoldability"));
		}
		return holdability;
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcObjectTransport<WSavepoint> result = sink.process(objectUid,
				ReflectiveCommand.<JdbcObjectTransport<WSavepoint>, Object> of(JdbcInterfaceType.CONNECTION, "setSavepoint"));
		return new WSavepoint(result.getUID(), sink);
	}

	@Override
	public Savepoint setSavepoint(final String name) throws SQLException {
		Validate.notNull(name, "Savepoint name is null");
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcObjectTransport<WSavepoint> result = sink.process(objectUid, ReflectiveCommand.<JdbcObjectTransport<WSavepoint>, Object> of(
				JdbcInterfaceType.CONNECTION, "setSavepoint", new Object[] { name }, ParameterTypeCombinations.STR));
		return new WSavepoint(result.getUID(), sink);
	}

	@Override
	public void rollback(final Savepoint savepoint) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		if (savepoint instanceof WSavepoint) {
			final WSavepoint vsp = (WSavepoint) savepoint;
			sink.process(objectUid, new ConnectionRollbackWithSavepointCommand(vsp.getObjectUID()));
		} else {
			throw new SQLException("Bad savepoint");
		}
	}

	@Override
	public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		if (savepoint instanceof WSavepoint) {
			final WSavepoint vsp = (WSavepoint) savepoint;
			sink.process(objectUid, new ConnectionReleaseSavepointCommand(vsp.getObjectUID()));
		} else {
			throw new SQLException("Bad savepoint");
		}
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<Statement> result = sink.process(objectUid, ReflectiveCommand.<JdbcStatementTransport<Statement>, Object> of(
				JdbcInterfaceType.CONNECTION, "createStatement", new Object[] { Integer.valueOf(resultSetType), Integer.valueOf(resultSetConcurrency),
						Integer.valueOf(resultSetHoldability) }, ParameterTypeCombinations.INTINTINT));
		return new WStatement(result.getUID(), this, sink, resultSetType, result.getMaxRows(), result.getQueryTimeout());
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<PreparedStatement> result = sink.process(objectUid, new ConnectionPrepareStatementCommand(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability));
		return new WPreparedStatement(result.getUID(), this, sink, resultSetType, result.getMaxRows(), result.getQueryTimeout());
	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<CallableStatement> result = sink.process(objectUid, new ConnectionPrepareCallCommand(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability));
		return new WCallableStatement(result.getUID(), this, sink, resultSetType, result.getMaxRows(), result.getQueryTimeout());
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<PreparedStatement> result = sink.process(objectUid, new ConnectionPrepareStatementExtendedCommand(sql,
				autoGeneratedKeys));
		return new WPreparedStatement(result.getUID(), this, sink, ResultSet.TYPE_FORWARD_ONLY, result.getMaxRows(), result.getQueryTimeout());
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int columnIndexes[]) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<PreparedStatement> result = sink.process(objectUid, new ConnectionPrepareStatementExtendedCommand(sql,
				columnIndexes));
		return new WPreparedStatement(result.getUID(), this, sink, ResultSet.TYPE_FORWARD_ONLY, result.getMaxRows(), result.getQueryTimeout());
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final String columnNames[]) throws SQLException {
		Validate.notNull(sql, "SQL sentence is null");
		Validate.isFalse(isClosed, "Connection closed");
		final JdbcStatementTransport<PreparedStatement> result = sink.process(objectUid,
				new ConnectionPrepareStatementExtendedCommand(sql, columnNames));
		return new WPreparedStatement(result.getUID(), this, sink, ResultSet.TYPE_FORWARD_ONLY, result.getMaxRows(), result.getQueryTimeout());
	}

	@Override
	public Clob createClob() throws SQLException {
		return new SerialClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return new SerialBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return new SerialNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return new SerialSQLXML();
	}

	private class ValidRunnable implements Callable<Boolean> {

		private final int	timeout;

		private ValidRunnable(final int timeout) {
			this.timeout = timeout;
		}

		@Override
		public Boolean call() {
			try {
				final Object args[] = { timeout };
				return sink.processWithBooleanResult(objectUid,
						ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.CONNECTION, "isValid", args, ParameterTypeCombinations.INT));
			} catch (final SQLException sqle) {
				return false;
			}
		}
	}

	@Override
	public boolean isValid(final int timeout) throws SQLException {
		if (timeout <= 0) {
			throw new SQLException("invalid timeout value " + timeout);
		}
		if (isClosed) {
			return false;
		}
		final Future<Boolean> future = executor.execute(new ValidRunnable(timeout));
		try {
			return future.get(Math.max(MAX_TIMEOUT, timeout), TimeUnit.MILLISECONDS);
		} catch (final Exception e) {
			return false;
		}
	}

	@Override
	public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
		final Properties clientProps = ClientInfo.getProperties(null);
		clientProps.put(name, value);
		try {
			sink.process(objectUid,
					ReflectiveCommand.of(JdbcInterfaceType.CONNECTION, "setClientInfo", new Object[] { name, value }, ParameterTypeCombinations.STRSTR));
		} catch (final SQLClientInfoException e) {
			throw e;
		} catch (final SQLException sqle) {
			throw new SQLClientInfoException(null, sqle);
		}
	}

	@Override
	public void setClientInfo(final Properties properties) throws SQLClientInfoException {
		for (final Object oKey : properties.keySet()) {
			final String key = (String) oKey;
			final String value = properties.getProperty(key);
			setClientInfo(key, value);
		}
	}

	@Override
	public String getClientInfo(final String name) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		final Properties clientProps = ClientInfo.getProperties(null);
		final String value = clientProps.getProperty(name);
		if (value != null) {
			return value;
		}
		final String ret = sink.process(objectUid,
				ReflectiveCommand.<String, Void> of(JdbcInterfaceType.CONNECTION, "getClientInfo", new Object[] { name }, ParameterTypeCombinations.STR));
		if (ret != null) {
			clientProps.setProperty(name, ret);
		}
		return ret;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		final Properties clientProps = ClientInfo.getProperties(null);
		if (clientProps != null && !clientProps.isEmpty()) {
			return clientProps;
		}
		final Properties prop = (Properties) sink.process(objectUid, ReflectiveCommand.of(JdbcInterfaceType.CONNECTION, "getClientInfo"));
		if (prop != null) {
			for (final Object oKey : prop.keySet()) {
				final String key = (String) oKey;
				final String value = prop.getProperty(key);
				clientProps.setProperty(key, value);
			}
		}
		return prop;
	}

	@Override
	public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
		return new SerialArray(typeName, elements);
	}

	@Override
	public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
		return new SerialStruct(typeName, attributes);
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return iface.isAssignableFrom(WConnection.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return (T) this;
	}

	@Override
	public void setSchema(final String schema) throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		sink.process(objectUid,
				ReflectiveCommand.<Void, String> of(JdbcInterfaceType.CONNECTION, "setSchema", new Object[] { schema }, ParameterTypeCombinations.STR));
		this.schema = schema;
	}

	@Override
	public String getSchema() throws SQLException {
		if (schema == null) {
			Validate.isFalse(isClosed, "Connection closed");
			schema = sink.process(objectUid, ReflectiveCommand.<String, Void> of(JdbcInterfaceType.CONNECTION, "getSchema", new Object[] {}, 0));
			return schema;
		}
		return schema;
	}

	@Override
	public void abort(final java.util.concurrent.Executor exec) throws SQLException {
		//TODO: review
		close();
	}

	@Override
	public void setNetworkTimeout(final java.util.concurrent.Executor executor, final int milliseconds) throws SQLException {
		throw new UnsupportedOperationException("setNetworkTimeout");
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		Validate.isFalse(isClosed, "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.CONNECTION, "getNetworkTimeout"));
	}
}
