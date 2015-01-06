package com.bzsoft.wjdbc.server.command;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.ConnectionContext;
import com.bzsoft.wjdbc.command.DestroyCommand;
import com.bzsoft.wjdbc.command.JdbcInterfaceType;
import com.bzsoft.wjdbc.command.ResultSetProducerCommand;
import com.bzsoft.wjdbc.command.StatementCancelCommand;
import com.bzsoft.wjdbc.serial.SerialResultSetMetaData;
import com.bzsoft.wjdbc.serial.StreamingResultSet;
import com.bzsoft.wjdbc.server.concurrent.Executor;
import com.bzsoft.wjdbc.server.config.ConnectionConfiguration;
import com.bzsoft.wjdbc.transport.JdbcObjectTransport;
import com.bzsoft.wjdbc.transport.LocalJdbcObjectTransport;
import com.bzsoft.wjdbc.transport.RemoteJdbcObjectTransport;
import com.bzsoft.wjdbc.transport.StatementLocalJdbcObjectTransport;
import com.bzsoft.wjdbc.transport.StatementRemoteJdbcObjectTransport;

class ConnectionEntry implements ConnectionContext {

	private static final Logger						LOGGER	= Logger.getLogger(ConnectionEntry.class);

	private final long									uid;
	private final Connection							connection;
	private final ConnectionConfiguration			connectionConfiguration;
	private final Properties							clientInfo;

	private volatile boolean							active;
	private long											lastAccessTimestamp;
	private long											numberOfProcessedCommands;

	private final Map<Long, JdbcObjectHolder<?>>	jdbcObjects;

	private final Map<String, Integer>				commandCountMap;
	private final AtomicLong							counter;
	private final Executor								executor;
	private final int										rowPacketSize;
	private final Lock									lock;

	protected ConnectionEntry(final long connuid, final Connection conn, final ConnectionConfiguration config, final Properties clientInfo,
			final AtomicLong counter, final Executor executor, final int rowPacketSize) {
		connection = conn;
		connectionConfiguration = config;
		this.clientInfo = clientInfo;
		this.counter = counter;
		this.executor = executor;
		this.rowPacketSize = rowPacketSize;
		uid = connuid;
		jdbcObjects = new ConcurrentHashMap<Long, JdbcObjectHolder<?>>();
		jdbcObjects.put(connuid, new JdbcObjectHolder<Connection>(conn, JdbcInterfaceType.CONNECTION));
		commandCountMap = new ConcurrentHashMap<String, Integer>();
		active = false;
		lastAccessTimestamp = System.currentTimeMillis();
		numberOfProcessedCommands = 0;
		lock = new ReentrantLock(true);
	}

	protected void close() {
		try {
			if (!connection.isClosed()) {
				connection.close();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Closed connection " + uid);
				}
			}
			traceConnectionStatistics();
		} catch (final SQLException e) {
			LOGGER.error("Exception during closing connection", e);
		}
	}

	@Override
	public void closeAllRelatedJdbcObjects() throws SQLException {
		for (final Entry<Long, JdbcObjectHolder<?>> entry : jdbcObjects.entrySet()) {
			final Long key = entry.getKey();
			final JdbcObjectHolder<?> jdbcObject = entry.getValue();
			// don't act on the Connection itself - this will be done elsewhere
			if (jdbcObject.getJdbcInterfaceType() == JdbcInterfaceType.CONNECTION) {
				continue;
			}
			// create a DestroyCommand and act on it
			final Command<Void, Object> destroy = new DestroyCommand(key, jdbcObject.getJdbcInterfaceType());
			destroy.execute(jdbcObject.getJdbcObject(), this);
		}
	}

	boolean hasJdbcObjects() {
		return !jdbcObjects.isEmpty();
	}

	public Properties getClientInfo() {
		return clientInfo;
	}

	public boolean isActive() {
		return active;
	}

	public long getLastAccess() {
		return lastAccessTimestamp;
	}

	@Override
	public Object getJDBCObject(final long key) {
		final JdbcObjectHolder<?> jdbcObjectHolder = jdbcObjects.get(key);
		if (jdbcObjectHolder != null) {
			return jdbcObjectHolder.getJdbcObject();
		}
		return null;
	}

	@Override
	public void addJDBCObject(final long key, final Object partner) {
		final int jdbcInterfaceType = getJdbcInterfaceTypeFromObject(partner);
		jdbcObjects.put(key, new JdbcObjectHolder<Object>(partner, jdbcInterfaceType));
	}

	@Override
	public Object removeJDBCObject(final long key) {
		final JdbcObjectHolder<?> jdbcObjectHolder = jdbcObjects.remove(key);
		if (jdbcObjectHolder != null) {
			return jdbcObjectHolder.getJdbcObject();
		}
		return null;
	}

	@Override
	public int getRowPacketSize() {
		return connectionConfiguration.getRowPacketSize();
	}

	@Override
	public String getCharset() {
		return connectionConfiguration.getCharset();
	}

	@Override
	public String resolveOrCheckQuery(final String sql) throws SQLException {
		checkAgainstQueryFilters(sql);
		return sql;
	}

	@SuppressWarnings("unchecked")
	public <R, V> R executeCommand(final long objectUID, final Command<R, V> cmd) throws SQLException {
		try {
			lock.lock();
			active = true;
			lastAccessTimestamp = System.currentTimeMillis();
			R result = null;
			final JdbcObjectHolder<V> target = (JdbcObjectHolder<V>) jdbcObjects.get(objectUID);
			if (target != null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Target for UID " + objectUID + " found");
				}
				final V val = target.getJdbcObject();
				// Execute the command on the target object
				result = cmd.execute(val, this);
				// Check if the result must be remembered on the server with a UID
				final R rr = (R) checkResult(result, counter);

				if (rr instanceof JdbcObjectTransport) {
					// put it in the JDBC-Object-Table
					final JdbcObjectTransport<?> transport = (JdbcObjectTransport<?>) rr;
					final int jdbcInterfaceType = getJdbcInterfaceTypeFromObject(transport.getJDBCObject());
					final long ruid = transport.getUID();
					jdbcObjects.put(ruid, new JdbcObjectHolder<V>((V) transport.getJDBCObject(), jdbcInterfaceType));
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Registered " + result.getClass().getName() + " with UID " + ruid);
					}
					return rr;
				}
				// When the result is of type ResultSet then handle it specially
				if (result instanceof ResultSet) {
					boolean forwardOnly = false;
					if (cmd instanceof ResultSetProducerCommand) {
						forwardOnly = ((ResultSetProducerCommand) cmd).getResultSetType() == ResultSet.TYPE_FORWARD_ONLY;
					} else {
						LOGGER.debug("Command " + cmd.toString() + " doesn't implement "
								+ "ResultSetProducer-Interface, assuming ResultSet is scroll insensitive");
					}
					result = (R) handleResultSet((ResultSet) result, forwardOnly);
				} else if (result instanceof ResultSetMetaData) {
					result = (R) handleResultSetMetaData((ResultSetMetaData) result);
				} else if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("... returned " + result);
				}
			} else {
				LOGGER.warn("JDBC-Object for UID " + objectUID + " and command " + cmd + " is null !");
			}

			if (connectionConfiguration.isTraceCommandCount()) {
				final String cmdString = cmd.toString();
				final Integer oldval = commandCountMap.get(cmdString);
				if (oldval == null) {
					commandCountMap.put(cmdString, Integer.valueOf(1));
				} else {
					commandCountMap.put(cmdString, Integer.valueOf(oldval.intValue() + 1));
				}
			}
			numberOfProcessedCommands++;
			return result;
		} finally {
			active = false;
			lastAccessTimestamp = System.currentTimeMillis();
			lock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> JdbcObjectTransport<T> checkResult(final Object obj, final AtomicLong counter) {
		if (obj instanceof StatementLocalJdbcObjectTransport) {
			final StatementLocalJdbcObjectTransport<?> t = (StatementLocalJdbcObjectTransport<?>) obj;
			final long uid = counter.getAndIncrement();
			@SuppressWarnings("rawtypes")
			final JdbcObjectTransport<T> retval = new StatementRemoteJdbcObjectTransport(uid, t.getJDBCObject(), t.getMaxRows(), t.getQueryTimeout());
			return retval;
		} else if (obj instanceof LocalJdbcObjectTransport<?>) {
			final LocalJdbcObjectTransport<T> t = (LocalJdbcObjectTransport<T>) obj;
			final long uid = counter.getAndIncrement();
			return new RemoteJdbcObjectTransport<T>(uid, t.getJDBCObject());
		} else if (obj instanceof Statement) {
			final long uid = counter.getAndIncrement();
			final Statement stmt = (Statement) obj;
			int maxRows = -1;
			int queryTimeOut = -1;
			try {
				maxRows = stmt.getMaxRows();
			} catch (final SQLException e) {
				// empty
			}
			try {
				queryTimeOut = stmt.getQueryTimeout();
			} catch (final SQLException e) {
				// empty
			}
			final StatementRemoteJdbcObjectTransport<Statement> retval = StatementRemoteJdbcObjectTransport.of(uid, stmt, maxRows, queryTimeOut);
			return (JdbcObjectTransport<T>) retval;
		} else if (obj instanceof DatabaseMetaData) {
			final long uid = counter.getAndIncrement();
			return new RemoteJdbcObjectTransport<T>(uid, (T) obj);
		} else if (obj instanceof Savepoint) {
			final long uid = counter.getAndIncrement();
			return new RemoteJdbcObjectTransport<T>(uid, (T) obj);
		}
		return null;
	}

	public void cancelCurrentStatementExecution(final long itemuid, final StatementCancelCommand cmd) throws SQLException {
		final JdbcObjectHolder<?> target = jdbcObjects.get(itemuid);
		if (target != null) {
			try {
				final Statement stmt = (Statement) target.getJdbcObject();
				if (stmt != null) {
					cmd.execute(stmt, this);
				} else {
					LOGGER.info("no statement with id " + itemuid + " to cancel");
				}
			} catch (final Exception e) {
				LOGGER.info(e.getMessage(), e);
			}
		} else {
			LOGGER.info("no statement with id " + itemuid + " to cancel");
		}
	}

	public void traceConnectionStatistics() {
		LOGGER.info("  Connection ........... " + connectionConfiguration.getId());
		LOGGER.info("  IP address ........... " + clientInfo.getProperty("wjdbc-client.address", "n.a."));
		LOGGER.info("  Host name ............ " + clientInfo.getProperty("wjdbc-client.name", "n.a."));
		dumpClientInfoProperties();
		LOGGER.info("  Last time of access .. " + new Date(lastAccessTimestamp));
		LOGGER.info("  Processed commands ... " + numberOfProcessedCommands);
		if (!jdbcObjects.isEmpty()) {
			LOGGER.info("  Remaining objects .... " + jdbcObjects.size());
			for (final JdbcObjectHolder<?> jdbcObjectHolder : jdbcObjects.values()) {
				LOGGER.info("  - " + jdbcObjectHolder.getJdbcObject().getClass().getName());
			}
		}
		if (!commandCountMap.isEmpty()) {
			LOGGER.info("  Command-Counts:");
			final List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(commandCountMap.entrySet());
			Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
				@Override
				public int compare(final Entry<String, Integer> o1, final Entry<String, Integer> o2) {
					final Integer v1 = o1.getValue();
					final Integer v2 = o2.getValue();
					// Descending sort
					return -v1.compareTo(v2);
				}
			});

			for (final Entry<String, Integer> entry : entries) {
				final String cmd = entry.getKey();
				final Integer count = entry.getValue();
				LOGGER.info("     " + count + " : " + cmd);
			}
		}
	}

	private StreamingResultSet handleResultSet(final ResultSet result, final boolean forwardOnly) throws SQLException {
		// Populate a StreamingResultSet
		final StreamingResultSet srs = new StreamingResultSet(connectionConfiguration.getRowPacketSize(), forwardOnly,
				connectionConfiguration.isPrefetchResultSetMetaData(), connectionConfiguration.getCharset());
		// Populate it
		final boolean lastPartReached = srs.populate(result);
		// Remember the ResultSet and put the UID in the StreamingResultSet
		final long id = counter.getAndIncrement();
		srs.setRemainingResultSetUID(id);
		jdbcObjects.put(id, new JdbcObjectHolder<ResultSetHolder>(new ResultSetHolder(result, executor, rowPacketSize, lastPartReached),
				JdbcInterfaceType.RESULTSETHOLDER));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Registered ResultSet with UID " + id);
		}
		return srs;
	}

	private static Object handleResultSetMetaData(final ResultSetMetaData result) throws SQLException {
		return new SerialResultSetMetaData(result);
	}

	private void dumpClientInfoProperties() {
		if (LOGGER.isInfoEnabled() && !clientInfo.isEmpty()) {
			boolean printedHeader = false;
			for (final Enumeration<Object> it = clientInfo.keys(); it.hasMoreElements();) {
				final String key = (String) it.nextElement();
				if (!key.startsWith("wjdbc")) {
					if (!printedHeader) {
						printedHeader = true;
						LOGGER.info("  Client-Properties ...");
					}
					LOGGER.info("    " + key + " => " + clientInfo.getProperty(key));
				}
			}
		}
	}

	private void checkAgainstQueryFilters(final String sql) throws SQLException {
		if (connectionConfiguration.getQueryFilters() != null) {
			connectionConfiguration.getQueryFilters().checkAgainstFilters(sql);
		}
	}

	private static int getJdbcInterfaceTypeFromObject(final Object jdbcObject) {
		int jdbcInterfaceType = 0;
		if (jdbcObject == null) {
			return jdbcInterfaceType;
		}
		if (jdbcObject instanceof CallableStatement) {
			jdbcInterfaceType = JdbcInterfaceType.CALLABLESTATEMENT;
		} else if (jdbcObject instanceof Connection) {
			jdbcInterfaceType = JdbcInterfaceType.CONNECTION;
		} else if (jdbcObject instanceof DatabaseMetaData) {
			jdbcInterfaceType = JdbcInterfaceType.DATABASEMETADATA;
		} else if (jdbcObject instanceof PreparedStatement) {
			jdbcInterfaceType = JdbcInterfaceType.PREPAREDSTATEMENT;
		} else if (jdbcObject instanceof Savepoint) {
			jdbcInterfaceType = JdbcInterfaceType.SAVEPOINT;
		} else if (jdbcObject instanceof Statement) {
			jdbcInterfaceType = JdbcInterfaceType.STATEMENT;
		} else if (jdbcObject instanceof ResultSetHolder) {
			jdbcInterfaceType = JdbcInterfaceType.RESULTSETHOLDER;
		}
		return jdbcInterfaceType;
	}
}
