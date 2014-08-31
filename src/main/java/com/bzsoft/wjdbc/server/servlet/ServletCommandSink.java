// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.server.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.serial.CallingContext;
import com.bzsoft.wjdbc.server.command.CommandProcessor;
import com.bzsoft.wjdbc.server.config.ConfigurationException;
import com.bzsoft.wjdbc.server.config.ConnectionConfiguration;
import com.bzsoft.wjdbc.server.config.WJdbcConfiguration;
import com.bzsoft.wjdbc.servlet.ServletCommandSinkIdentifier;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;
import com.bzsoft.wjdbc.util.StreamCloser;

public class ServletCommandSink extends HttpServlet {

	private static final long		serialVersionUID						= 3257570624301249846L;

	private static final String	INIT_PARAMETER_CONFIG_RESOURCE	= "config-resource";
	private static final String	INIT_PARAMETER_CONFIG_VARIABLES	= "config-variables";
	private static final String	DEFAULT_CONFIG_RESOURCE				= "/WEB-INF/vjdbc-config.xml";
	private static final Logger	LOGGER									= Logger.getLogger(ServletCommandSink.class);

	private WJdbcConfiguration		config;
	private CommandProcessor		processor;

	public ServletCommandSink() {
		// empty
	}

	@Override
	public void init(final ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		String configResource = servletConfig.getInitParameter(INIT_PARAMETER_CONFIG_RESOURCE);
		// Use default location when nothing is configured
		if (configResource == null) {
			configResource = DEFAULT_CONFIG_RESOURCE;
		}
		final ServletContext ctx = servletConfig.getServletContext();
		LOGGER.info("Trying to get config resource " + configResource + "...");
		InputStream configResourceInputStream = ctx.getResourceAsStream(configResource);
		if (configResourceInputStream == null) {
			try {
				configResourceInputStream = new FileInputStream(ctx.getRealPath(configResource));
			} catch (final FileNotFoundException fnfe) {
				// empty
			}
		}

		if (configResourceInputStream == null) {
			final String msg = "VJDBC-Configuration " + configResource + " not found !";
			LOGGER.error(msg);
			throw new ServletException(msg);
		}
		// Are config variables specifiec ?
		final String configVariables = servletConfig.getInitParameter(INIT_PARAMETER_CONFIG_VARIABLES);
		Properties configVariablesProps = null;
		if (configVariables != null) {
			LOGGER.info("... using variables specified in " + configVariables);
			InputStream configVariablesInputStream = null;
			try {
				configVariablesInputStream = ctx.getResourceAsStream(configVariables);
				if (configVariablesInputStream == null) {
					configVariablesInputStream = new FileInputStream(ctx.getRealPath(configVariables));
				}
				configVariablesProps = new Properties();
				configVariablesProps.load(configVariablesInputStream);
			} catch (final IOException e) {
				final String msg = "Reading of configuration variables failed";
				LOGGER.error(msg, e);
				throw new ServletException(msg, e);
			} finally {
				if (configVariablesInputStream != null) {
					try {
						configVariablesInputStream.close();
					} catch (final IOException e) {
						// empty
					}
				}
			}
		}

		try {
			LOGGER.info("Initialize VJDBC-Configuration");
			config = WJdbcConfiguration.init(configResourceInputStream, configVariablesProps);
			processor = new CommandProcessor(config);
		} catch (final ConfigurationException e) {
			LOGGER.error("Initialization failed", e);
			throw new ServletException("VJDBC-Initialization failed", e);
		} finally {
			StreamCloser.close(configResourceInputStream);
		}
	}

	@Override
	public void destroy() {
		// empty
	}

	@Override
	protected void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException {
		handleRequest(httpServletRequest, httpServletResponse);
	}

	@Override
	protected void doPost(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException {
		handleRequest(httpServletRequest, httpServletResponse);
	}

	private void handleRequest(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException {
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;

		try {
			// Get the method to execute
			final String method = httpServletRequest.getHeader(ServletCommandSinkIdentifier.METHOD_IDENTIFIER);

			if (method != null) {
				ois = new ObjectInputStream(httpServletRequest.getInputStream());
				// And initialize the output
				final OutputStream os = httpServletResponse.getOutputStream();
				oos = new ObjectOutputStream(os);
				Object objectToReturn = null;

				try {
					// Some command to process ?
					if (method.equals(ServletCommandSinkIdentifier.PROCESS_COMMAND)) {
						// Read parameter objects
						final long connuid = ois.readLong();
						final long uid = ois.readLong();
						final Command<?, ?> cmd = (Command<?, ?>) ois.readObject();
						final CallingContext ctx = (CallingContext) ois.readObject();
						// Delegate execution to the CommandProcessor
						objectToReturn = processor.process(connuid, uid, cmd, ctx);
					} else if (method.equals(ServletCommandSinkIdentifier.CONNECT_COMMAND)) {
						final String url = ois.readUTF();
						final Properties props = (Properties) ois.readObject();
						final Properties clientInfo = (Properties) ois.readObject();
						final CallingContext ctx = (CallingContext) ois.readObject();

						final ConnectionConfiguration connectionConfiguration = config.getConnection(url);

						if (connectionConfiguration != null) {
							final Connection conn = connectionConfiguration.create(props);
							objectToReturn = processor.registerConnection(conn, connectionConfiguration, clientInfo, ctx);
						} else {
							objectToReturn = new SQLException("VJDBC-Connection " + url + " not found");
						}
					}
				} catch (final Throwable t) {
					// Wrap any exception so that it can be transported back to
					// the client
					objectToReturn = SQLExceptionHelper.wrap(t);
				}

				// Write the result in the response buffer
				oos.writeObject(objectToReturn);
				oos.flush();

				httpServletResponse.flushBuffer();
			} else {
				// No VJDBC-Method ? Then we redirect the stupid browser user to
				// some information page :-)
				httpServletResponse.sendRedirect("index.html");
			}
		} catch (final Exception e) {
			LOGGER.error("Unexpected Exception", e);
			throw new ServletException(e);
		} finally {
			StreamCloser.close(ois);
			StreamCloser.close(oos);
		}
	}
}
