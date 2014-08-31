package com.bzsoft.wjdbc.transport;

import java.sql.Statement;

public interface JdbcStatementTransport<T extends Statement> extends JdbcObjectTransport<T> {

	public int getQueryTimeout();

	public int getMaxRows();

}
