package com.bzsoft.wjdbc.server.concurrent.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CommonjFuture<V> implements Future<V> {

	private final Thread thread;

	public CommonjFuture(final Thread thread) {
		this.thread = thread;
	}

	@Override
	public boolean cancel(final boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		return null;
	}

	@Override
	public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return null;
	}

}
