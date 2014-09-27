package com.bzsoft.wjdbc.server.config;

public class DigesterRmiConfiguration extends RmiConfiguration {

	public void setCreateRegistry(final String createRegistry) {
		this.createRegistry = ConfigurationUtil.getBooleanFromString(createRegistry);
	}
}
