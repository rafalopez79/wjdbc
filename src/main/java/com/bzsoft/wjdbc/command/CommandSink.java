// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import java.sql.SQLException;
import java.util.Properties;

import com.bzsoft.wjdbc.serial.CallingContext;


/**
 * Interface which each CommandSink must implement to be used for VJDBC
 * client-server communication.
 */
public interface CommandSink {

	ConnectResult connect(String database, Properties props, Properties clientInfo, CallingContext ctx) throws SQLException;

	<R, P> R process(long connuid, long uid, Command<R, P> cmd, CallingContext ctx) throws SQLException;

	void close();
}
