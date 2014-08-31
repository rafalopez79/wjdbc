// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

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

	// Row-Packets
	int getRowPacketSize();

	String getCharset();

	// Resolve and check query
	String resolveOrCheckQuery(String sql) throws SQLException;

	// convenience method to remove all related JdbcObjects from this connection
	void closeAllRelatedJdbcObjects() throws SQLException;
}
