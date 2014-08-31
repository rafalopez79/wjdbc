// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.servlet.jakarta;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.ConnectResult;
import com.bzsoft.wjdbc.serial.CallingContext;
import com.bzsoft.wjdbc.servlet.AbstractServletCommandSinkClient;
import com.bzsoft.wjdbc.servlet.RequestEnhancer;
import com.bzsoft.wjdbc.servlet.ServletCommandSinkIdentifier;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;
import com.bzsoft.wjdbc.util.StreamCloser;


/**
 * ServletCommandSinkClient implementation which uses Jakarta-HttpClient to
 * communicate with the web server.
 * 
 * @author Mike
 */
public class ServletCommandSinkJakartaHttpClient extends AbstractServletCommandSinkClient {

	private final String											_urlExternalForm;
	private final HttpClient									_httpClient;
	private final MultiThreadedHttpConnectionManager	_multiThreadedHttpConnectionManager;

	public ServletCommandSinkJakartaHttpClient(final String url, final RequestEnhancer requestEnhancer) throws SQLException {
		super(url, requestEnhancer);
		_urlExternalForm = this.url.toExternalForm();
		_multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
		_httpClient = new HttpClient(_multiThreadedHttpConnectionManager);

		_httpClient.getParams().setBooleanParameter("http.connection.stalecheck", false);
	}

	@Override
	public void close() {
		super.close();
		_multiThreadedHttpConnectionManager.shutdown();
	}

	@Override
	public ConnectResult connect(final String database, final Properties props, final Properties clientInfo, final CallingContext ctx)
			throws SQLException {
		PostMethod post = null;
		final ObjectOutputStream oos = null;
		ObjectInputStream ois = null;

		try {
			// Open connection and adjust the Input/Output
			post = new PostMethod(_urlExternalForm);
			post.setDoAuthentication(false);
			post.setFollowRedirects(false);
			post.setRequestHeader("Content-type", "binary/x-java-serialized");
			post.setRequestHeader(ServletCommandSinkIdentifier.METHOD_IDENTIFIER, ServletCommandSinkIdentifier.CONNECT_COMMAND);
			// Finally let the optional Request-Enhancer set request headers
			if (requestEnhancer != null) {
				requestEnhancer.enhanceConnectRequest(new RequestModifierJakartaHttpClient(post));
			}
			// Write the parameter objects using a ConnectRequestEntity
			post.setRequestEntity(new ConnectRequestEntity(database, props, clientInfo, ctx));

			// Call ...
			_httpClient.executeMethod(post);

			// Check the HTTP status.
			if (post.getStatusCode() != HttpStatus.SC_OK) {
				throw SQLExceptionHelper.wrap(new HttpClientError(post.getStatusLine().toString()));
			}
			// Read the result object from the InputStream
			ois = new ObjectInputStream(new BufferedInputStream(post.getResponseBodyAsStream()));
			final Object result = ois.readObject();
			// This might be a SQLException which must be rethrown
			if (result instanceof SQLException) {
				throw (SQLException) result;
			}
			return (ConnectResult) result;

		} catch (final SQLException e) {
			throw e;
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		} finally {
			// Cleanup resources
			StreamCloser.close(oos);
			StreamCloser.close(ois);

			if (post != null) {
				post.releaseConnection();
			}
		}
	}

	@Override
	public <R, P> R process(final long connuid, final long uid, final Command<R, P> cmd, final CallingContext ctx) throws SQLException {
		PostMethod post = null;
		final ObjectOutputStream oos = null;
		ObjectInputStream ois = null;

		try {
			post = new PostMethod(_urlExternalForm);
			post.setDoAuthentication(false);
			post.setFollowRedirects(false);
			post.setContentChunked(false);
			post.setRequestHeader(ServletCommandSinkIdentifier.METHOD_IDENTIFIER, ServletCommandSinkIdentifier.PROCESS_COMMAND);
			// Finally let the optional Request-Enhancer set request properties
			if (requestEnhancer != null) {
				requestEnhancer.enhanceProcessRequest(new RequestModifierJakartaHttpClient(post));
			}
			// Write the parameter objects using a ProcessRequestEntity
			post.setRequestEntity(new ProcessRequestEntity(connuid, uid, cmd, ctx));

			// Call ...
			_httpClient.executeMethod(post);

			if (post.getStatusCode() != HttpStatus.SC_OK) {
				throw SQLExceptionHelper.wrap(new HttpClientError(post.getStatusLine().toString()));
			}
			ois = new ObjectInputStream(new BufferedInputStream(post.getResponseBodyAsStream()));
			final Object result = ois.readObject();
			if (result instanceof SQLException) {
				throw (SQLException) result;
			}
			return (R) result;
		} catch (final SQLException e) {
			throw e;
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		} finally {
			// Cleanup resources
			StreamCloser.close(oos);
			StreamCloser.close(ois);

			if (post != null) {
				post.releaseConnection();
			}
		}
	}
}
