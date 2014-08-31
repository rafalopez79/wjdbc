// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.servlet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.Properties;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.ConnectResult;
import com.bzsoft.wjdbc.serial.CallingContext;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;
import com.bzsoft.wjdbc.util.StreamCloser;


public class ServletCommandSinkJdkHttpClient extends AbstractServletCommandSinkClient {

	public ServletCommandSinkJdkHttpClient(final String url, final RequestEnhancer requestEnhancer) throws SQLException {
		super(url, requestEnhancer);
	}

	@Override
	public ConnectResult connect(final String database, final Properties props, final Properties clientInfo, final CallingContext ctx)
			throws SQLException {
		HttpURLConnection conn = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;

		try {
			// Open connection and adjust the Input/Output
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setAllowUserInteraction(false); // system may not ask the user
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-type", "binary/x-java-serialized");
			conn.setRequestProperty(ServletCommandSinkIdentifier.METHOD_IDENTIFIER, ServletCommandSinkIdentifier.CONNECT_COMMAND);
			// Finally let the optional Request-Enhancer set request properties
			if (requestEnhancer != null) {
				requestEnhancer.enhanceConnectRequest(new RequestModifierJdk(conn));
			}
			// Write the parameter objects to the ObjectOutputStream
			oos = new ObjectOutputStream(conn.getOutputStream());
			oos.writeUTF(database);
			oos.writeObject(props);
			oos.writeObject(clientInfo);
			oos.writeObject(ctx);
			oos.flush();
			// Connect ...
			conn.connect();
			// Read the result object from the InputStream
			ois = new ObjectInputStream(conn.getInputStream());
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
			StreamCloser.close(ois);
			StreamCloser.close(oos);

			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	@Override
	public <R, P> R process(final long connuid, final long uid, final Command<R, P> cmd, final CallingContext ctx) throws SQLException {
		HttpURLConnection conn = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;

		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty(ServletCommandSinkIdentifier.METHOD_IDENTIFIER, ServletCommandSinkIdentifier.PROCESS_COMMAND);
			// Finally let the optional Request-Enhancer set request properties
			if (requestEnhancer != null) {
				requestEnhancer.enhanceProcessRequest(new RequestModifierJdk(conn));
			}
			conn.connect();

			oos = new ObjectOutputStream(conn.getOutputStream());
			oos.writeObject(connuid);
			oos.writeObject(uid);
			oos.writeObject(cmd);
			oos.writeObject(ctx);
			oos.flush();

			ois = new ObjectInputStream(conn.getInputStream());
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
			StreamCloser.close(ois);
			StreamCloser.close(oos);

			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}
