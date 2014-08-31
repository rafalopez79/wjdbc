// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.command;

import com.bzsoft.wjdbc.serial.CallingContext;

/**
 * A CallingContextFactory creates CallingContext objects.
 * 
 * @author Mike
 */
public interface CallingContextFactory {

	CallingContext create();
}
