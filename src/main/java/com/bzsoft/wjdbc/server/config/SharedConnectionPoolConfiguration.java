package com.bzsoft.wjdbc.server.config;

import static com.bzsoft.wjdbc.server.config.ConnectionConstants.DBCPDRIVER;
import static com.bzsoft.wjdbc.server.config.ConnectionConstants.DBCPID;

import java.sql.DriverManager;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

public class SharedConnectionPoolConfiguration {

	private static final Logger	LOGGER								= Logger.getLogger(SharedConnectionPoolConfiguration.class);

	protected String					id;
	protected String					driver;
	protected String					url;
	protected String					dataSourceProvider;
	protected String					user;
	protected String					password;
	protected int						maxActive							= 8;
	protected int						maxIdle								= 8;
	protected int						minIdle								= 0;
	protected long						maxWait								= -1;
	protected int						timeBetweenEvictionRunsMillis	= -1;
	protected int						minEvictableIdleTimeMillis		= 1000 * 60 * 30;

	public SharedConnectionPoolConfiguration() {
		// empty
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setDriver(final String driver) {
		this.driver = driver;
	}

	public String getDriver() {
		return driver;
	}

	public void setDataSourceProvider(final String dataSourceProvider) {
		this.dataSourceProvider = dataSourceProvider;
	}

	public String getDataSourceProvider() {
		return dataSourceProvider;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(final int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(final int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(final long maxWait) {
		this.maxWait = maxWait;
	}

	public int getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(final int minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(final int minIdle) {
		this.minIdle = minIdle;
	}

	public int getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(final int timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	protected void log() {
		LOGGER.info("  Shared ConnectionPool-Configuration");
		LOGGER.info("    Id ................................... " + id);
		LOGGER.info("    Driver ............................... " + driver);
		if (dataSourceProvider != null) {
			LOGGER.info("    DatasourceProvider .......................... " + driver);
		}
		LOGGER.info("    Url .................................. " + url);
		LOGGER.info("    User ................................. " + user);
		LOGGER.info("    Password ............................. " + password);
		LOGGER.info("    Max. active connections .............. " + maxActive);
		LOGGER.info("    Max. number of idle connections ...... " + maxIdle);
		LOGGER.info("    Min. number of idle connections ...... " + minIdle);
		LOGGER.info("    Max. waiting time for connections .... " + ConfigurationUtil.getStringFromMillis(maxWait));
		LOGGER.info("    Time between eviction runs ........... " + ConfigurationUtil.getStringFromMillis(timeBetweenEvictionRunsMillis));
		LOGGER.info("    Min. idle time before eviction ....... " + ConfigurationUtil.getStringFromMillis(minEvictableIdleTimeMillis));
	}

	@SuppressWarnings("unused")
	public String createSharedConnectionPool() {
		final String dbcpId = DBCPID + id;
		// Try to load the DBCP-Driver
		try {
			Class.forName(DBCPDRIVER);
			Class.forName(driver);
			// Populate configuration object
			final GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
			poolConfig.maxActive = getMaxActive();
			poolConfig.maxIdle = getMaxIdle();
			poolConfig.maxWait = getMaxWait();
			poolConfig.minIdle = getMinIdle();
			poolConfig.minEvictableIdleTimeMillis = getMinEvictableIdleTimeMillis();
			poolConfig.timeBetweenEvictionRunsMillis = getTimeBetweenEvictionRunsMillis();
			final GenericObjectPool connectionPool = new LoggingGenericObjectPool(id, poolConfig);
			final ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(getUrl(), getUser(), getPassword());
			new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
			final PoolingDriver dbcpDriver = (PoolingDriver) DriverManager.getDriver(DBCPID);
			// Register pool with connection id
			dbcpDriver.registerPool(id, connectionPool);
			LOGGER.debug("Shared Connection-Pooling successfully initialized for pool " + id);
			return dbcpId;
		} catch (final Exception e) {
			LOGGER.error("Cannot initialize shared pool " + id, e);
			return null;
		}
	}
}
