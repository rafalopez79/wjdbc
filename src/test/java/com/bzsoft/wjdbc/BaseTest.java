package com.bzsoft.wjdbc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.h2.Driver;
import org.junit.BeforeClass;
import org.junit.Ignore;

import com.bzsoft.wjdbc.server.config.ConfigurationException;
import com.bzsoft.wjdbc.server.config.WJdbcConfiguration;
import com.bzsoft.wjdbc.server.rmi.ConnectionServer;
import com.bzsoft.wjdbc.util.StreamCloser;

@Ignore
public abstract class BaseTest {

	protected static interface TestRunnable{

		public String getDescription();

		public void run() throws Exception;

	}


	protected static final Logger	LOGGER	= Logger.getLogger(BaseTest.class);

	public static final String MDDBURL = "jdbc:h2:mem:md;DB_CLOSE_DELAY=-1";
	protected static final double EPS = 0.00001;
	protected static final Random RAND = new Random(System.nanoTime());

	protected static boolean setup = false;
	protected static ConnectionServer cs;

	static {
		try {
			DriverManager.registerDriver(new Driver());
		} catch (final SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
		try {
			DriverManager.registerDriver(new WDriver());
		} catch (final SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	protected BaseTest() {
		// empty
	}

	public void setUp()throws Exception{
		LOGGER.info("Setting up "+getClass().getSimpleName());
	}

	public void tearDown()throws Exception{
		LOGGER.info("Tearing down "+getClass().getSimpleName());
	}

	@BeforeClass
	public static void setUpTestSuite() throws Exception{
		if (!setup){
			cs = setUpServer();
			setup = true;
		}
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

	protected static void tearDownServer(){
		if (cs != null){
			cs.shutdown();
		}
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

	protected static synchronized int randomInt(final int n){
		return RAND.nextInt(n);
	}

	protected static synchronized float randomFloat(){
		return RAND.nextFloat();
	}

	protected static synchronized double randomDouble(){
		return RAND.nextDouble();
	}

	protected static synchronized String randomString(final int n){
		final byte[] bytes = new byte[n];
		RAND.nextBytes(bytes);
		return new String(bytes);
	}

	protected static void assertThrowsException(final TestRunnable r, final Class<? extends Exception> ...exceptions){
		boolean throwsex = false;
		try{
			if (r != null){
				r.run();
			}
			throwsex = false;
		}catch(final InvocationTargetException e){
			final Throwable t = e.getCause();
			for(final Class<? extends Exception> item : exceptions){
				if (item.isAssignableFrom(t.getClass())){
					throwsex = true;
				}
			}
		}catch (final Exception e) {
			throwsex = false;
		}
		if (!throwsex){
			throw new AssertionError("Exception not thrown in method "+r.getDescription());
		}
	}

	protected static Object getRandomObject(final Class<?> clazz){
		if (clazz == String.class){
			return randomString(10);
		}else if (clazz == Boolean.class || clazz == boolean.class){
			return randomFloat() > 0.5f;
		}else if (clazz == Integer.class || clazz == int.class){
			return randomInt(1000);
		}else if (clazz == Float.class || clazz == float.class){
			return randomFloat();
		}else if (clazz == Double.class || clazz == double.class){
			return randomDouble();
		}else if (clazz == BigDecimal.class){
			return new BigDecimal(randomDouble());
		}else if (clazz == String[].class){
			return new String[]{randomString(10), randomString(10)};
		}else if (clazz == Boolean.class || clazz == boolean.class){
			return new boolean[]{randomFloat() > 0.5f};
		}else if (clazz == Integer.class || clazz == int.class){
			return new int[]{randomInt(1000)};
		}else if (clazz == Float.class || clazz == float.class){
			return new float[]{randomFloat()};
		}else if (clazz == Double.class || clazz == double.class){
			return new double[]{randomDouble()};
		}else if (clazz == BigDecimal.class){
			return new BigDecimal[]{new BigDecimal(randomDouble())};
		}
		return null;
	}
}
