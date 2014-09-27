// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.test.junit.db2;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.DriverManager;

import com.bzsoft.wjdbc.test.junit.VJdbcTest;
import com.bzsoft.wjdbc.test.junit.general.AddressTest;

public class Db2AddressTest extends AddressTest {
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        
        VJdbcTest.addAllTestMethods(suite, Db2AddressTest.class);

        TestSetup wrapper = new TestSetup(suite) {
            protected void setUp() throws Exception {
                new Db2AddressTest("").oneTimeSetup();
            }

            protected void tearDown() throws Exception {
                new Db2AddressTest("").oneTimeTearDown();
            }
        };

        return wrapper;
    }

    public Db2AddressTest(String s) {
        super(s);
    }

    protected Connection createNativeDatabaseConnection() throws Exception {
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        return DriverManager.getConnection("jdbc:db2://mikepc:50000/VJDBC", "db2admin", "db2admin");
    }

    protected String getVJdbcDatabaseShortcut() {
        return "DB2DB";
    }
}
