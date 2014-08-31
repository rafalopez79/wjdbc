// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

/**
 * Interface for objects which are interested what is happening in the command
 * sink.
 */
public interface CommandSinkListener {

	<R, P> void preExecution(Command<R, P> cmd);

	<R, P> void postExecution(Command<R, P> cmd);
}
