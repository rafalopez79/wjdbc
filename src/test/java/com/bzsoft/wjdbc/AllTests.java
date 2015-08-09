package com.bzsoft.wjdbc;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.bzsoft.wjdbc.serialization.SerializationTest;

@RunWith(Suite.class)
@SuiteClasses({ SerializationTest.class})
public class AllTests {

	public static void main(final String[] args) {
		System.setProperty("blockOnClose","true");
		final String[] params = {SerializationTest.class.getName()};
		JUnitCore.main(params);
	}
}
