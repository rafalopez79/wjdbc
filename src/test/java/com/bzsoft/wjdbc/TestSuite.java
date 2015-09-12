package com.bzsoft.wjdbc;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.bzsoft.wjdbc.driver.CloseJdbcObjectsTest;
import com.bzsoft.wjdbc.driver.DDLTest;
import com.bzsoft.wjdbc.driver.DriverTest;
import com.bzsoft.wjdbc.serialization.SerializationTest;

@RunWith(Suite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuiteClasses({ SerializationTest.class, DriverTest.class, DDLTest.class, CloseJdbcObjectsTest.class })
public class TestSuite {

	protected static final Logger	LOGGER	= Logger.getLogger(TestSuite.class);

	@BeforeClass
	public static void setUpTestSuite() throws Exception{
		LOGGER.info("Setting up TestSuite");
	}


	@AfterClass
	public static void tearDownTestSuite()  throws Exception{
		LOGGER.info("Tearing down TestSuite");
		BaseTest.tearDownServer();
	}


	public static void main(final String[] args) {
		System.setProperty("blockOnClose", "true");
		final String[] params = { SerializationTest.class.getName(), DriverTest.class.getName(), DDLTest.class.getName(),
				CloseJdbcObjectsTest.class.getName() };
		JUnitCore.main(params);
	}
}
