package com.bzsoft.wjdbc.pool;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

public class Connection4ConnectionPooled implements Connection {

	private PooledConnectionSubject	subject;
	private Connection					connection;

	private Connection4ConnectionPooled() {

	}

	public Connection4ConnectionPooled(final Connection connection, final PooledConnectionSubject subject) {
		this();
		this.connection = connection;
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
		if (subject != null) {
			sb.append("subject=");
			sb.append(subject);
			sb.append(", ");
		}
		if (connection != null) {
			sb.append("connection=");
			sb.append(connection);
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return connection.unwrap(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return connection.isWrapperFor(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createStatement()
	 */
	@Override
	public Statement createStatement() throws SQLException {
		SQLException sqle = null;
		try {
			final Statement stmt = connection.createStatement();
			final Statement wrapper = new Statement4ConnectionPooled(stmt, subject);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	@Override
	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		SQLException sqle = null;
		try {
			final PreparedStatement pstmt = connection.prepareStatement(sql);
			final PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	@Override
	public CallableStatement prepareCall(final String sql) throws SQLException {
		SQLException sqle = null;
		try {
			final CallableStatement cstmt = connection.prepareCall(sql);
			final CallableStatement wrapper = new CallableStatement4ConnectionPooled(cstmt, subject);
			subject.addStatement(cstmt);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
	@Override
	public String nativeSQL(final String sql) throws SQLException {
		SQLException sqle = null;
		try {
			return connection.nativeSQL(sql);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	@Override
	public void setAutoCommit(final boolean autoCommit) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setAutoCommit(autoCommit);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getAutoCommit()
	 */
	@Override
	public boolean getAutoCommit() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getAutoCommit();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#commit()
	 */
	@Override
	public void commit() throws SQLException {
		SQLException sqle = null;
		try {
			connection.commit();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#rollback()
	 */
	@Override
	public void rollback() throws SQLException {
		SQLException sqle = null;
		try {
			connection.rollback();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#close()
	 */
	@Override
	public void close() throws SQLException {
		subject.connectionClosed(null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.isClosed();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getMetaData()
	 */
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getMetaData();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(final boolean readOnly) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setReadOnly(readOnly);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.isReadOnly();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setCatalog(java.lang.String)
	 */
	@Override
	public void setCatalog(final String catalog) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setCatalog(catalog);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getCatalog()
	 */
	@Override
	public String getCatalog() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getCatalog();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	@Override
	public void setTransactionIsolation(final int level) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setTransactionIsolation(level);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	@Override
	public int getTransactionIsolation() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getTransactionIsolation();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getWarnings();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		SQLException sqle = null;
		try {
			connection.clearWarnings();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
		SQLException sqle = null;
		try {
			final Statement stmt = connection.createStatement(resultSetType, resultSetConcurrency);
			final Statement wrapper = new Statement4ConnectionPooled(stmt, subject);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		SQLException sqle = null;
		try {
			final PreparedStatement pstmt = connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
			final PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		SQLException sqle = null;
		try {
			final CallableStatement cstmt = connection.prepareCall(sql, resultSetType, resultSetConcurrency);
			final CallableStatement wrapper = new CallableStatement4ConnectionPooled(cstmt, subject);
			subject.addStatement(cstmt);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getTypeMap()
	 */
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getTypeMap();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setTypeMap(java.util.Map)
	 */
	@Override
	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setTypeMap(map);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setHoldability(int)
	 */
	@Override
	public void setHoldability(final int holdability) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setHoldability(holdability);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getHoldability()
	 */
	@Override
	public int getHoldability() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getHoldability();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setSavepoint()
	 */
	@Override
	public Savepoint setSavepoint() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.setSavepoint();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	 */
	@Override
	public Savepoint setSavepoint(final String name) throws SQLException {
		SQLException sqle = null;
		try {
			return connection.setSavepoint(name);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	 */
	@Override
	public void rollback(final Savepoint savepoint) throws SQLException {
		SQLException sqle = null;
		try {
			connection.rollback(savepoint);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	@Override
	public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
		SQLException sqle = null;
		try {
			connection.releaseSavepoint(savepoint);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
		SQLException sqle = null;
		try {
			final Statement stmt = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
			final Statement wrapper = new Statement4ConnectionPooled(stmt, subject);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
	 */
	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		SQLException sqle = null;
		try {
			final PreparedStatement pstmt = connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
			final PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		SQLException sqle = null;
		try {
			final CallableStatement cstmt = connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
			final CallableStatement wrapper = new CallableStatement4ConnectionPooled(cstmt, subject);
			subject.addStatement(cstmt);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	@Override
	public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
		SQLException sqle = null;
		try {
			final PreparedStatement pstmt = connection.prepareStatement(sql, autoGeneratedKeys);
			final PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	@Override
	public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
		SQLException sqle = null;
		try {
			final PreparedStatement pstmt = connection.prepareStatement(sql, columnIndexes);
			final PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String,
	 *      java.lang.String[])
	 */
	@Override
	public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
		SQLException sqle = null;
		try {
			final PreparedStatement pstmt = connection.prepareStatement(sql, columnNames);
			final PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createClob()
	 */
	@Override
	public Clob createClob() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createClob();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createBlob()
	 */
	@Override
	public Blob createBlob() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createBlob();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createNClob()
	 */
	@Override
	public NClob createNClob() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createNClob();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createSQLXML()
	 */
	@Override
	public SQLXML createSQLXML() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createSQLXML();
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#isValid(int)
	 */
	@Override
	public boolean isValid(final int timeout) throws SQLException {
		SQLException sqle = null;
		try {
			return connection.isValid(timeout);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
		SQLClientInfoException sqle = null;
		try {
			connection.setClientInfo(name, value);
		} catch (final SQLClientInfoException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLClientInfoException();
			sqle.initCause(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setClientInfo(java.util.Properties)
	 */
	@Override
	public void setClientInfo(final Properties properties) throws SQLClientInfoException {
		SQLClientInfoException sqle = null;
		try {
			connection.setClientInfo(properties);
		} catch (final SQLClientInfoException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLClientInfoException();
			sqle.initCause(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getClientInfo(java.lang.String)
	 */
	@Override
	public String getClientInfo(final String name) throws SQLException {
		SQLException sqle = null;
		String props = null;
		try {
			props = connection.getClientInfo(name);
			return props;
		} catch (final SQLException e) {
			sqle = e;
			// throw sqle;
			return props;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			// throw sqle;
			return props;
		} finally {
			if (null != sqle) {
				sqle.printStackTrace(System.out);
				// subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getClientInfo()
	 */
	@Override
	public Properties getClientInfo() throws SQLException {
		SQLException sqle = null;
		Properties props = null;
		try {
			System.out.println("[VLM] " + connection);
			props = connection.getClientInfo();
			return props;
		} catch (final SQLException e) {
			sqle = e;
			System.out.println("[VLM] SQLException");
			// throw sqle;
			return props;
		} catch (final Throwable th) {
			System.out.println("[VLM] Throwable");
			sqle = new SQLException(th);
			// throw sqle;
			return props;
		} finally {
			if (null != sqle) {
				sqle.printStackTrace(System.out);
				// subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createArrayOf(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Override
	public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createArrayOf(typeName, elements);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createStruct(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Override
	public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createStruct(typeName, attributes);
		} catch (final SQLException e) {
			sqle = e;
			throw sqle;
		} catch (final Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	protected Connection getInnerConnection() {
		return connection;
	}

}
