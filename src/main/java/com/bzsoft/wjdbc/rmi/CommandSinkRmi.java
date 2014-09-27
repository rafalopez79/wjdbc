package com.bzsoft.wjdbc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Properties;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.ConnectResult;
import com.bzsoft.wjdbc.serial.CallingContext;

public interface CommandSinkRmi extends Remote {

	ConnectResult connect(String url, Properties props, Properties clientInfo, CallingContext ctx) throws SQLException, RemoteException;

	<R, V> R process(long connuid, long uid, Command<R, V> cmd, CallingContext ctx) throws SQLException, RemoteException;
}
