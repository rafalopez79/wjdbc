package com.bzsoft.wjdbc.servlet.jakarta;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.httpclient.methods.RequestEntity;

/**
 * RequestEntity implementation which streams all of the parameters necessary
 * for a connect request.
 *
 * @author Mike
 */
class ConnectRequestEntity implements RequestEntity {
	private final String			_database;
	private final Properties	_props;
	private final Properties	_clientInfo;

	public ConnectRequestEntity(final String database, final Properties props, final Properties clientInfo) {
		_database = database;
		_props = props;
		_clientInfo = clientInfo;
	}

	@Override
	public long getContentLength() {
		return -1; // Don't know the length in advance
	}

	@Override
	public String getContentType() {
		return "binary/x-java-serialized";
	}

	@Override
	public boolean isRepeatable() {
		return true;
	}

	@Override
	public void writeRequest(final OutputStream os) throws IOException {
		final ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeUTF(_database);
		oos.writeObject(_props);
		oos.writeObject(_clientInfo);
		oos.flush();
	}
}
