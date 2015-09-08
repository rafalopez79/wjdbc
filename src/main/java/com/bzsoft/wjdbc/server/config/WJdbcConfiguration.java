package com.bzsoft.wjdbc.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.substitution.MultiVariableExpander;
import org.apache.commons.digester.substitution.VariableSubstitutor;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.bzsoft.wjdbc.server.concurrent.Executor;
import com.bzsoft.wjdbc.server.concurrent.impl.BaseExecutor;

public class WJdbcConfiguration {

	private static final Logger												LOGGER					= Logger.getLogger(WJdbcConfiguration.class);
	private static final int													SCHEDTHREADPOOLSIZE	= 1;
	private static final int													MAXTHREADPOOLSIZE		= 200;

	private OcctConfiguration													occtConfiguration;
	private RmiConfiguration													rmiConfiguration;
	private final List<ConnectionConfiguration>							connections;
	private final Executor														pooledExecutor;
	private final Map<String, SharedConnectionPoolConfiguration>	sharedPoolMap;

	private WJdbcConfiguration() {
		pooledExecutor = new BaseExecutor(SCHEDTHREADPOOLSIZE, MAXTHREADPOOLSIZE);
		connections = new ArrayList<ConnectionConfiguration>();
		sharedPoolMap = new LinkedHashMap<String, SharedConnectionPoolConfiguration>();
		occtConfiguration = new OcctConfiguration();
	}

	private WJdbcConfiguration(final InputStream configResource, final Properties vars) throws IOException, SAXException, ConfigurationException {
		this();
		final Digester digester = createDigester(vars);
		digester.parse(configResource);
		validateConnections();
	}

	private WJdbcConfiguration(final String configResource, final Properties vars) throws IOException, SAXException, ConfigurationException {
		this();
		final Digester digester = createDigester(vars);
		digester.parse(configResource);
		validateConnections();
	}

	/**
	 * Initialization with resource.
	 *
	 * @param configResource
	 *           Resource to be loaded by the ClassLoader
	 * @return WJdbcConfiguration
	 * 			 The configuration
	 * @throws ConfigurationException
	 *           The config exception
	 */
	public static WJdbcConfiguration of(final String configResource) throws ConfigurationException {
		return of(configResource, null);
	}

	/**
	 * Initialization with resource.
	 *
	 * @param configResource
	 *           Resource to be loaded by the ClassLoader
	 * @param configVariables
	 * 			 The configVariables
	 * @return WJdbcConfiguration
	 * 			 The configuration
	 * @throws ConfigurationException
	 * 			 Configuration Exception
	 */
	public static WJdbcConfiguration of(final String configResource, final Properties configVariables) throws ConfigurationException {
		try {
			final WJdbcConfiguration config = new WJdbcConfiguration(configResource, configVariables);
			if (LOGGER.isInfoEnabled()) {
				config.log();
			}
			return config;
		} catch (final Exception e) {
			final String msg = "WJdbc-Configuration failed";
			LOGGER.error(msg, e);
			throw new ConfigurationException(msg, e);
		}
	}

	/**
	 * Initialization with pre-opened InputStream.
	 *
	 * @param configResourceInputStream
	 *           InputStream
	 * @param configVariables
	 *  			 The config variables
	 * @return WJdbcConfiguration
	 * 			 The configuration
	 * @throws ConfigurationException
	 * 			 ConfigurationException
	 */
	public static WJdbcConfiguration of(final InputStream configResourceInputStream, final Properties configVariables) throws ConfigurationException {
		try {
			final WJdbcConfiguration config = new WJdbcConfiguration(configResourceInputStream, configVariables);
			if (LOGGER.isInfoEnabled()) {
				config.log();
			}
			return config;
		} catch (final Exception e) {
			final String msg = "WJdbc-Configuration failed";
			LOGGER.error(msg, e);
			throw new ConfigurationException(msg, e);
		}

	}

	public OcctConfiguration getOcctConfiguration() {
		return occtConfiguration;
	}

	public void setOcctConfiguration(final OcctConfiguration occtConfiguration) {
		this.occtConfiguration = occtConfiguration;
	}

	public Executor getExecutor() {
		return pooledExecutor;
	}

	public void addSharedPoolConfiguration(final SharedConnectionPoolConfiguration scp) {
		sharedPoolMap.put(scp.getId(), scp);
	}

	public Map<String, SharedConnectionPoolConfiguration> getSharedPoolConfiguration() {
		return Collections.unmodifiableMap(sharedPoolMap);
	}

	/**
	 * Returns the RMI-Configuration.
	 *
	 * @return RmiConfiguration object or null
	 */
	public RmiConfiguration getRmiConfiguration() {
		return rmiConfiguration;
	}

	/**
	 * Sets the RMI-Configuration object.
	 *
	 * @param rmiConfiguration
	 *           RmiConfiguration object to be used.
	 */
	public void setRmiConfiguration(final RmiConfiguration rmiConfiguration) {
		this.rmiConfiguration = rmiConfiguration;
	}

	/**
	 * Returns a ConnectionConfiguration for a specific identifier.
	 *
	 * @param name
	 *           Identifier of the ConnectionConfiguration
	 * @return ConnectionConfiguration or null
	 */
	public ConnectionConfiguration getConnection(final String name) {
		for (final ConnectionConfiguration connectionConfiguration : connections) {
			if (connectionConfiguration.getId().equals(name)) {
				return connectionConfiguration;
			}
		}
		return null;
	}

	public ConnectionConfiguration getConnectionBySharedPoolId(final String sharedPoolId) {
		for (final ConnectionConfiguration connectionConfiguration : connections) {
			if (sharedPoolId.equals(connectionConfiguration.getSharedPoolId())) {
				return connectionConfiguration;
			}
		}
		return null;
	}

	/**
	 * Adds a ConnectionConfiguration.
	 *
	 * @param connectionConfiguration
	 * 			The connection configuration
	 * @throws ConfigurationException
	 *            Thrown when the connection identifier already exists
	 */
	public void addConnection(final ConnectionConfiguration connectionConfiguration) throws ConfigurationException {
		if (getConnection(connectionConfiguration.getId()) == null) {
			connections.add(connectionConfiguration);
		} else {
			final String msg = "Connection configuration for " + connectionConfiguration.getId() + " already exists";
			LOGGER.error(msg);
			throw new ConfigurationException(msg);
		}
	}

	private Digester createDigester(final Properties vars) {
		final Digester digester = createDigester();
		if (vars != null) {
			final MultiVariableExpander expander = new MultiVariableExpander();
			expander.addSource("$", vars);
			digester.setSubstitutor(new VariableSubstitutor(expander));
		}
		return digester;
	}

	private void validateConnections() throws ConfigurationException {
		for (final ConnectionConfiguration connectionConfiguration : connections) {
			connectionConfiguration.validate(sharedPoolMap);
		}
	}

	private Digester createDigester() {
		final Digester digester = new Digester();

		digester.push(this);

		digester.addObjectCreate("wjdbc-configuration/occt", DigesterOcctConfiguration.class);
		digester.addSetProperties("wjdbc-configuration/occt");
		digester.addSetNext("wjdbc-configuration/occt", "setOcctConfiguration", OcctConfiguration.class.getName());

		digester.addObjectCreate("wjdbc-configuration/rmi", DigesterRmiConfiguration.class);
		digester.addSetProperties("wjdbc-configuration/rmi");
		digester.addSetNext("wjdbc-configuration/rmi", "setRmiConfiguration", RmiConfiguration.class.getName());

		digester.addObjectCreate("wjdbc-configuration/connection", DigesterConnectionConfiguration.class);
		digester.addSetProperties("wjdbc-configuration/connection");
		digester.addSetNext("wjdbc-configuration/connection", "addConnection", ConnectionConfiguration.class.getName());

		digester.addObjectCreate("wjdbc-configuration/connection/connection-pool", ConnectionPoolConfiguration.class);
		digester.addSetProperties("wjdbc-configuration/connection/connection-pool");
		digester.addSetNext("wjdbc-configuration/connection/connection-pool", "setConnectionPoolConfiguration",
				ConnectionPoolConfiguration.class.getName());

		// Query-Filters
		digester.addObjectCreate("wjdbc-configuration/connection/query-filters", QueryFilterConfiguration.class);
		digester.addCallMethod("wjdbc-configuration/connection/query-filters/deny", "addDenyEntry", 2);
		digester.addCallParam("wjdbc-configuration/connection/query-filters/deny", 0);
		digester.addCallParam("wjdbc-configuration/connection/query-filters/deny", 1, "type");
		digester.addCallMethod("wjdbc-configuration/connection/query-filters/allow", "addAllowEntry", 2);
		digester.addCallParam("wjdbc-configuration/connection/query-filters/allow", 0);
		digester.addCallParam("wjdbc-configuration/connection/query-filters/allow", 1, "type");
		digester.addSetNext("wjdbc-configuration/connection/query-filters", "setQueryFilters", QueryFilterConfiguration.class.getName());

		// Shared pools
		digester.addObjectCreate("wjdbc-configuration/shared-pool", SharedConnectionPoolConfiguration.class);
		digester.addSetProperties("wjdbc-configuration/shared-pool");
		digester.addSetNext("wjdbc-configuration/shared-pool", "addSharedPoolConfiguration", SharedConnectionPoolConfiguration.class.getName());

		return digester;
	}

	private void log() {
		if (rmiConfiguration != null) {
			rmiConfiguration.log();
		}
		occtConfiguration.log();
		for (final ConnectionConfiguration connectionConfiguration : connections) {
			connectionConfiguration.log();
		}
		for (final SharedConnectionPoolConfiguration scp : sharedPoolMap.values()) {
			scp.log();
		}
	}
}
