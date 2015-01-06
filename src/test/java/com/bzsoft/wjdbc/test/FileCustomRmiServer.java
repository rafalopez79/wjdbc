package com.bzsoft.wjdbc.test;

import com.bzsoft.wjdbc.server.config.WJdbcConfiguration;
import com.bzsoft.wjdbc.server.rmi.ConnectionServer;

public class FileCustomRmiServer {

	public static void main(final String[] args) {
		try {
			System.out.println("Initializing WJDBC");
			final WJdbcConfiguration vjdbcConfig = WJdbcConfiguration.init("conf/wjdbc_config.xml");
			final ConnectionServer server = new ConnectionServer(vjdbcConfig);
			server.serve();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
