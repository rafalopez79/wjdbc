package com.bzsoft.wjdbc.command;

import com.bzsoft.wjdbc.serial.CallingContext;

/**
 * Dummy class which is doesn't create CallingContexts but returns null.
 */
public class NullCallingContextFactory implements CallingContextFactory {

	@Override
	public CallingContext create() {
		return null;
	}
}
