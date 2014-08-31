// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Savepoint;
import java.sql.Statement;

import com.bzsoft.wjdbc.server.command.ResultSetHolder;


public final class JdbcInterfaceType {

	private JdbcInterfaceType() {
		// empty
	}

	public static final Class<?>[]	INTERFACES			= new Class[] { null, CallableStatement.class, Connection.class, DatabaseMetaData.class,
			PreparedStatement.class, Savepoint.class, Statement.class, ResultSetHolder.class };

	public static final int				CALLABLESTATEMENT	= 1;
	public static final int				CONNECTION			= 2;
	public static final int				DATABASEMETADATA	= 3;
	public static final int				PREPAREDSTATEMENT	= 4;
	public static final int				SAVEPOINT			= 5;
	public static final int				STATEMENT			= 6;
	public static final int				RESULTSETHOLDER	= 7;
}
