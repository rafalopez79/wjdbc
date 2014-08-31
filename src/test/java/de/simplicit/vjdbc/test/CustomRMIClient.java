package de.simplicit.vjdbc.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.bzsoft.wjdbc.WDriver;

public class CustomRMIClient {

    private static final void process(final Driver driver, final int i) throws Exception {
        final Properties prop = new Properties();
        prop.put("user", "distribucion");
        prop.put("password", "raHgTINI4FWo");
        // prop.setProperty(WJdbcProperties.RMI_SSL, "true");
        // final Connection conn = driver.connect("jdbc:wjdbc:rmi://127.0.0.1:20000/wjdbc;H2", prop);
        final Connection conn = driver.connect("jdbc:wjdbc:rmi://10.1.5.222:2668/WJdbc;desdistribucionrw", prop);
        System.out.println("Connected!");
        final DatabaseMetaData dbMd = conn.getMetaData();
        final Statement stmt = conn.createStatement();
        // stmt.executeUpdate("DROP TABLE IF EXISTS TEST" + i);
        // stmt.executeUpdate("CREATE TABLE TEST" + i + " (a int, b CLOB)");// b
        // System.out.println(conn.isValid(10000));
        // stmt.executeUpdate("DELETE FROM TEST" + i);
        // final PreparedStatement pstmt =
        // conn.prepareStatement("INSERT INTO TEST" + i +
        // " (a, b) VALUES (?, ?)");
        // int sum = 0;
        // for (int j = 0; j < 20000; j++) {
        // pstmt.setInt(1, j);
        // // pstmt.setString(2, createString(j));
        // pstmt.setBlob(2, createInputStream(j));
        // if (j % 1000 == 0) {
        // System.out.println(j + " rows inserted");
        // }
        // pstmt.executeUpdate();
        // sum++;
        // }
        // final int[] res = pstmt.executeBatch();
        // System.out.println(sum + " updated");
        // stmt.close();
        // pstmt.close();
        final PreparedStatement pstmt2 = conn.prepareStatement("SELECT a, b FROM TEST" + i);
        final ResultSet rset2 = pstmt2.executeQuery();
        final PreparedStatement pstmt3 = conn.prepareStatement("SELECT a, b FROM TEST" + i);
        final ResultSet rset3 = pstmt3.executeQuery();
        System.out.println("Looping");
        int b = 0;
        while (rset2.next()) {
            final int a = rset2.getInt(1);
            if (b % 1000 == 0) {
                System.out.println(a + " rows retrieved.");
            }
            b++;
        }
        b = 0;
        while (rset3.next()) {
            final int a = rset3.getInt(1);
            if (b % 1000 == 0) {
                System.out.println(a + " rows retrieved.");
            }
            b++;
        }
        System.out.println("Finishing");
        rset2.close();
        pstmt2.close();
        rset3.close();
        pstmt3.close();
        conn.close();
    }

    private static String createString(final int j) {
        final StringBuilder sb = new StringBuilder(j + "-");
        for (int i = 0; i < 512; i++) {
            sb.append(i);
        }
        return sb.toString();
    }

    private static InputStream createInputStream(final int j) {
        final ByteArrayOutputStream sb = new ByteArrayOutputStream(1024);
        for (int i = 0; i < 5120; i++) {
            sb.write(i);
        }
        return new ByteArrayInputStream(sb.toByteArray());
    }

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(final String[] args) throws Exception {
        final Driver driver = new WDriver();
        final List<Thread> list = new ArrayList<Thread>();
        for (int i = 0; i < 1; i++) {
            final int j = i;
            final Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        process(driver, j);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            list.add(t);
            t.start();
        }
        for (final Thread t : list) {
            t.join();
        }
        System.out.println("End");
    }
}
