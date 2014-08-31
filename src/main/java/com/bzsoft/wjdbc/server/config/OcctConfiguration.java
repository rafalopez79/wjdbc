// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.server.config;

import org.apache.log4j.Logger;

/**
 * This class holds configuration information for the OCCT.
 */
public class OcctConfiguration {

	private static final Logger	LOGGER			= Logger.getLogger(OcctConfiguration.class);

	private long						checkingPeriod	= 30000;
	private long						timeout			= 120000;

	public OcctConfiguration() {
		// empty
	}

	public long getCheckingPeriodInMillis() {
		return checkingPeriod;
	}

	public void setCheckingPeriodInMillis(final long checkingPeriod) {
		if (checkingPeriod != 0 && checkingPeriod <= 1000) {
			LOGGER.error("Checking-Period must be greater than 1 second");
		} else {
			this.checkingPeriod = checkingPeriod;
		}
	}

	public long getTimeoutInMillis() {
		return this.timeout;
	}

	public void setTimeoutInMillis(final long timeout) {
		if (timeout > 0 && timeout <= 1000) {
			LOGGER.error("Timeout must be greater than 1 second " + timeout);
		} else {
			this.timeout = timeout;
		}
	}

	protected void log() {
		if (checkingPeriod > 0) {
			LOGGER.info("OrphanedConnectionCollectorTask-Configuration (OCCT)");
			LOGGER.info("  Checking-Period........... " + ConfigurationUtil.getStringFromMillis(this.checkingPeriod));
			LOGGER.info("  Timeout................... " + ConfigurationUtil.getStringFromMillis(this.timeout));
		} else {
			LOGGER.info("OrphanedConnectionCollectorTask-Configuration (OCCT): off");
		}
	}
}
