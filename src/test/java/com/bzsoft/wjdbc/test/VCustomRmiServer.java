package com.bzsoft.wjdbc.test;

import java.sql.DriverManager;

import com.bzsoft.wjdbc.test.mock.MockDriver;

import de.simplicit.vjdbc.server.config.ConnectionConfiguration;
import de.simplicit.vjdbc.server.config.RmiConfiguration;
import de.simplicit.vjdbc.server.config.VJdbcConfiguration;
import de.simplicit.vjdbc.server.rmi.ConnectionServer;

public class VCustomRmiServer {

	public static void main(final String[] args) {
		try {
			DriverManager.registerDriver(new MockDriver());
			System.out.println("Initializing VJDBC");
			final VJdbcConfiguration vjdbcConfig = new VJdbcConfiguration();
			VJdbcConfiguration.init(vjdbcConfig);
			final RmiConfiguration rmiConfig = new RmiConfiguration("wjdbc", 20000);
			rmiConfig.setRemotingPort(20001);
			// rmiConfig.setUseSSL(true);
			vjdbcConfig.setRmiConfiguration(rmiConfig);
			final ConnectionConfiguration configHSql = new ConnectionConfiguration();
			// configHSql.setDriver("org.h2.Driver");
			// configHSql.setId("H2");
			// configHSql.setUrl("jdbc:h2:~/test");
			// configHSql.setUser("sa");
			// configHSql.setPassword("");
			configHSql.setDriver("com.bzsoft.wjdbc.test.mock.MockDriver");
			configHSql.setId("H2");
			configHSql.setUrl("mock");
			configHSql.setUser("sa");
			configHSql.setPassword("");
			configHSql.setRowPacketSize(10000);
			configHSql.setConnectionPooling(false);
			// configHSql.setWjdbcUser("distribucion");
			// configHSql.setWjdbcPassword("raHgTINI4FWo");
			vjdbcConfig.addConnection(configHSql);

			final ConnectionServer server = new ConnectionServer();
			server.serve();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
