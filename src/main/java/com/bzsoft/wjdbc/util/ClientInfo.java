// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.util;

import java.net.InetAddress;
import java.util.Properties;

public final class ClientInfo {

	private static Properties	PROPERTIES	= null;

	private ClientInfo() {
		// empty
	}

	public static synchronized Properties getProperties(final String propertiesToTransfer) {
		if (PROPERTIES == null) {
			PROPERTIES = new Properties();
			try {
				// Deliver local host information
				final InetAddress iadr = InetAddress.getLocalHost();
				PROPERTIES.put("vjdbc-client.address", iadr.getHostAddress());
				PROPERTIES.put("vjdbc-client.name", iadr.getHostName());
				// Split the passed string into pieces and put all system PROPERTIES
				// into the Properties object
				if (propertiesToTransfer != null) {
					final String[] sarray = propertiesToTransfer.split(";");
					for (final String key : sarray) {
						final String value = System.getProperty(key);
						if (value != null) {
							PROPERTIES.put(key, value);
						}
					}
				}
			} catch (final Exception e) {
				// empty
			}
		}
		return PROPERTIES;
	}
}
