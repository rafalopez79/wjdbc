package com.bzsoft.wjdbc.servlet;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.WinHttpClients;
import org.apache.http.util.EntityUtils;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.CommandSink;
import com.bzsoft.wjdbc.rmi.sf.LZFCompressingInputStream;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;
import com.bzsoft.wjdbc.util.StreamCloser;

public class ServletCommandSink implements CommandSink {

	private final URL url;
	private final CloseableHttpClient client;

	public ServletCommandSink(final String url) throws SQLException {
		try {
			this.url = new URL(url);
		} catch (final MalformedURLException e) {
			throw new SQLException(e);
		}
		//TODO: spnego
		final boolean spnego = true;
		if (spnego){
			client = WinHttpClients.custom().build();
		}else{
			client = HttpClients.custom().build();
		}
	}

	@Override
	public long connect(final String database, final Properties props, final Properties clientInfo) throws SQLException {
		CloseableHttpResponse response = null;
		ObjectInputStream ois = null;
		try {
			final HttpPost post = new HttpPost(url.toString());
			post.setEntity(new ConnectCallEntity(database, props, clientInfo));
			post.setHeader(ServletCommandConstants.METHOD, ServletCommandConstants.CONNECT);
			response = client.execute(post);
			final StatusLine sl = response.getStatusLine();
			final int statusCode = sl.getStatusCode();
			final HttpEntity entity = response.getEntity();
			if (entity != null && entity.isStreaming() && statusCode == 200) {
				try {
					final InputStream is = entity.getContent();
					ois = new ObjectInputStream(is);
					final Object result = ois.readObject();
					// This might be a SQLException which must be rethrown
					if(result instanceof SQLException) {
						throw (SQLException)result;
					}
					return (Long)result;
				} finally {
					EntityUtils.consume(entity);
				}
			}
			throw new SQLException("Unexpected response");
		} catch(final SQLException e) {
			throw e;
		} catch(final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		} finally {
			// Cleanup resources
			StreamCloser.close(ois, response);
		}
	}

	@Override
	public <R,P> R process(final long connuid, final long uid, final Command<R,P> cmd) throws SQLException {
		ObjectInputStream ois = null;
		CloseableHttpResponse response = null;
		try {
			final HttpPost post = new HttpPost(url.toString());
			post.setEntity(new ProcessCallEntity<R,P>(connuid, uid, cmd));
			post.setHeader(ServletCommandConstants.METHOD, ServletCommandConstants.CONNECT);
			response = client.execute(post);
			final StatusLine sl = response.getStatusLine();
			final int statusCode = sl.getStatusCode();
			final HttpEntity entity = response.getEntity();
			if (entity != null && entity.isStreaming() && statusCode == 200) {
				try {
					final InputStream is = entity.getContent();
					ois = new ObjectInputStream(new LZFCompressingInputStream(is));
					final Object result = ois.readObject();
					if(result instanceof SQLException) {
						throw (SQLException)result;
					}
					return (R)result;
				} finally {
					EntityUtils.consume(entity);
				}
			}
			throw new SQLException("Unexpected response");
		} catch(final SQLException e) {
			throw e;
		} catch(final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		} finally {
			// Cleanup resources
			StreamCloser.close(ois, response);
		}
	}

	@Override
	public void close() {
		StreamCloser.close(client);
	}
}
