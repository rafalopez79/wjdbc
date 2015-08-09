package com.bzsoft.wjdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.h2.Driver;

import com.bzsoft.wjdbc.server.config.ConfigurationException;
import com.bzsoft.wjdbc.server.config.WJdbcConfiguration;
import com.bzsoft.wjdbc.server.rmi.ConnectionServer;
import com.bzsoft.wjdbc.util.StreamCloser;

public class BaseTest {

	protected static final Logger	LOG	= Logger.getLogger(BaseTest.class);

	public static final String MDDBURL = "jdbc:h2:mem:md;DB_CLOSE_DELAY=-1";

	static {
		try {
			DriverManager.registerDriver(new Driver());
		} catch (final SQLException e) {
			LOG.error(e.getMessage(), e);
		}
		try {
			DriverManager.registerDriver(new WDriver());
		} catch (final SQLException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	protected BaseTest() {
		// empty
	}

	protected static ConnectionServer setUpServer() throws ConfigurationException, IOException{
		InputStream is = null;
		try{
			is = BaseTest.class.getClassLoader().getResourceAsStream("wjdbc_config.xml");
			final WJdbcConfiguration config = WJdbcConfiguration.of(is, new Properties());
			final ConnectionServer server = new ConnectionServer(config);
			server.serve();
			return server;
		}finally{
			StreamCloser.close(is);
		}
	}

	protected static void tearDownServer(final ConnectionServer cs){
		cs.shutdown();
	}

	protected static Connection setUpClient() throws SQLException{
		final WDriver driver = new WDriver();
		final Properties prop = new Properties();
		prop.setProperty("user", "pp");
		prop.setProperty("password","pepa");
		return driver.connect("jdbc:wjdbc:rmi://127.0.0.1:40000/WJdbc;h2", prop);
	}

	protected static void executeUpdate(final Statement stmt, final String... sqls) throws SQLException {
		for (final String sql : sqls) {
			stmt.executeUpdate(sql);
		}
	}

	protected static void executeUpdate(final Statement stmt, final List<String> sqls) throws SQLException {
		for (final String sql : sqls) {
			stmt.executeUpdate(sql);
		}
	}

}
