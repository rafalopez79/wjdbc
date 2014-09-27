package com.bzsoft.wjdbc.command;

import java.io.Externalizable;
import java.sql.SQLException;

/**
 * Interface for all commands which shall be executed by the server
 * CommandProcessor.
 */
public interface Command<R, P> extends Externalizable {

	R execute(P target, ConnectionContext ctx) throws SQLException;

}
