package com.bzsoft.wjdbc.test;

import java.sql.SQLException;

import junit.framework.TestCase;

import com.bzsoft.wjdbc.util.SQLExceptionHelper;

public class SQLExceptionHelperTest extends TestCase {

	public void testDerivedSQLException() throws Exception {
		final SQLException originalEx = new SQLException();
		final Exception otherEx = new UnsupportedOperationException("Bla");

		final SQLException wex2 = SQLExceptionHelper.wrap(originalEx);
		assertSame(originalEx, wex2);

		final SQLException wex3 = SQLExceptionHelper.wrap(otherEx);
		assertNotSame(otherEx, wex3);
	}
}
