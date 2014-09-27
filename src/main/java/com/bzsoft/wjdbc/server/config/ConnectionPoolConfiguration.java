package com.bzsoft.wjdbc.server.config;

import org.apache.log4j.Logger;

public class ConnectionPoolConfiguration {

	private static final Logger	LOGGER								= Logger.getLogger(ConnectionPoolConfiguration.class);

	protected int						maxActive							= 8;
	protected int						maxIdle								= 8;
	protected int						minIdle								= 0;
	protected long						maxWait								= -1;
	protected int						timeBetweenEvictionRunsMillis	= -1;
	protected int						minEvictableIdleTimeMillis		= 1000 * 60 * 30;

	public ConnectionPoolConfiguration() {
		// empty
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
		LOGGER.info("  ConnectionPool-Configuration");
		LOGGER.info("    Max. active connections .............. " + maxActive);
		LOGGER.info("    Max. number of idle connections ...... " + maxIdle);
		LOGGER.info("    Min. number of idle connections ...... " + minIdle);
		LOGGER.info("    Max. waiting time for connections .... " + ConfigurationUtil.getStringFromMillis(maxWait));
		LOGGER.info("    Time between eviction runs ........... " + ConfigurationUtil.getStringFromMillis(timeBetweenEvictionRunsMillis));
		LOGGER.info("    Min. idle time before eviction ....... " + ConfigurationUtil.getStringFromMillis(minEvictableIdleTimeMillis));
	}
}
