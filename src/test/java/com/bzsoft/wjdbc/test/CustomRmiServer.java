// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.test;

import java.util.zip.Deflater;

import com.bzsoft.wjdbc.server.config.ConnectionConfiguration;
import com.bzsoft.wjdbc.server.config.RmiConfiguration;
import com.bzsoft.wjdbc.server.config.WJdbcConfiguration;
import com.bzsoft.wjdbc.server.rmi.ConnectionServer;

public class CustomRmiServer {

	public static void main(final String[] args) {
		try {
			// Initialize VJDBC programmatically
			System.out.println("Initializing VJDBC");
			final WJdbcConfiguration vjdbcConfig = new WJdbcConfiguration();
			final RmiConfiguration rmiConfig = new RmiConfiguration("wjdbc", 20000);
			rmiConfig.setRemotingPort(20001);
			// rmiConfig.setUseSSL(true);
			rmiConfig.setCompressionModeAsInt(Deflater.BEST_COMPRESSION);
			vjdbcConfig.setRmiConfiguration(rmiConfig);
			final ConnectionConfiguration configHSql = new ConnectionConfiguration();
			configHSql.setDriver("org.h2.Driver");
			configHSql.setId("H2");
			configHSql.setUrl("jdbc:h2:~/test");
			configHSql.setUser("sa");
			configHSql.setPassword("");
			configHSql.setRowPacketSize(1000000);
			configHSql.setConnectionPooling(false);
			configHSql.setWjdbcPassword("a");
			configHSql.setWjdbcUser("a");
			vjdbcConfig.addConnection(configHSql);

			final ConnectionServer server = new ConnectionServer(vjdbcConfig);
			server.serve();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
