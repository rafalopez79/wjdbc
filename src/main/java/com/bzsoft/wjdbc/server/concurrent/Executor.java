/*
  File: Executor.java

  Originally written by Doug Lea and released into the public domain.
  This may be used for any purposes whatsoever without acknowledgment.
  Thanks for the assistance and support of Sun Microsystems Labs,
  and everyone contributing, testing, and using this code.

  History:
  Date       Who                What
  19Jun1998  dl               Create public version
 */

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
