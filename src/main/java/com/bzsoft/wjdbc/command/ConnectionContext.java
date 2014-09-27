package com.bzsoft.wjdbc.command;

import java.sql.SQLException;

/**
 * This interface provides access to connection specific context for all
 * commands executed on the server.
 */
public interface ConnectionContext {

	Object getJDBCObject(long key);

	void addJDBCObject(long key, Object partner);

	Object removeJDBCObject(long key);

	int getRowPacketSize();

	String getCharset();

	String resolveOrCheckQuery(String sql) throws SQLException;

	void closeAllRelatedJdbcObjects() throws SQLException;
}
