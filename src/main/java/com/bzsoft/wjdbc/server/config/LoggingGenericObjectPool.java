//VJDBC - Virtual JDBC
//Written by Michael Link
//Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.server.config;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

/**
 * This class inherits from the GenericObjectPool and provides a little bit of
 * logging when eviction happens.
 * 
 * @author Mike
 */
public class LoggingGenericObjectPool extends GenericObjectPool {

	private static final Logger	LOGGER	= Logger.getLogger(LoggingGenericObjectPool.class);

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
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("DBCP-Evictor: number of idle connections in '" + idOfConnection + "' = " + getNumIdle());
		}
	}
}
