package com.bzsoft.wjdbc.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEvent;
import javax.sql.StatementEventListener;

//TODO: rewrite synchronization
public class Connection2PooledConnection implements PooledConnection, PooledConnectionSubject, StatementEventListener {

	private Connection4ConnectionPooled		connection;
	private List<StatementEventListener>	statementEventListeners;
	private List<ConnectionEventListener>	connectionEventListeners;
	private final Set<PreparedStatement>	preparedStatements;

	private Connection2PooledConnection() {
		statementEventListeners = new LinkedList<StatementEventListener>();
		connectionEventListeners = new LinkedList<ConnectionEventListener>();
		preparedStatements = new HashSet<PreparedStatement>();
		addStatementEventListener(this);
	}

	public Connection2PooledConnection(final Connection connection) throws SQLException {
		this();
		if (connection == null) {
			throw new SQLException("Connection must be not null");
		}
		this.connection = new Connection4ConnectionPooled(connection, this);

	}

	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		if (connection != null) {
			sb.append("connection=");
			sb.append(connection);
			sb.append(", ");
		}
		if (statementEventListeners != null) {
			sb.append("statementEventListeners=");
			sb.append(statementEventListeners.size());
			sb.append(", ");
		}
		if (connectionEventListeners != null) {
			sb.append("connectionEventListeners=");
			sb.append(connectionEventListeners.size());
			sb.append(", ");
		}
		if (preparedStatements != null) {
			sb.append("preparedStatements=");
			sb.append(preparedStatements.size());
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.PooledConnection#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return connection;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.PooledConnection#addConnectionEventListener(javax.sql.
	 *      ConnectionEventListener)
	 */
	@Override
	public void addConnectionEventListener(final ConnectionEventListener listener) {
		synchronized (connectionEventListeners) {
			final List<ConnectionEventListener> list = new LinkedList<ConnectionEventListener>(connectionEventListeners);
			list.add(listener);
			connectionEventListeners = list;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.PooledConnection#removeConnectionEventListener(javax.sql.
	 *      ConnectionEventListener)
	 */
	@Override
	public void removeConnectionEventListener(final ConnectionEventListener listener) {
		synchronized (connectionEventListeners) {
			final List<ConnectionEventListener> list = new LinkedList<ConnectionEventListener>(connectionEventListeners);
			list.remove(listener);
			connectionEventListeners = list;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.PooledConnection#addStatementEventListener(javax.sql.
	 *      StatementEventListener)
	 */
	@Override
	public void addStatementEventListener(final StatementEventListener listener) {
		synchronized (statementEventListeners) {
			final List<StatementEventListener> list = new LinkedList<StatementEventListener>(statementEventListeners);
			list.add(listener);
			statementEventListeners = list;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.PooledConnection#removeStatementEventListener(javax.sql.
	 *      StatementEventListener)
	 */
	@Override
	public void removeStatementEventListener(final StatementEventListener listener) {
		synchronized (statementEventListeners) {
			final List<StatementEventListener> list = new LinkedList<StatementEventListener>(statementEventListeners);
			list.remove(listener);
			statementEventListeners = list;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see PooledConnectionSubject#connectionClosed (javax.sql.ConnectionEvent)
	 */
	@Override
	public void connectionClosed(final SQLException e) {
		List<ConnectionEventListener> list = null;
		synchronized (connectionEventListeners) {
			list = connectionEventListeners;
		}
		final ConnectionEvent event = new ConnectionEvent(this, e);
		for (final ConnectionEventListener l : list) {
			l.connectionClosed(event);
		}

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see PooledConnectionSubject#
	 *      connectionErrorOccurred(javax.sql.ConnectionEvent)
	 */
	@Override
	public void connectionErrorOccurred(final SQLException e) {
		List<ConnectionEventListener> list = null;
		synchronized (connectionEventListeners) {
			list = connectionEventListeners;
		}
		final ConnectionEvent event = new ConnectionEvent(this, e);
		for (final ConnectionEventListener l : list) {
			l.connectionErrorOccurred(event);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see PooledConnectionSubject#statementClosed (javax.sql.StatementEvent)
	 */
	@Override
	public void statementClosed(final PreparedStatement stmt, final SQLException e) {
		List<StatementEventListener> list = null;
		synchronized (statementEventListeners) {
			list = statementEventListeners;
		}
		final StatementEvent event = new StatementEvent(this, stmt, e);
		for (final StatementEventListener l : list) {
			l.statementClosed(event);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see PooledConnectionSubject#addStatement(java.sql.PreparedStatement)
	 */
	@Override
	public void addStatement(final PreparedStatement pstmt) {
		if (null == preparedStatements) {
			return;
		}
		System.out.println("addStatement(" + pstmt + ")");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.StatementEventListener#statementClosed(javax.sql.StatementEvent
	 *      )
	 */
	@Override
	public void statementClosed(final StatementEvent event) {
		System.out.println("statementClosed(" + event + ")");
		try {
			event.getStatement().close();
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.StatementEventListener#statementErrorOccurred(javax.sql.StatementEvent)
	 */
	@Override
	public void statementErrorOccurred(final StatementEvent event) {
		System.out.println("statementErrorOccurred(" + event + ")");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see PooledConnectionSubject#statementErrorOccurred
	 *      (javax.sql.StatementEvent)
	 */
	@Override
	public void statementErrorOccurred(final PreparedStatement stmt, final SQLException e) {
		List<StatementEventListener> list = null;
		synchronized (statementEventListeners) {
			list = statementEventListeners;
		}
		final StatementEvent event = new StatementEvent(this, stmt, e);
		for (final StatementEventListener l : list) {
			l.statementErrorOccurred(event);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.PooledConnection#close()
	 */
	@Override
	public void close() throws SQLException {
		SQLException th = null;
		try {
			connection.getInnerConnection().close();
		} catch (final SQLException e) {
			th = e;
			throw th;
		} catch (final Throwable t) {
			th = new SQLException(t);
			throw th;
		} finally {
			connection = null;
		}
	}
}
