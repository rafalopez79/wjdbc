package com.bzsoft.wjdbc.serialization;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.bzsoft.wjdbc.BaseSerTest;
import com.bzsoft.wjdbc.command.CallableStatementGetArrayCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetBlobCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetCharacterStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetClobCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetNCharacterStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetNClobCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetObjectCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetRefCommand;
import com.bzsoft.wjdbc.command.CallableStatementGetSQLXMLCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetAsciiStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetBinaryStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetBlobCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetCharacterStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetClobCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetNCharacterStreamCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetNClobCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetObjectCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetRowIdCommand;
import com.bzsoft.wjdbc.command.CallableStatementSetSQLXMLCommand;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-method")
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
		final CallableStatementGetArrayCommand icmd4 = new CallableStatementGetArrayCommand(null);
		final CallableStatementGetArrayCommand ocmd4 = deserialize(serialize(icmd4));
		assertTrue(icmd4.equals(ocmd4));
	}

	@Test
	public void testCallableStatementGetBlobCommand() throws Exception{
		final CallableStatementGetBlobCommand icmd1 = new CallableStatementGetBlobCommand();
		final CallableStatementGetBlobCommand ocmd1 = deserialize(serialize(icmd1));
		assertTrue(icmd1.equals(ocmd1));
		final CallableStatementGetBlobCommand icmd2 = new CallableStatementGetBlobCommand(randomInt(MAXINT));
		final CallableStatementGetBlobCommand ocmd2 = deserialize(serialize(icmd2));
		assertTrue(icmd2.equals(ocmd2));
		final CallableStatementGetBlobCommand icmd3 = new CallableStatementGetBlobCommand(randomString(MAXSTRINGSIZE));
		final CallableStatementGetBlobCommand ocmd3 = deserialize(serialize(icmd3));
		assertTrue(icmd3.equals(ocmd3));
		final CallableStatementGetBlobCommand icmd4 = new CallableStatementGetBlobCommand(null);
		final CallableStatementGetBlobCommand ocmd4 = deserialize(serialize(icmd4));
		assertTrue(icmd4.equals(ocmd4));
	}

	@Test
	public void testCallableStatementGetCharacgterStreamCommand() throws Exception{
		final CallableStatementGetCharacterStreamCommand icmd1 = new CallableStatementGetCharacterStreamCommand();
		final CallableStatementGetCharacterStreamCommand ocmd1 = deserialize(serialize(icmd1));
		assertTrue(icmd1.equals(ocmd1));
		final CallableStatementGetCharacterStreamCommand icmd2 = new CallableStatementGetCharacterStreamCommand(randomInt(MAXINT));
		final CallableStatementGetCharacterStreamCommand ocmd2 = deserialize(serialize(icmd2));
		assertTrue(icmd2.equals(ocmd2));
		final CallableStatementGetCharacterStreamCommand icmd3 = new CallableStatementGetCharacterStreamCommand(randomString(MAXSTRINGSIZE));
		final CallableStatementGetCharacterStreamCommand ocmd3 = deserialize(serialize(icmd3));
		assertTrue(icmd3.equals(ocmd3));
		final CallableStatementGetCharacterStreamCommand icmd4 = new CallableStatementGetCharacterStreamCommand(null);
		final CallableStatementGetCharacterStreamCommand ocmd4 = deserialize(serialize(icmd4));
		assertTrue(icmd4.equals(ocmd4));
	}

	@Test
	public void testVoidConstructor() throws Exception{
		testSerializationVoidConstructor(CallableStatementGetArrayCommand.class,
				CallableStatementGetBlobCommand.class,
				CallableStatementGetCharacterStreamCommand.class,
				CallableStatementGetClobCommand.class,
				CallableStatementGetNCharacterStreamCommand.class,
				CallableStatementGetNClobCommand.class,
				CallableStatementGetObjectCommand.class,
				CallableStatementGetRefCommand.class,
				CallableStatementGetSQLXMLCommand.class,
				CallableStatementSetAsciiStreamCommand.class,
				CallableStatementSetBinaryStreamCommand.class,
				CallableStatementSetBlobCommand.class,
				CallableStatementSetCharacterStreamCommand.class,
				CallableStatementSetClobCommand.class,
				CallableStatementSetNCharacterStreamCommand.class,
				CallableStatementSetNClobCommand.class,
				CallableStatementSetObjectCommand.class,
				CallableStatementSetRowIdCommand.class,
				CallableStatementSetSQLXMLCommand.class);
	}

	@Test
	public void testStringConstructor() throws Exception{
		testSerializationStringConstructor(
				CallableStatementGetArrayCommand.class,
				CallableStatementGetBlobCommand.class,
				CallableStatementGetCharacterStreamCommand.class,
				CallableStatementGetClobCommand.class,
				CallableStatementGetNCharacterStreamCommand.class,
				CallableStatementGetNClobCommand.class,
				//CallableStatementGetObjectCommand.class,
				CallableStatementGetRefCommand.class,
				CallableStatementGetSQLXMLCommand.class
				//CallableStatementSetAsciiStreamCommand.class,
				//CallableStatementSetBinaryStreamCommand.class,
				//CallableStatementSetBlobCommand.class,
				//CallableStatementSetCharacterStreamCommand.class,
				//CallableStatementSetClobCommand.class,
				//CallableStatementSetNCharacterStreamCommand.class,
				//CallableStatementSetNClobCommand.class,
				//CallableStatementSetObjectCommand.class,
				//CallableStatementSetRowIdCommand.class,
				//CallableStatementSetSQLXMLCommand.class
				);
	}


	private static final void testSerializationStringConstructor(final Class<?> ...classes) throws Exception{
		for(final Class<?> clazz : classes){
			final String str = randomString(MAXSTRINGSIZE);
			final Object in = clazz.getConstructor(String.class).newInstance(str);
			final Object out = deserialize(serialize(in));
			assertTrue(in != null && out != null);
			final byte[] bin = serialize(in);
			final byte[] bout = serialize(out);
			assertTrue(equals(bin, bout));
		}
	}

	private static final void testSerializationVoidConstructor(final Class<?> ...classes) throws Exception{
		for(final Class<?> clazz : classes){
			final Object in = clazz.newInstance();
			final Object out = deserialize(serialize(in));
			assertTrue(in != null && out != null);
			final byte[] bin = serialize(in);
			final byte[] bout = serialize(out);
			assertTrue(equals(bin, bout));
		}
	}
}
