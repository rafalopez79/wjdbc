// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.test.junit.hsqldb;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.DriverManager;

import com.bzsoft.wjdbc.test.junit.VJdbcTest;
import com.bzsoft.wjdbc.test.junit.general.AddressTest;

public class HSqlDbAddressTestDriverManager extends AddressTest {
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        
        VJdbcTest.addAllTestMethods(suite, HSqlDbAddressTestDriverManager.class);

        TestSetup wrapper = new TestSetup(suite) {
            protected void setUp() throws Exception {
                new HSqlDbAddressTestDriverManager("").oneTimeSetup();
            }

            protected void tearDown() throws Exception {
                new HSqlDbAddressTestDriverManager("").oneTimeTearDown();
            }
        };

        return wrapper;
    }

    public HSqlDbAddressTestDriverManager(String s) {
        super(s);
    }

    protected Connection createNativeDatabaseConnection() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/HSqlDb", "sa", "");
    }

    protected String getVJdbcDatabaseShortcut() {
        return "HSqlDB";
    }
}
