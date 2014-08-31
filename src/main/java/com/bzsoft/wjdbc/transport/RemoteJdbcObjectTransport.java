package com.bzsoft.wjdbc.transport;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class RemoteJdbcObjectTransport<T> implements JdbcObjectTransport<T> {

	protected long				uid;
	protected transient T	object;

	public RemoteJdbcObjectTransport() {
		// empty
	}

	public RemoteJdbcObjectTransport(final long uid, final T object) {
		this.uid = uid;
		this.object = object;
	}

	@Override
	public T getJDBCObject() {
		return object;
	}

	@Override
	public long getUID() {
		return uid;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		uid = in.readLong();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeLong(uid);
	}
}
