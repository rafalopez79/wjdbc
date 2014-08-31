// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

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
import java.util.zip.Deflater;

import com.bzsoft.wjdbc.command.CallingContextFactory;
import com.bzsoft.wjdbc.command.CommandSink;
import com.bzsoft.wjdbc.command.ConnectResult;
import com.bzsoft.wjdbc.command.DecoratedCommandSink;
import com.bzsoft.wjdbc.command.NullCallingContextFactory;
import com.bzsoft.wjdbc.command.StandardCallingContextFactory;
import com.bzsoft.wjdbc.rmi.ReconnectableCommandSinkRmiProxy;
import com.bzsoft.wjdbc.rmi.SecureSocketFactory;
import com.bzsoft.wjdbc.rmi.sf.CompressedRMISocketFactory;
import com.bzsoft.wjdbc.serial.CallingContext;
import com.bzsoft.wjdbc.server.concurrent.Executor;
import com.bzsoft.wjdbc.server.concurrent.impl.BaseExecutor;
import com.bzsoft.wjdbc.servlet.RequestEnhancer;
import com.bzsoft.wjdbc.servlet.RequestEnhancerFactory;
import com.bzsoft.wjdbc.servlet.ServletCommandSinkJdkHttpClient;
import com.bzsoft.wjdbc.servlet.jakarta.ServletCommandSinkJakartaHttpClient;
import com.bzsoft.wjdbc.util.ClientInfo;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;

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
					// Servlet-Connection
				} else if (realUrl.startsWith(SERVLET_IDENTIFIER)) {
					urlparts = split(realUrl.substring(SERVLET_IDENTIFIER.length()));
					sink = createServletCommandSink(urlparts[0], props);
				} else {
					throw new SQLException("Unknown protocol identifier " + realUrl);
				}
				// Connect with the sink
				final ConnectResult cr = sink.connect(urlparts[1], props,
						ClientInfo.getProperties(props.getProperty(WJdbcProperties.CLIENTINFO_PROPERTIES)), new CallingContext());

				CallingContextFactory ctxFactory;
				// The value 1 signals that every remote call shall provide a
				// calling context. This should only
				// be used for debugging purposes as sending of these objects is
				// quite expensive.
				if (cr.isTrace()) {
					ctxFactory = new StandardCallingContextFactory();
				} else {
					ctxFactory = new NullCallingContextFactory();
				}
				final Executor executor = new BaseExecutor(1, 5);
				final long uid = cr.getUid();
				final DecoratedCommandSink decosink = new DecoratedCommandSink(uid, sink, ctxFactory, executor);
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
		final int compressionMode = Deflater.BEST_SPEED;
		if (useSSL) {
			final SecureSocketFactory ssf = new SecureSocketFactory();
			socketFactory = new CompressedRMISocketFactory(ssf, ssf, compressionMode);
		} else {
			socketFactory = new CompressedRMISocketFactory(compressionMode);
		}
		// final ConnectionBrokerRmi broker = (ConnectionBrokerRmi)
		// Naming.lookup(rminame, socketFactory);
		// final CommandSinkRmi rmiSink = broker.createCommandSink();
		// final CommandSink proxy = new CommandSinkRmiProxy(rmiSink);
		final CommandSink proxy = new ReconnectableCommandSinkRmiProxy(rminame, socketFactory);
		return proxy;
	}

	private static CommandSink createServletCommandSink(final String url, final Properties props) throws Exception {
		RequestEnhancer requestEnhancer = null;
		final String requestEnhancerFactoryClassName = props.getProperty(WJdbcProperties.SERVLET_REQUEST_ENHANCER_FACTORY);
		if (requestEnhancerFactoryClassName != null) {
			final Class<?> requestEnhancerFactoryClass = Class.forName(requestEnhancerFactoryClassName);
			final RequestEnhancerFactory requestEnhancerFactory = (RequestEnhancerFactory) requestEnhancerFactoryClass.newInstance();
			requestEnhancer = requestEnhancerFactory.create();
		}
		// Decide here if we should use Jakarta-HTTP-Client
		final String useJakartaHttpClient = props.getProperty(WJdbcProperties.SERVLET_USE_JAKARTA_HTTP_CLIENT);
		if (useJakartaHttpClient != null && useJakartaHttpClient.equals("true")) {
			return new ServletCommandSinkJakartaHttpClient(url, requestEnhancer);
		}
		return new ServletCommandSinkJdkHttpClient(url, requestEnhancer);
	}

	// Helper method (can't use the 1.4-Method because support for 1.3 is
	// desired)
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

	@SuppressWarnings("static-method")
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("getParentLogger");
	}
}
