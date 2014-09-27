package com.bzsoft.wjdbc.server.config;

public class DigesterConnectionConfiguration extends ConnectionConfiguration {

	public void setTraceCommandCount(final String traceCommandCount) {
		this.traceCommandCount = ConfigurationUtil.getBooleanFromString(traceCommandCount);
	}

	public void setTraceOrphanedObjects(final String traceOrphandedObjects) {
		this.traceOrphanedObjects = ConfigurationUtil.getBooleanFromString(traceOrphandedObjects);
	}

	public void setConnectionPooling(final String connectionPooling) {
		this.connectionPooling = ConfigurationUtil.getBooleanFromString(connectionPooling);
	}
}
