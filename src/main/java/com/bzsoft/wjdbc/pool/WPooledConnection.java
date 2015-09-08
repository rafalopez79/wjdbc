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

public class WPooledConnection implements PooledConnection, PooledConnectionSubject, StatementEventListener {

	private WConnectionPooled		connection;
	private final List<StatementEventListener>	statementEventListeners;
	private final List<ConnectionEventListener>	connectionEventListeners;
	private final Set<PreparedStatement>	preparedStatements;

	private WPooledConnection() {
		statementEventListeners = new LinkedList<StatementEventListener>();
		connectionEventListeners = new LinkedList<ConnectionEventListener>();
		preparedStatements = new HashSet<PreparedStatement>();
		addStatementEventListener(this);
	}

	public WPooledConnection(final Connection connection) throws SQLException {
		this();
		if (connection == null) {
			throw new SQLException("Connection must be not null");
		}
		this.connection = new WConnectionPooled(connection, this);

	}

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
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return connection;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void addConnectionEventListener(final ConnectionEventListener listener) {
		synchronized (connectionEventListeners) {
			connectionEventListeners.add(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void removeConnectionEventListener(final ConnectionEventListener listener) {
		synchronized (connectionEventListeners) {
			connectionEventListeners.add(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void addStatementEventListener(final StatementEventListener listener) {
		synchronized (statementEventListeners) {
			statementEventListeners.add(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void removeStatementEventListener(final StatementEventListener listener) {
		synchronized (statementEventListeners) {
			statementEventListeners.remove(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connectionClosed(final SQLException e) {
		final List<ConnectionEventListener> list;
		synchronized (connectionEventListeners) {
			list = new LinkedList<ConnectionEventListener>(connectionEventListeners);
		}
		final ConnectionEvent event = new ConnectionEvent(this, e);
		for (final ConnectionEventListener l : list) {
			l.connectionClosed(event);
		}

	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void connectionErrorOccurred(final SQLException e) {
		final List<ConnectionEventListener> list;
		synchronized (connectionEventListeners) {
			list = new LinkedList<ConnectionEventListener>(connectionEventListeners);
		}
		final ConnectionEvent event = new ConnectionEvent(this, e);
		for (final ConnectionEventListener l : list) {
			l.connectionErrorOccurred(event);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void statementClosed(final PreparedStatement stmt, final SQLException e) {
		final List<StatementEventListener> list;
		synchronized (statementEventListeners) {
			list = new LinkedList<StatementEventListener>(statementEventListeners);
		}
		final StatementEvent event = new StatementEvent(this, stmt, e);
		for (final StatementEventListener l : list) {
			l.statementClosed(event);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void addStatement(final PreparedStatement pstmt) {
		//empty
	}

	/**
	 * {@inheritDoc}
	 *
	 *
	 */
	@Override
	public void statementClosed(final StatementEvent event) {
		try {
			event.getStatement().close();
		} catch (final SQLException e) {
			//empty
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void statementErrorOccurred(final StatementEvent event) {
		//empty
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void statementErrorOccurred(final PreparedStatement stmt, final SQLException e) {
		final List<StatementEventListener> list;
		synchronized (statementEventListeners) {
			list = new LinkedList<StatementEventListener>(statementEventListeners);
		}
		final StatementEvent event = new StatementEvent(this, stmt, e);
		for (final StatementEventListener l : list) {
			l.statementErrorOccurred(event);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void close() throws SQLException {
		try {
			connection.getInnerConnection().close();
		} catch (final SQLException e) {
			throw e;
		} catch (final Throwable t) {
			throw new SQLException(t);
		} finally {
			connection = null;
		}
	}
}
