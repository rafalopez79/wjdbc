package com.bzsoft.wjdbc.command;

import java.sql.SQLException;
import java.util.Properties;

public interface CommandSink {

	long connect(String database, Properties props, Properties clientInfo) throws SQLException;

	<R, P> R process(long connuid, long uid, Command<R, P> cmd) throws SQLException;

	void close();
}
