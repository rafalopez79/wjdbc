package com.bzsoft.wjdbc.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.http.entity.AbstractHttpEntity;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.rmi.sf.LZFCompressingOutputStream;
import com.bzsoft.wjdbc.util.StreamCloser;

/**
 * The Class ConnectCallEntity.
 */
public class ProcessCallEntity<R,P> extends AbstractHttpEntity {

	private final long connuid;
	private final long uid;
	private final Command<R,P> cmd;

	public ProcessCallEntity(final long connuid, final long uid, final Command<R,P> cmd) throws IOException {
		this.connuid = connuid;
		this.uid = uid;
		this.cmd = cmd;
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
			oos = new ObjectOutputStream(new LZFCompressingOutputStream(outstream));
			oos.writeLong(connuid);
			oos.writeLong(uid);
			oos.writeObject(cmd);
		}finally{
			StreamCloser.close(oos);
		}
	}
}
