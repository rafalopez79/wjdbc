package com.bzsoft.wjdbc.server.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface Executor {

	public Future<?> execute(Runnable command);

	public <T> Future<T> execute(Callable<T> command);

	public <T> Future<T> schedule(Runnable command, long initialDelay, long period, TimeUnit unit);

	public void close();
}
