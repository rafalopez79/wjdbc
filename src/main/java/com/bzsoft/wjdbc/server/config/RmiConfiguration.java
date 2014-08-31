// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.server.config;

import java.util.zip.Deflater;

import org.apache.log4j.Logger;

public class RmiConfiguration {

	private static final Logger	LOGGER				= Logger.getLogger(RmiConfiguration.class);

	protected String					objectName			= "WJdbc";
	protected int						registryPort		= 2000;
	protected int						remotingPort		= 0;
	protected int						compressionMode	= Deflater.NO_COMPRESSION;
	protected boolean					createRegistry		= true;
	protected boolean					useSSL				= false;

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

	public int getCompressionModeAsInt() {
		return compressionMode;
	}

	public void setCompressionModeAsInt(final int compressionMode) throws ConfigurationException {
		switch (compressionMode) {
		case Deflater.BEST_SPEED:
		case Deflater.BEST_COMPRESSION:
		case Deflater.NO_COMPRESSION:
			this.compressionMode = compressionMode;
			break;
		default:
			throw new ConfigurationException("Unknown compression mode");
		}
	}

	public String getCompressionMode() {
		switch (compressionMode) {
		case Deflater.BEST_SPEED:
			return "bestspeed";
		case Deflater.BEST_COMPRESSION:
			return "bestcompression";
		case Deflater.NO_COMPRESSION:
			return "none";
		default:
			throw new RuntimeException("Unknown compression mode");
		}
	}

	public void setCompressionMode(final String compressionMode) throws ConfigurationException {
		if (compressionMode.equalsIgnoreCase("bestspeed")) {
			this.compressionMode = Deflater.BEST_SPEED;
		} else if (compressionMode.equalsIgnoreCase("bestcompression")) {
			this.compressionMode = Deflater.BEST_COMPRESSION;
		} else if (compressionMode.equalsIgnoreCase("none")) {
			this.compressionMode = Deflater.NO_COMPRESSION;
		} else {
			throw new ConfigurationException("Unknown compression mode '" + compressionMode + "', use either bestspeed, bestcompression or none");
		}
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
		LOGGER.info("  Compression Mode ......... " + getCompressionMode());
	}
}
