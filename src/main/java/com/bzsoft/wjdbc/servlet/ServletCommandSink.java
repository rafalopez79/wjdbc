package com.bzsoft.wjdbc.servlet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.CommandSink;
import com.bzsoft.wjdbc.rmi.sf.LZFCompressingInputStream;
import com.bzsoft.wjdbc.rmi.sf.LZFCompressingOutputStream;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;
import com.bzsoft.wjdbc.util.StreamCloser;

public class ServletCommandSink implements CommandSink {

	private final URL url;

	public ServletCommandSink(final String url) throws SQLException {
		try {
			this.url = new URL(url);
		} catch (final MalformedURLException e) {
			throw new SQLException(e);
		}
	}

	@Override
	public long connect(final String database, final Properties props, final Properties clientInfo) throws SQLException {
		HttpURLConnection conn = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;

		try {
			// Open connection and adjust the Input/Output
			conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setAllowUserInteraction(false);
			conn.setUseCaches(false);
			conn.setRequestProperty(ServletCommandConstants.METHOD,
					ServletCommandConstants.CONNECT);
			oos = new ObjectOutputStream(conn.getOutputStream());
			oos.writeUTF(database);
			oos.writeObject(props);
			oos.writeObject(clientInfo);
			oos.flush();
			// Connect ...
			conn.connect();
			// Read the result object from the InputStream
			ois = new ObjectInputStream(conn.getInputStream());
			final Object result = ois.readObject();
			// This might be a SQLException which must be rethrown
			if(result instanceof SQLException) {
				throw (SQLException)result;
			}
			return (Long)result;
		} catch(final SQLException e) {
			throw e;
		} catch(final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		} finally {
			// Cleanup resources
			StreamCloser.close(oos);
			StreamCloser.close(ois);
			if(conn != null) {
				conn.disconnect();
			}
		}
	}

	@Override
	public <R,P> R process(final long connuid, final long uid, final Command<R,P> cmd) throws SQLException {
		HttpURLConnection conn = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;

		try {
			conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty(ServletCommandConstants.METHOD, ServletCommandConstants.PROCESS);
			conn.connect();

			oos = new ObjectOutputStream(new LZFCompressingOutputStream(conn.getOutputStream()));
			oos.writeLong(connuid);
			oos.writeLong(uid);
			oos.writeObject(cmd);
			oos.flush();

			ois = new ObjectInputStream(new LZFCompressingInputStream(conn.getInputStream()));
			final Object result = ois.readObject();
			if(result instanceof SQLException) {
				throw (SQLException)result;
			}
			return (R) result;
		} catch(final SQLException e) {
			throw e;
		} catch(final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		} finally {
			// Cleanup resources
			StreamCloser.close(oos);
			StreamCloser.close(ois);
			if(conn != null) {
				conn.disconnect();
			}
		}
	}

	@Override
	public void close() {
		//empty
	}
}
