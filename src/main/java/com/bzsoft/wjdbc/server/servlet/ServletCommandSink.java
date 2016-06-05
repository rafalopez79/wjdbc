
package com.bzsoft.wjdbc.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.rmi.sf.LZFCompressingInputStream;
import com.bzsoft.wjdbc.rmi.sf.LZFCompressingOutputStream;
import com.bzsoft.wjdbc.server.command.CommandProcessor;
import com.bzsoft.wjdbc.server.config.ConfigurationException;
import com.bzsoft.wjdbc.server.config.ConnectionConfiguration;
import com.bzsoft.wjdbc.server.config.WJdbcConfiguration;
import com.bzsoft.wjdbc.servlet.ServletCommandConstants;
import com.bzsoft.wjdbc.util.IgnoreCloseInputStream;
import com.bzsoft.wjdbc.util.IgnoreCloseOutputStream;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;
import com.bzsoft.wjdbc.util.StreamCloser;

public class ServletCommandSink extends HttpServlet {

	private static final long	serialVersionUID	= -2480165771562580510L;

	private static final String INIT_PARAMETER_CONFIG_RESOURCE = "config-resource";
	private static final String INIT_PARAMETER_CONFIG_VARIABLES = "config-variables";
	private static final String DEFAULT_CONFIG_RESOURCE = "/WEB-INF/wjdbc-config.xml";


	private static final Logger LOGGER = LoggerFactory.getLogger(ServletCommandSink.class);

	private CommandProcessor processor;
	private WJdbcConfiguration config;

	public ServletCommandSink() {
		//empty
	}

	@Override
	public void init(final ServletConfig servletConfig) throws ServletException {
		String configResource = servletConfig.getInitParameter(INIT_PARAMETER_CONFIG_RESOURCE);
		if(configResource == null) {
			configResource = DEFAULT_CONFIG_RESOURCE;
		}
		LOGGER.info("Trying to get config resource {}...",configResource);
		final InputStream cis = getClass().getResourceAsStream(configResource);
		if(cis == null) {
			final String msg = "WJDBC-Configuration " + configResource + " not found !";
			LOGGER.error(msg);
			throw new ServletException(msg);
		}
		final String configVariables = servletConfig.getInitParameter(INIT_PARAMETER_CONFIG_VARIABLES);
		Properties configVariablesProps = null;
		if(configVariables != null) {
			LOGGER.info("... using variables specified in " + configVariables);
			InputStream configVariablesInputStream = null;
			try {
				configVariablesInputStream = getClass().getResourceAsStream(configVariables);
				if(configVariablesInputStream == null) {
					final String msg = "Configuration-Variables " + configVariables + " not found !";
					LOGGER.error(msg);
					throw new ServletException(msg);
				}
				configVariablesProps = new Properties();
				configVariablesProps.load(configVariablesInputStream);
			} catch (final IOException e) {
				final String msg = "Reading of configuration variables failed";
				LOGGER.error(msg, e);
				throw new ServletException(msg, e);
			} finally {
				StreamCloser.close(configVariablesInputStream);
			}
		}
		try {
			LOGGER.info("Initialize WJDBC-Configuration");
			config = WJdbcConfiguration.of(cis, configVariablesProps);
			processor = new CommandProcessor(config);
		} catch (final ConfigurationException e) {
			LOGGER.error("Initialization failed", e);
			throw new ServletException("WJDBC-Initialization failed", e);
		} finally {
			StreamCloser.close(cis);
		}
	}

	@Override
	public void destroy() {
		processor.destroy();
	}

	@Override
	protected void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException {
		try {
			httpServletResponse.sendError(500);
		} catch (final IOException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		try {
			// Get the method to execute
			final String method = request.getHeader(ServletCommandConstants.METHOD);
			if(method != null) {
				ois = new ObjectInputStream(new LZFCompressingInputStream(new IgnoreCloseInputStream(request.getInputStream())));
				// And initialize the output
				oos = new ObjectOutputStream(new LZFCompressingOutputStream(new IgnoreCloseOutputStream(response.getOutputStream())));
				Object objectToReturn = null;
				try {
					// Some command to process ?
					if(method.equals(ServletCommandConstants.PROCESS)) {
						// Read parameter objects
						final long connuid = ois.readLong();
						final long uid = ois.readLong();
						final Command cmd = (Command) ois.readObject();
						// Delegate execution to the CommandProcessor
						objectToReturn = processor.process(connuid, uid, cmd);
					} else if(method.equals(ServletCommandConstants.CONNECT)) {
						final String url = ois.readUTF();
						final Properties props = (Properties) ois.readObject();
						final Properties clientInfo = (Properties) ois.readObject();
						final ConnectionConfiguration cc = config.getConnection(url);
						if(cc != null) {
							final Connection conn = cc.create(props);
							objectToReturn = processor.registerConnection(conn, cc, clientInfo);
						} else {
							objectToReturn = new SQLException("WJDBC-Connection " + url + " not found");
						}
					}else{
						response.sendError(500);
					}
				} catch (final Throwable t) {
					objectToReturn = SQLExceptionHelper.wrap(t);
				}
				// Write the result in the response buffer
				oos.writeObject(objectToReturn);
				oos.flush();
				response.flushBuffer();
			} else {
				response.sendError(404);
			}
		} catch (final Exception e) {
			LOGGER.error("Unexpected Exception", e);
			throw new ServletException(e);
		} finally {
			StreamCloser.close(ois, oos);
		}
	}
}
