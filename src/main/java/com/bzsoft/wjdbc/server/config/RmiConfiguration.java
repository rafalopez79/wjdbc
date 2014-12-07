package com.bzsoft.wjdbc.server.config;

import org.apache.log4j.Logger;

public class RmiConfiguration {

	private static final Logger	LOGGER			= Logger.getLogger(RmiConfiguration.class);

	protected String					objectName		= "WJdbc";
	protected int						registryPort	= 2000;
	protected int						remotingPort	= 0;
	protected boolean					createRegistry	= true;
	protected boolean					useSSL			= false;

	public RmiConfiguration() {
		// empty
	}

	public RmiConfiguration(final String objectName) {
		this.objectName = objectName;
	}

	public RmiConfiguration(final String objectName, final int port) {
		this.objectName = objectName;
		registryPort = port;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(final String objectName) {
		this.objectName = objectName;
	}

	public int getPort() {
		return registryPort;
	}

	public void setPort(final int port) {
		registryPort = port;
	}

	public int getRegistryPort() {
		return registryPort;
	}

	public void setRegistryPort(final int registryPort) {
		this.registryPort = registryPort;
	}

	public int getRemotingPort() {
		return remotingPort;
	}

	public void setRemotingPort(final int listenerPort) {
		remotingPort = listenerPort;
	}

	public boolean isCreateRegistry() {
		return createRegistry;
	}

	public void setCreateRegistry(final boolean createRegistry) {
		this.createRegistry = createRegistry;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public void setUseSSL(final boolean useSSL) {
		this.useSSL = useSSL;
	}

	protected void log() {
		LOGGER.info("RMI-Configuration");
		LOGGER.info("  ObjectName ............... " + objectName);
		LOGGER.info("  Registry-Port ............ " + registryPort);
		if (remotingPort > 0) {
			LOGGER.info("  Remoting-Port ............ " + remotingPort);
		}
		LOGGER.info("  Create Registry .......... " + createRegistry);
		LOGGER.info("  Use SSL .................. " + useSSL);
	}
}
