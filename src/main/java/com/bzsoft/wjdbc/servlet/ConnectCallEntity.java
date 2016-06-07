package com.bzsoft.wjdbc.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.http.entity.AbstractHttpEntity;

import com.bzsoft.wjdbc.util.StreamCloser;

/**
 * The Class ConnectCallEntity.
 */
public class ConnectCallEntity extends AbstractHttpEntity {

	private final String database;
	private final Properties props;
	private final Properties clientInfo;


	public ConnectCallEntity(final String database, final Properties props, final Properties clientInfo) throws IOException {
		this.database = database;
		this.props = props;
		this.clientInfo = clientInfo;
		setChunked(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getContent() throws IOException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getContentLength() {
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRepeatable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStreaming() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		if (outstream == null) {
			throw new IllegalArgumentException("Output stream may not be null");
		}
		ObjectOutputStream oos = null;
		try{
			oos = new ObjectOutputStream(outstream);
			oos.writeUTF(database);
			oos.writeObject(props);
			oos.writeObject(clientInfo);
		}finally{
			StreamCloser.close(oos);
		}
	}
}
