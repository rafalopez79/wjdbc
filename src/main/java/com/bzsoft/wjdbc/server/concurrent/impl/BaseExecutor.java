package com.bzsoft.wjdbc.server.concurrent.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.bzsoft.wjdbc.server.concurrent.Executor;

public class BaseExecutor implements Executor {

	private static final class BaseThreadFactory implements ThreadFactory {

		private final AtomicLong	threadNumber;
		private final String			name;

		protected BaseThreadFactory(final String name) {
			this.name = name;
			threadNumber = new AtomicLong(1);
		}

		@Override
		public Thread newThread(final Runnable r) {
			final Thread t = new Thread(r, name + "-" + threadNumber.getAndIncrement());
			if (!t.isDaemon()) {
				t.setDaemon(true);
			}
			if (t.getPriority() != Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}
			return t;
		}
	}

	private final ScheduledThreadPoolExecutor	schedPool;
	private final ThreadPoolExecutor				workerPool;

	public BaseExecutor(final int numThreads, final int maxThreads) {
		schedPool = new ScheduledThreadPoolExecutor(numThreads, new BaseThreadFactory("WJDBCSched"));
		schedPool.prestartAllCoreThreads();
		workerPool = new ThreadPoolExecutor(numThreads, maxThreads, 2, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new BaseThreadFactory(
				"WJDBCWorker"));
	}

	@Override
	public Future<?> execute(final Runnable command) {
		return workerPool.submit(command);
	}

	@Override
	public <T> Future<T> execute(final Callable<T> command) {
		return workerPool.submit(command);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Future<T> schedule(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
		return (Future<T>) schedPool.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	@Override
	public void close() {
		schedPool.shutdownNow();
		workerPool.shutdownNow();
	}
}