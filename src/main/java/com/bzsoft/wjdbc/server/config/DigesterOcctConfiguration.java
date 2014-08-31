// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.server.config;

public class DigesterOcctConfiguration extends OcctConfiguration {

	public void setCheckingPeriod(final String checkingPeriod) {
		setCheckingPeriodInMillis(ConfigurationUtil.getMillisFromString(checkingPeriod));
	}

	public void setTimeout(final String timeout) {
		setTimeoutInMillis(ConfigurationUtil.getMillisFromString(timeout));
	}
}
