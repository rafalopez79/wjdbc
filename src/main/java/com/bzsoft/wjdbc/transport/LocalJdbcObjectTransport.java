package com.bzsoft.wjdbc.transport;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class LocalJdbcObjectTransport<T> implements JdbcObjectTransport<T> {

	protected T	value;

	public LocalJdbcObjectTransport() {
		// empty
	}

	public LocalJdbcObjectTransport(final T value) {
		this.value = value;
	}

	public static final <T> LocalJdbcObjectTransport<T> of(final T val) {
		return new LocalJdbcObjectTransport<T>(val);
	}

	@Override
	public T getJDBCObject() {
		return value;
	}

	@Override
	public long getUID() {
		return 0;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		// empty
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		// empty
	}
}
