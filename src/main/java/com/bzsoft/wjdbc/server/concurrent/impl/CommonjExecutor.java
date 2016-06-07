package com.bzsoft.wjdbc.server.concurrent.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.bzsoft.wjdbc.server.concurrent.Executor;
import commonj.work.Work;
import commonj.work.WorkItem;
import commonj.work.WorkManager;

public class CommonjExecutor implements Executor {

	private final WorkManager wm;

	public CommonjExecutor(final String jndiName) {
		try{
			wm = InitialContext.doLookup(jndiName);
		}catch(final NamingException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public Future<?> execute(final Runnable command) {
		final Work work = new Work() {

			@Override
			public void run() {
				command.run();
			}

			@Override
			public void release() {
				//empty
			}

			@Override
			public boolean isDaemon() {
				return true;
			}
		};
		final WorkItem wi = wm.schedule(work);
		return null;
	}

	@Override
	public <T> Future<T> execute(final Callable<T> command) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Future<T> schedule(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		//empty
	}

}
