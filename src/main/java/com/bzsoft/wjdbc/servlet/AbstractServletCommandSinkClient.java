package com.bzsoft.wjdbc.servlet;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import com.bzsoft.wjdbc.command.CommandSink;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;

/**
 * Abstract base class for clients of WJDBC in Servlet-Mode.
 *
 * @author Mike
 *
 */
public abstract class AbstractServletCommandSinkClient implements CommandSink {

	protected final URL					url;
	protected final RequestEnhancer	requestEnhancer;

	public AbstractServletCommandSinkClient(final String url, final RequestEnhancer requestEnhancer) throws SQLException {
		try {
			this.url = new URL(url);
			this.requestEnhancer = requestEnhancer;
		} catch (final IOException e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public void close() {
		// Nothing to do
	}
}