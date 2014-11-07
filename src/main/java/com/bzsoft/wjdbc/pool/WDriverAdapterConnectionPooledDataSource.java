package com.bzsoft.wjdbc.pool;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

import com.bzsoft.wjdbc.WDriver;

public class WDriverAdapterConnectionPooledDataSource implements ConnectionPoolDataSource, Referenceable, Serializable {

	private static final long		serialVersionUID							= 1L;

	private static final String	GET_CONNECTION_CALLED					= "A PooledConnection was already requested from this source, "
																									+ "further initialization is not allowed.";

	private String						description;
	private String						password;
	private String						url;
	private String						user;
	private int							loginTimeout;

	/** Whether or not getConnection has been called */
	private volatile boolean		getConnectionCalled						= false;

	/** Connection properties passed to JDBC Driver */
	private Properties				connectionProperties						= null;

	static {
		// Attempt to prevent deadlocks - see DBCP - 272
		DriverManager.getDrivers();
	}

	/**
	 * Controls access to the underlying connection
	 */
	private boolean					accessToUnderlyingConnectionAllowed	= false;

	private PrintWriter				logWriter;

	/**
	 * Default no-arg constructor for Serialization
	 */
	public WDriverAdapterConnectionPooledDataSource() {
		new WDriver();
	}

	/**
	 * Attempt to establish a database connection using the default user and
	 * password.
	 */
	@Override
	public PooledConnection getPooledConnection() throws SQLException {
		return getPooledConnection(getUser(), getPassword());
	}

	/**
	 * Attempt to establish a database connection.
	 * 
	 * @param username
	 *           name to be used for the connection
	 * @param pass
	 *           password to be used fur the connection
	 */
	@Override
	public PooledConnection getPooledConnection(final String username, final String pass) throws SQLException {
		getConnectionCalled = true;

		/**
		 * TODO ELIMINADO YA QUE ES PARA POOLS DE PREPAREDSTATEMENTS
		 * 
		 * KeyedObjectPool stmtPool = null; if (isPoolPreparedStatements()) { if
		 * (getMaxPreparedStatements() <= 0) { // since there is no limit, create
		 * a prepared statement pool with an eviction thread // evictor settings
		 * are the same as the connection pool settings. stmtPool = new
		 * GenericKeyedObjectPool(null, getMaxActive(),
		 * GenericKeyedObjectPool.WHEN_EXHAUSTED_GROW, 0, getMaxIdle(), false,
		 * false, getTimeBetweenEvictionRunsMillis(),getNumTestsPerEvictionRun(),
		 * getMinEvictableIdleTimeMillis(), false); } else { // since there is
		 * limit, create a prepared statement pool without an eviction thread //
		 * pool has LRU functionality so when the limit is reached, 15% of the
		 * pool is cleared. // see
		 * org.apache.commons.pool.impl.GenericKeyedObjectPool.clearOldest method
		 * stmtPool = new GenericKeyedObjectPool(null, getMaxActive(),
		 * GenericKeyedObjectPool.WHEN_EXHAUSTED_GROW, 0, getMaxIdle(),
		 * getMaxPreparedStatements(), false, false, -1,0,0, // -1 tells the pool
		 * that there should be no eviction thread. false); } }
		 **/

		// Workaround for buggy WebLogic 5.1 classloader - ignore the
		// exception upon first invocation.
		try {
			Connection2PooledConnection pci = null;
			if (connectionProperties != null) {
				connectionProperties.put("user", username);
				connectionProperties.put("password", pass);
				pci = new Connection2PooledConnection(DriverManager.getConnection(getUrl(), connectionProperties));
			} else {
				pci = new Connection2PooledConnection(DriverManager.getConnection(getUrl(), username, pass));
			}
			return pci;
		} catch (final ClassCircularityError e) {
			Connection2PooledConnection pci = null;
			if (connectionProperties != null) {
				pci = new Connection2PooledConnection(DriverManager.getConnection(getUrl(), connectionProperties));
			} else {
				pci = new Connection2PooledConnection(DriverManager.getConnection(getUrl(), username, pass));
			}
			return pci;
		}
	}

	/**
	 * Throws an IllegalStateException, if a PooledConnection has already been
	 * requested.
	 */
	private void assertInitializationAllowed() throws IllegalStateException {
		if (getConnectionCalled) {
			throw new IllegalStateException(GET_CONNECTION_CALLED);
		}
	}

	// ----------------------------------------------------------------------
	// Properties

	/**
	 * Get the connection properties passed to the JDBC driver.
	 * 
	 * @return the JDBC connection properties used when creating connections.
	 * @since 1.3
	 */
	public Properties getConnectionProperties() {
		return connectionProperties;
	}

	/**
	 * <p>
	 * Set the connection properties passed to the JDBC driver.
	 * </p>
	 * 
	 * <p>
	 * If <code>props</code> contains "user" and/or "password" properties, the
	 * corresponding instance properties are set. If these properties are not
	 * present, they are filled in using {@link #getUser()},
	 * {@link #getPassword()} when {@link #getPooledConnection()} is called, or
	 * using the actual parameters to the method call when
	 * {@link #getPooledConnection(String, String)} is called. Calls to
	 * {@link #setUser(String)} or {@link #setPassword(String)} overwrite the
	 * values of these properties if <code>connectionProperties</code> is not
	 * null.
	 * </p>
	 * 
	 * @param props
	 *           Connection properties to use when creating new connections.
	 * @since 1.3
	 * @throws IllegalStateException
	 *            if {@link #getPooledConnection()} has been called
	 */
	public void setConnectionProperties(final Properties props) {
		assertInitializationAllowed();
		connectionProperties = props;
		if (connectionProperties.containsKey("user")) {
			setUser(connectionProperties.getProperty("user"));
		}
		if (connectionProperties.containsKey("password")) {
			setPassword(connectionProperties.getProperty("password"));
		}
	}

	/**
	 * Get the value of description. This property is here for use by the code
	 * which will deploy this datasource. It is not used internally.
	 * 
	 * @return value of description, may be null.
	 * @see #setDescription(String)
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the value of description. This property is here for use by the code
	 * which will deploy this datasource. It is not used internally.
	 * 
	 * @param v
	 *           Value to assign to description.
	 */
	public void setDescription(final String v) {
		description = v;
	}

	/**
	 * Get the value of password for the default user.
	 * 
	 * @return value of password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the value of password for the default user.
	 * 
	 * @param v
	 *           Value to assign to password.
	 * @throws IllegalStateException
	 *            if {@link #getPooledConnection()} has been called
	 */
	public void setPassword(final String v) {
		assertInitializationAllowed();
		password = v;
		if (connectionProperties != null) {
			connectionProperties.setProperty("password", v);
		}
	}

	/**
	 * Get the value of url used to locate the database for this datasource.
	 * 
	 * @return value of url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the value of url used to locate the database for this datasource.
	 * 
	 * @param v
	 *           Value to assign to url.
	 * @throws IllegalStateException
	 *            if {@link #getPooledConnection()} has been called
	 */
	public void setUrl(final String v) {
		assertInitializationAllowed();
		url = v;
	}

	/**
	 * Get the value of default user (login or username).
	 * 
	 * @return value of user.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Set the value of default user (login or username).
	 * 
	 * @param v
	 *           Value to assign to user.
	 * @throws IllegalStateException
	 *            if {@link #getPooledConnection()} has been called
	 */
	public void setUser(final String v) {
		assertInitializationAllowed();
		user = v;
		if (connectionProperties != null) {
			connectionProperties.setProperty("user", v);
		}
	}

	/**
	 * Gets the maximum time in seconds that this data source can wait while
	 * attempting to connect to a database. NOT USED.
	 */
	@Override
	public int getLoginTimeout() {
		return loginTimeout;
	}

	/**
	 * Sets the maximum time in seconds that this data source will wait while
	 * attempting to connect to a database. NOT USED.
	 */
	@Override
	public void setLoginTimeout(final int seconds) {
		loginTimeout = seconds;
	}

	/**
	 * Returns the value of the accessToUnderlyingConnectionAllowed property.
	 * 
	 * @return true if access to the underlying is allowed, false otherwise.
	 */
	public synchronized boolean isAccessToUnderlyingConnectionAllowed() {
		return accessToUnderlyingConnectionAllowed;
	}

	/**
	 * Sets the value of the accessToUnderlyingConnectionAllowed property. It
	 * controls if the PoolGuard allows access to the underlying connection.
	 * (Default: false)
	 * 
	 * @param allow
	 *           Access to the underlying connection is granted when true.
	 */
	public synchronized void setAccessToUnderlyingConnectionAllowed(final boolean allow) {
		accessToUnderlyingConnectionAllowed = allow;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}

	@Override
	public void setLogWriter(final PrintWriter out) throws SQLException {
		logWriter = out;

	}

	@Override
	public Reference getReference() throws NamingException {
		// this class implements its own factory
		final String factory = getClass().getName();
		final Reference ref = new Reference(getClass().getName(), factory, null);
		ref.add(new StringRefAddr("description", getDescription()));
		ref.add(new StringRefAddr("driver", WDriver.class.getSimpleName()));
		ref.add(new StringRefAddr("loginTimeout", String.valueOf(getLoginTimeout())));
		ref.add(new StringRefAddr("password", getPassword()));
		ref.add(new StringRefAddr("user", getUser()));
		ref.add(new StringRefAddr("url", getUrl()));
		return ref;
	}
}
