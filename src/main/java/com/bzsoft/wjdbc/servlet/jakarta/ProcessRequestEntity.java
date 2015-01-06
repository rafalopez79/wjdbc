package com.bzsoft.wjdbc.servlet.jakarta;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.methods.RequestEntity;

import com.bzsoft.wjdbc.command.Command;

public class ProcessRequestEntity implements RequestEntity {

	private final Long		_connuid;
	private final Long		_uid;
	private final Command	_cmd;

	public ProcessRequestEntity(final Long connuid, final Long uid, final Command cmd) {
		_connuid = connuid;
		_uid = uid;
		_cmd = cmd;
	}

	@Override
	public long getContentLength() {
		return -1; // Don't know length in advance
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
		oos.writeObject(_connuid);
		oos.writeObject(_uid);
		oos.writeObject(_cmd);
		oos.flush();
	}
}
