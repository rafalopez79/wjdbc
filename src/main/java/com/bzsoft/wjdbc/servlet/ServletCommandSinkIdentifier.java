package com.bzsoft.wjdbc.servlet;

/**
 * Common identifiers which are used in the Http-Header to route the requests to
 * the corresponding handler.
 *
 * @author Mike
 *
 */
public final class ServletCommandSinkIdentifier {

	private ServletCommandSinkIdentifier() {
		// empty
	}

	public static final String	METHOD_IDENTIFIER	= "vjdbc-method";
	public static final String	CONNECT_COMMAND	= "connect";
	public static final String	PROCESS_COMMAND	= "process";
}
