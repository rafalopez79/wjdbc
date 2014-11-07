package com.bzsoft.wjdbc.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import javax.sql.PooledConnection;

public class Statement4ConnectionPooled implements Statement {

	private Statement						statement;
	private PooledConnectionSubject	subject;

	private Statement4ConnectionPooled() {

	}

	public Statement4ConnectionPooled(final Statement statement, final PooledConnectionSubject subject) {
		this();
		this.statement = statement;
		this.subject = subject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		if (getSubject() != null) {
			sb.append("subject=");
			sb.append(getSubject());
			sb.append(", ");
		}
		if (statement != null) {
			sb.append("statement=");
			sb.append(statement);
		}
		sb.append("}");
		return sb.toString();
	}

	protected PooledConnectionSubject getSubject() {
		return subject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return statement.unwrap(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return statement.isWrapperFor(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeQuery(java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(final String sql) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeQuery(sql);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	@Override
	public int executeUpdate(final String sql) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeUpdate(sql);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#close()
	 */
	@Override
	public void close() throws SQLException {
		statementClosed(statement, null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	@Override
	public int getMaxFieldSize() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getMaxFieldSize();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	@Override
	public void setMaxFieldSize(final int max) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setMaxFieldSize(max);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getMaxRows()
	 */
	@Override
	public int getMaxRows() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getMaxRows();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	@Override
	public void setMaxRows(final int max) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setMaxRows(max);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	@Override
	public void setEscapeProcessing(final boolean enable) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setEscapeProcessing(enable);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	@Override
	public int getQueryTimeout() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getQueryTimeout();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	@Override
	public void setQueryTimeout(final int seconds) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setQueryTimeout(seconds);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#cancel()
	 */
	@Override
	public void cancel() throws SQLException {
		SQLException sqle = null;
		try {
			statement.cancel();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getWarnings();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		SQLException sqle = null;
		try {
			statement.clearWarnings();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setCursorName(java.lang.String)
	 */
	@Override
	public void setCursorName(final String name) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setCursorName(name);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	@Override
	public boolean execute(final String sql) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.execute(sql);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getResultSet()
	 */
	@Override
	public ResultSet getResultSet() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getResultSet();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getUpdateCount()
	 */
	@Override
	public int getUpdateCount() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getUpdateCount();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getMoreResults()
	 */
	@Override
	public boolean getMoreResults() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getMoreResults();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	@Override
	public void setFetchDirection(final int direction) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setFetchDirection(direction);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getFetchDirection()
	 */
	@Override
	public int getFetchDirection() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getFetchDirection();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	@Override
	public void setFetchSize(final int rows) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setFetchSize(rows);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getFetchSize()
	 */
	@Override
	public int getFetchSize() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getFetchSize();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	@Override
	public int getResultSetConcurrency() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getResultSetConcurrency();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getResultSetType()
	 */
	@Override
	public int getResultSetType() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getResultSetType();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	@Override
	public void addBatch(final String sql) throws SQLException {
		SQLException sqle = null;
		try {
			statement.addBatch(sql);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#clearBatch()
	 */
	@Override
	public void clearBatch() throws SQLException {
		SQLException sqle = null;
		try {
			statement.clearBatch();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeBatch()
	 */
	@Override
	public int[] executeBatch() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeBatch();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return ((PooledConnection) subject).getConnection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	@Override
	public boolean getMoreResults(final int current) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getMoreResults(current);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getGeneratedKeys();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */
	@Override
	public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeUpdate(sql, autoGeneratedKeys);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */
	@Override
	public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeUpdate(sql, columnIndexes);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String,
	 *      java.lang.String[])
	 */
	@Override
	public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeUpdate(sql, columnNames);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */
	@Override
	public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.execute(sql, autoGeneratedKeys);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
	@Override
	public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.execute(sql, columnIndexes);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean execute(final String sql, final String[] columnNames) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.execute(sql, columnNames);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	@Override
	public int getResultSetHoldability() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getResultSetHoldability();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.isClosed();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setPoolable(boolean)
	 */
	@Override
	public void setPoolable(final boolean poolable) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setPoolable(poolable);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#isPoolable()
	 */
	@Override
	public boolean isPoolable() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.isPoolable();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	private void statementClosed(final Statement stmt, final SQLException e) throws SQLException {
		if (stmt instanceof PreparedStatement) {
			subject.statementClosed((PreparedStatement) stmt, e);
		} else {
			SQLException sqle = null;
			try {
				stmt.close();
			} catch (final SQLException ex) {
				sqle = ex;
				throw sqle;
			} catch (final Throwable th) {
				sqle = new SQLException(th);
				throw sqle;
			}
		}
	}

	protected void statementErrorOccurred(final Statement stmt, final SQLException e) {
		if (stmt instanceof PreparedStatement) {
			subject.statementErrorOccurred((PreparedStatement) stmt, e);
		}
	}
}
