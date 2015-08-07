package com.bzsoft.wjdbc.command;

public abstract class BaseCommand<R, P> implements Command<R, P> {

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);
}
