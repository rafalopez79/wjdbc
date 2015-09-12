package com.bzsoft.wjdbc;

import java.rmi.server.RMISocketFactory;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import com.bzsoft.wjdbc.command.CommandSink;
import com.bzsoft.wjdbc.command.DecoratedCommandSink;
import com.bzsoft.wjdbc.rmi.ReconnectableCommandSinkRmiProxy;
import com.bzsoft.wjdbc.rmi.SecureSocketFactory;
import com.bzsoft.wjdbc.rmi.sf.CompressedRMISocketFactory;
import com.bzsoft.wjdbc.server.concurrent.Executor;
import com.bzsoft.wjdbc.server.concurrent.impl.BaseExecutor;
import com.bzsoft.wjdbc.servlet.ServletCommandSink;
import com.bzsoft.wjdbc.util.ClientInfo;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;

/**
 * The Class WDriver.
 */
public final class WDriver implements Driver {

	private static final String	WJDBC_IDENTIFIER		= "jdbc:wjdbc:";
	private static final String	RMI_IDENTIFIER			= "rmi:";
	private static final String	SERVLET_IDENTIFIER	= "servlet:";

	private static final int		MAJOR_VERSION			= 2;
	private static final int		MINOR_VERSION			= 0;

	static {
		try {
			DriverManager.registerDriver(new WDriver());
		} catch (final Exception e) {
			throw new RuntimeException("Couldn't register Wirtual-JDBC-Driver !", e);
		}
	}

	/**
	 * Instantiates a new w driver.
	 */
	public WDriver() {
		// empty
	}

	@Override
	public Connection connect(final String urlstr, final Properties props) throws SQLException {
		Connection result = null;
		if (acceptsURL(urlstr)) {
			final String realUrl = urlstr.substring(WJDBC_IDENTIFIER.length());
			try {
				final CommandSink sink;
				final String[] urlparts;
				if (realUrl.startsWith(RMI_IDENTIFIER)) {
					urlparts = split(realUrl.substring(RMI_IDENTIFIER.length()));
					// Examine SSL property
					boolean useSSL = false;
					final String propSSL = props.getProperty(WJdbcProperties.RMI_SSL);
					useSSL = propSSL != null && propSSL.equalsIgnoreCase("true");
					sink = createRmiCommandSink(urlparts[0], useSSL);
				}else  if (realUrl.startsWith(SERVLET_IDENTIFIER)) {
					urlparts = split(realUrl.substring(SERVLET_IDENTIFIER.length()));
					sink = createServletCommandSink(urlparts[0]);
				}else {
					throw new SQLException("Unknown protocol identifier " + realUrl);
				}
				// Connect with the sink
				final long uid = sink.connect(urlparts[1], props, ClientInfo.getProperties(props.getProperty(WJdbcProperties.CLIENTINFO_PROPERTIES)));
				final Executor executor = new BaseExecutor(1, 5);
				final DecoratedCommandSink decosink = new DecoratedCommandSink(uid, sink, executor);
				result = new WConnection(uid, decosink, executor);
			} catch (final Exception e) {
				throw SQLExceptionHelper.wrap(e);
			}
		}
		return result;
	}

	@Override
	public boolean acceptsURL(final String url) throws SQLException {
		return url.startsWith(WJDBC_IDENTIFIER);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
		return new DriverPropertyInfo[0];
	}

	@Override
	public int getMajorVersion() {
		return MAJOR_VERSION;
	}

	@Override
	public int getMinorVersion() {
		return MINOR_VERSION;
	}

	@Override
	public boolean jdbcCompliant() {
		return true;
	}

	private static CommandSink createRmiCommandSink(final String rminame, final boolean useSSL) throws Exception {
		final RMISocketFactory socketFactory;
		if (useSSL) {
			final SecureSocketFactory ssf = new SecureSocketFactory();
			socketFactory = new CompressedRMISocketFactory(ssf, ssf);
		} else {
			socketFactory = new CompressedRMISocketFactory();
		}
		return new ReconnectableCommandSinkRmiProxy(rminame, socketFactory);
	}


	private static CommandSink createServletCommandSink(final String url) throws Exception {
		return new ServletCommandSink(url);
	}


	private static String[] split(final String url) {
		final char[] splitChars = { ',', ';', '#', '$' };
		for (final char splitChar : splitChars) {
			final int charindex = url.indexOf(splitChar);
			if (charindex >= 0) {
				return new String[] { url.substring(0, charindex), url.substring(charindex + 1) };
			}
		}
		return new String[] { url, "" };
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("getParentLogger");
	}
}
