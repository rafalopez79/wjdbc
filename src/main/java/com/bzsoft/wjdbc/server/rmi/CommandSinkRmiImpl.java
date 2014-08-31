// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.server.rmi;

import java.rmi.RemoteException;
import java.rmi.server.Unreferenced;
import java.sql.SQLException;
import java.util.Properties;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.ConnectResult;
import com.bzsoft.wjdbc.rmi.CommandSinkRmi;
import com.bzsoft.wjdbc.serial.CallingContext;
import com.bzsoft.wjdbc.server.command.CommandProcessor;

public class CommandSinkRmiImpl implements CommandSinkRmi, Unreferenced {

	private final CommandProcessor	processor;

	public CommandSinkRmiImpl(final CommandProcessor processor) {
		this.processor = processor;
	}

	@Override
	public void unreferenced() {
		// empty
	}

	@Override
	public ConnectResult connect(final String url, final Properties props, final Properties clientInfo, final CallingContext ctx) throws SQLException,
			RemoteException {
		return processor.createConnection(url, props, clientInfo, ctx);
	}

	@Override
	public <R, V> R process(final long connuid, final long uid, final Command<R, V> cmd, final CallingContext ctx) throws SQLException,
			RemoteException {
		return processor.process(connuid, uid, cmd, ctx);
	}
}
