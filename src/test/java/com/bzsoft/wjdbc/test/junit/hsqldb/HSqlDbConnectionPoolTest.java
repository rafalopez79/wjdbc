//VJDBC - Virtual JDBC
//Written by Michael Link
//Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.test.junit.hsqldb;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.DriverManager;

import com.bzsoft.wjdbc.test.junit.VJdbcTest;

public class HSqlDbConnectionPoolTest extends VJdbcTest {
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();

        VJdbcTest.addAllTestMethods(suite, HSqlDbConnectionPoolTest.class);

        TestSetup wrapper = new TestSetup(suite) {
            protected void setUp() throws Exception {
                new HSqlDbConnectionPoolTest("").oneTimeSetup();
            }

            protected void tearDown() throws Exception {
                new HSqlDbConnectionPoolTest("").oneTimeTearDown();
            }
        };

        return wrapper;
    }

    public HSqlDbConnectionPoolTest(String s) {
        super(s);
    }

    protected Connection createNativeDatabaseConnection() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/HSqlDb", "sa", "");
    }

    protected String getVJdbcDatabaseShortcut() {
        return "HSqlDB";
    }
    
    public void testOpenSomeConnectionsFromConnectionPool() throws Exception {
        Connection[] conn = new Connection[15];
        for(int i = 0; i < conn.length; i++) {
            conn[i] = createVJdbcConnection();
        }
        for(int i = 0; i < conn.length; i++) {
            conn[i].close();
        }        
    }
}
