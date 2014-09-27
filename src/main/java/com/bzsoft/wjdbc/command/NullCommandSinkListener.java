package com.bzsoft.wjdbc.command;

public class NullCommandSinkListener implements CommandSinkListener {

	@Override
	public <R, P> void preExecution(final Command<R, P> cmd) {
		// empty
	}

	@Override
	public <R, P> void postExecution(final Command<R, P> cmd) {
		// empty
	}
}
