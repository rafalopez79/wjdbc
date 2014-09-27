package com.bzsoft.wjdbc.server.config;

final class ConfigurationUtil {

	private static final long	MILLIS_PER_SECOND	= 1000;
	private static final long	MILLIS_PER_MINUTE	= MILLIS_PER_SECOND * 60;

	private ConfigurationUtil() {
		// empty
	}

	protected static boolean getBooleanFromString(final String value) {
		return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on");
	}

	protected static long getMillisFromString(final String value) {
		if (value.endsWith("s")) {
			return Long.parseLong(value.substring(0, value.length() - 1)) * MILLIS_PER_SECOND;
		} else if (value.endsWith("m")) {
			return Long.parseLong(value.substring(0, value.length() - 1)) * MILLIS_PER_MINUTE;
		} else {
			return Long.parseLong(value);
		}
	}

	protected static String getStringFromMillis(final long value) {
		if (value % MILLIS_PER_MINUTE == 0) {
			return "" + value / MILLIS_PER_MINUTE + " min";
		} else if (value % MILLIS_PER_SECOND == 0) {
			return "" + value / MILLIS_PER_SECOND + " sec";
		} else {
			return "" + value + " ms";
		}
	}
}
