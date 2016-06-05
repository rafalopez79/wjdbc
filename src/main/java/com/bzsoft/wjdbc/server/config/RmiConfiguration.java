package com.bzsoft.wjdbc.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RmiConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(RmiConfiguration.class);

	public static  final String OBJECTNAME		= "WJdbc";

	protected int						registryPort	= 2000;
	protected int						remotingPort	= 0;
	protected boolean					createRegistry	= true;
	protected boolean					useSSL			= false;

	public RmiConfiguration() {
		// empty
	}

	public RmiConfiguration(final int port) {
		registryPort = port;
	}

	public String getObjectName() {
		return OBJECTNAME;
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
		LOGGER.info("  ObjectName ............... {}", OBJECTNAME);
		LOGGER.info("  Registry-Port ............ {}", registryPort);
		if (remotingPort > 0) {
			LOGGER.info("  Remoting-Port ............ {}", remotingPort);
		}
		LOGGER.info("  Create Registry .......... {}", createRegistry);
		LOGGER.info("  Use SSL .................. {}", useSSL);
	}
}
