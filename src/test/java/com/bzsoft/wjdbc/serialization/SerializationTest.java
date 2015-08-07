package com.bzsoft.wjdbc.serialization;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.bzsoft.wjdbc.BaseSerTest;
import com.bzsoft.wjdbc.command.CallableStatementGetArrayCommand;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SerializationTest extends BaseSerTest{

	private static final int MAXINT = Integer.MAX_VALUE;
	private static final int MAXSTRINGSIZE = 1024;

	@Test
	public void testCallableStatementGetArrayCommand() throws Exception{
		final CallableStatementGetArrayCommand icmd1 = new CallableStatementGetArrayCommand();
		final CallableStatementGetArrayCommand ocmd1 = deserialize(serialize(icmd1));
		assertTrue(icmd1.equals(ocmd1));
		final CallableStatementGetArrayCommand icmd2 = new CallableStatementGetArrayCommand(randomInt(MAXINT));
		final CallableStatementGetArrayCommand ocmd2 = deserialize(serialize(icmd2));
		assertTrue(icmd2.equals(ocmd2));
		final CallableStatementGetArrayCommand icmd3 = new CallableStatementGetArrayCommand(randomString(MAXSTRINGSIZE));
		final CallableStatementGetArrayCommand ocmd3 = deserialize(serialize(icmd3));
		assertTrue(icmd3.equals(ocmd3));
	}

}
