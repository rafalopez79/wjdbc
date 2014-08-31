// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import com.bzsoft.wjdbc.serial.CallingContext;

/**
 * This class produces standard Calling-Contexts which contain the callstack of
 * the executing command.
 */
public class StandardCallingContextFactory implements CallingContextFactory {

	@Override
	public CallingContext create() {
		return new CallingContext();
	}
}
