package com.bzsoft.wjdbc.transport;

import java.io.Externalizable;

public interface JdbcObjectTransport<T> extends Externalizable {

	public T getJDBCObject();

	public long getUID();

}
