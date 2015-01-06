package com.bzsoft.wjdbc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Properties;

import com.bzsoft.wjdbc.command.Command;

public interface CommandSinkRmi extends Remote {

	long connect(String url, Properties props, Properties clientInfo) throws SQLException, RemoteException;

	<R, V> R process(long connuid, long uid, Command<R, V> cmd) throws SQLException, RemoteException;
}
