package com.bzsoft.wjdbc.server.config;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class inherits from the GenericObjectPool and provides a little bit of
 * logging when eviction happens.
 *
 * @author Mike
 */
public class LoggingGenericObjectPool extends GenericObjectPool {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingGenericObjectPool.class);

	private final String				idOfConnection;

	public LoggingGenericObjectPool(final String nameOfConnection) {
		super(null);
		idOfConnection = nameOfConnection;
	}

	public LoggingGenericObjectPool(final String nameOfConnection, final GenericObjectPool.Config config) {
		super(null, config);
		idOfConnection = nameOfConnection;
	}

	@Override
	public synchronized void evict() throws Exception {
		super.evict();
		LOGGER.debug("DBCP-Evictor: number of idle connections in '{}' = {}", idOfConnection, getNumIdle());
	}
}
