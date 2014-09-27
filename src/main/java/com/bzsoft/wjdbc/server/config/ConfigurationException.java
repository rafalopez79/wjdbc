package com.bzsoft.wjdbc.server.config;

public class ConfigurationException extends Exception {

	private static final long	serialVersionUID	= 4121131450591556150L;

	public ConfigurationException(final String msg) {
		super(msg);
	}

	public ConfigurationException(final String msg, final Exception cause) {
		super(msg, cause);
	}
}
