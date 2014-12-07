package com.bzsoft.wjdbc.server.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import com.bzsoft.wjdbc.WJdbcException;
import com.bzsoft.wjdbc.server.DataSourceProvider;

public class ConnectionConfiguration {

	private static final Logger				LOGGER								= Logger.getLogger(ConnectionConfiguration.class);
	private static final String				DBCPID								= "jdbc:apache:commons:dbcp:";

	// Basic properties
	protected String								id;
	protected String								driver;
	protected String								url;
	protected String								dataSourceProvider;
	protected String								user;
	protected String								password;
	protected String								wjdbcUser;
	protected String								wjdbcPassword;
	// Trace properties
	protected boolean								traceCommandCount					= false;
	protected boolean								traceOrphanedObjects				= false;
	// Row-Packet size defines the number of rows that is transported in one
	// packet
	protected int									rowPacketSize						= 200;
	// Encoding for strings
	protected String								charset								= "ISO-8859-1";
	// Connection pooling
	protected boolean								connectionPooling					= true;
	protected ConnectionPoolConfiguration	connectionPoolConfiguration	= null;
	// Fetch the metadata of a resultset immediately after constructing
	protected boolean								prefetchResultSetMetaData		= false;

	// Query filters
	protected QueryFilterConfiguration		queryFilters;
	// Connection pooling support
	private boolean								driverInitialized					= false;
	private Boolean								connectionPoolInitialized		= Boolean.FALSE;
	private GenericObjectPool					connectionPool						= null;

	public ConnectionConfiguration() {
		// empty
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(final String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getDataSourceProvider() {
		return dataSourceProvider;
	}

	public void setDataSourceProvider(final String dataSourceProvider) {
		this.dataSourceProvider = dataSourceProvider;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public void setWjdbcPassword(final String wjdbcPassword) {
		this.wjdbcPassword = wjdbcPassword;
	}

	public void setWjdbcUser(final String wjdbcUser) {
		this.wjdbcUser = wjdbcUser;
	}

	public String getWjdbcPassword() {
		return wjdbcPassword;
	}

	public String getWjdbcUser() {
		return wjdbcUser;
	}

	public boolean isTraceCommandCount() {
		return traceCommandCount;
	}

	public void setTraceCommandCount(final boolean traceCommandCount) {
		this.traceCommandCount = traceCommandCount;
	}

	public boolean isTraceOrphanedObjects() {
		return traceOrphanedObjects;
	}

	public void setTraceOrphanedObjects(final boolean traceOrphanedObjects) {
		this.traceOrphanedObjects = traceOrphanedObjects;
	}

	public int getRowPacketSize() {
		return rowPacketSize;
	}

	public void setRowPacketSize(final int rowPacketSize) {
		this.rowPacketSize = rowPacketSize;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(final String charset) {
		this.charset = charset;
	}

	public boolean useConnectionPooling() {
		return connectionPooling;
	}

	public void setConnectionPooling(final boolean connectionPooling) {
		this.connectionPooling = connectionPooling;
	}

	public ConnectionPoolConfiguration getConnectionPoolConfiguration() {
		return connectionPoolConfiguration;
	}

	public void setConnectionPoolConfiguration(final ConnectionPoolConfiguration connectionPoolConfiguration) {
		this.connectionPoolConfiguration = connectionPoolConfiguration;
		connectionPooling = true;
	}

	public boolean isPrefetchResultSetMetaData() {
		return prefetchResultSetMetaData;
	}

	public void setPrefetchResultSetMetaData(final boolean fetchResultSetMetaData) {
		prefetchResultSetMetaData = fetchResultSetMetaData;
	}

	public QueryFilterConfiguration getQueryFilters() {
		return queryFilters;
	}

	public void setQueryFilters(final QueryFilterConfiguration queryFilters) {
		this.queryFilters = queryFilters;
	}

	protected void validate() throws ConfigurationException {
		if (url == null && dataSourceProvider == null) {
			final String msg = "Connection-Entry " + id + ": neither URL nor DataSourceProvider is provided";
			LOGGER.error(msg);
			throw new ConfigurationException(msg);
		}
		// When connection pooling is used, the user/password combination must be
		// provided in the configuration as otherwise user-accounts are mixed up
		if (dataSourceProvider == null) {
			if (connectionPooling && user == null) {
				final String msg = "Connection-Entry " + id
						+ ": connection pooling can only be used when a dedicated user is specified for the connection";
				LOGGER.error(msg);
				throw new ConfigurationException(msg);
			}
		}
	}

	protected void log() {
		String usedPassword = "provided by client";
		if (password != null) {
			final char[] hiddenPassword = new char[password.length()];
			for (int i = 0; i < password.length(); i++) {
				hiddenPassword[i] = '*';
			}
			usedPassword = String.valueOf(hiddenPassword);
		}
		String userWJDBCPassword = null;
		if (wjdbcPassword != null) {
			final char[] hiddenPassword = new char[wjdbcPassword.length()];
			for (int i = 0; i < wjdbcPassword.length(); i++) {
				hiddenPassword[i] = '*';
			}
			userWJDBCPassword = String.valueOf(hiddenPassword);
		}
		LOGGER.info("Connection-Configuration '" + id + "'");
		// We must differentiate between the DataSource-API and the older
		// DriverManager-API. When the DataSource-Provider is provided, the
		// driver and URL configurations will be ignored
		if (dataSourceProvider != null) {
			LOGGER.info("  DataSource-Provider ........ " + dataSourceProvider);
		} else {
			if (driver != null) {
				LOGGER.info("  Driver ..................... " + driver);
			}
			LOGGER.info("  URL ........................ " + url);
		}
		LOGGER.info("  User ....................... " + (user != null ? user : "provided by client"));
		LOGGER.info("  Password ................... " + usedPassword);
		LOGGER.info("  WJDBCUser .................. " + (wjdbcUser != null ? wjdbcUser : "provided by client"));
		LOGGER.info("  WJDBPassword ............... " + userWJDBCPassword);
		LOGGER.info("  Row-Packetsize ............. " + rowPacketSize);
		LOGGER.info("  Charset .................... " + charset);
		LOGGER.info("  Connection-Pool ............ " + (connectionPooling ? "on" : "off"));
		LOGGER.info("  Pre-Fetch ResultSetMetaData  " + (prefetchResultSetMetaData ? "on" : "off"));
		LOGGER.info("  Trace Command-Counts ....... " + traceCommandCount);
		LOGGER.info("  Trace Orphaned-Objects ..... " + traceOrphanedObjects);
		if (connectionPoolConfiguration != null) {
			connectionPoolConfiguration.log();
		}
		if (queryFilters != null) {
			queryFilters.log();
		}
	}

	public Connection create(final Properties props) throws SQLException, WJdbcException {
		if (dataSourceProvider != null) {
			return createConnectionViaDataSource();
		}
		return createConnectionViaDriverManager(props);
	}

	protected Connection createConnectionViaDataSource() throws SQLException {
		Connection result;

		LOGGER.debug("Creating DataSourceFactory from class " + dataSourceProvider);

		try {
			final Class<?> clsDataSourceProvider = Class.forName(dataSourceProvider);
			final DataSourceProvider dsp = (DataSourceProvider) clsDataSourceProvider.newInstance();
			LOGGER.debug("DataSourceProvider created");
			final DataSource dataSource = dsp.getDataSource();
			LOGGER.debug("Retrieving connection from DataSource");
			if (user != null) {
				result = dataSource.getConnection(user, password);
			} else {
				result = dataSource.getConnection();
			}
			LOGGER.debug("... Connection successfully retrieved");
		} catch (final ClassNotFoundException e) {
			final String msg = "DataSourceProvider-Class " + dataSourceProvider + " not found";
			LOGGER.error(msg, e);
			throw new SQLException(msg);
		} catch (final InstantiationException e) {
			final String msg = "Failed to create DataSourceProvider";
			LOGGER.error(msg, e);
			throw new SQLException(msg);
		} catch (final IllegalAccessException e) {
			final String msg = "Can't access DataSourceProvider";
			LOGGER.error(msg, e);
			throw new SQLException(msg);
		}

		return result;
	}

	protected Connection createConnectionViaDriverManager(final Properties props) throws SQLException {
		// Try to load the driver
		if (!driverInitialized && driver != null) {
			try {
				LOGGER.debug("Loading driver " + driver);
				Class.forName(driver).newInstance();
				LOGGER.debug("... successful");
			} catch (final Exception e) {
				final String msg = "Loading of driver " + driver + " failed";
				LOGGER.error(msg, e);
				throw new SQLException(msg);
			}
			driverInitialized = true;
		}
		// When database login is provided use them for the login instead of the
		// ones provided by the client
		if (user != null) {
			LOGGER.debug("Using " + user + " for database-login");
			props.put("user", user);
			if (password != null) {
				props.put("password", password);
			} else {
				LOGGER.warn("No password was provided for database-login " + user);
			}
		}
		String jdbcurl = url;
		if (jdbcurl.length() > 0) {
			LOGGER.debug("JDBC-Connection-String: " + jdbcurl);
		} else {
			final String msg = "No JDBC-Connection-String available";
			LOGGER.error(msg);
			throw new SQLException(msg);
		}
		// Connection pooling with DBCP
		if (connectionPooling && connectionPoolInitialized != null) {
			final String dbcpId = DBCPID + id;
			if (connectionPool != null) {
				jdbcurl = dbcpId;
			} else {
				try {
					// Try to load the DBCP-Driver
					Class.forName("org.apache.commons.dbcp.PoolingDriver");
					// Populate configuration object
					if (connectionPoolConfiguration != null) {
						final GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
						poolConfig.maxActive = connectionPoolConfiguration.getMaxActive();
						poolConfig.maxIdle = connectionPoolConfiguration.getMaxIdle();
						poolConfig.maxWait = connectionPoolConfiguration.getMaxWait();
						poolConfig.minIdle = connectionPoolConfiguration.getMinIdle();
						poolConfig.minEvictableIdleTimeMillis = connectionPoolConfiguration.getMinEvictableIdleTimeMillis();
						poolConfig.timeBetweenEvictionRunsMillis = connectionPoolConfiguration.getTimeBetweenEvictionRunsMillis();
						connectionPool = new LoggingGenericObjectPool(id, poolConfig);
					} else {
						connectionPool = new LoggingGenericObjectPool(id);
					}
					final ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(jdbcurl, props);
					new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
					final PoolingDriver dbcpDriver = (PoolingDriver) DriverManager.getDriver(DBCPID);
					// Register pool with connection id
					dbcpDriver.registerPool(id, connectionPool);
					connectionPoolInitialized = Boolean.TRUE;
					jdbcurl = dbcpId;
					LOGGER.debug("Connection-Pooling successfully initialized for connection " + id);
				} catch (final ClassNotFoundException e) {
					connectionPool = null;
					connectionPoolInitialized = null;
					LOGGER.error("Jakarta-DBCP-Driver not found, switching it off for connection " + id, e);
				}
			}
		}
		return DriverManager.getConnection(jdbcurl, props);
	}
}
