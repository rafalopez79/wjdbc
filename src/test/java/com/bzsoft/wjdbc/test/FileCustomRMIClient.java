package com.bzsoft.wjdbc.test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.bzsoft.wjdbc.WDriver;

public class FileCustomRMIClient {

	private static final List<Double> process(final Driver driver, final int i) throws Exception {
		final Properties prop = new Properties();
		prop.put("user", "pp");
		prop.put("password", "pepa");
		// prop.setProperty(WJdbcProperties.RMI_SSL, "true");
		final Connection conn = driver.connect("jdbc:wjdbc:rmi://127.0.0.1:40000/WJdbc;h2", prop);
		System.out.println("Connected!");
		final Statement stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS TEST" + i);
		stmt.executeUpdate("CREATE TABLE TEST" + i + " (a int, b varchar(50))");
		// System.out.println(conn.isValid(10000));
		// stmt.executeUpdate("DELETE FROM TEST" + i);
		// final PreparedStatement pstmt =
		// conn.prepareStatement("INSERT INTO TEST" + i +
		// " (a, b) VALUES (?, ?)");
		// final Random rand = new Random(System.currentTimeMillis());
		// for (int k = 0; k < 1000; k++) {
		// for (int j = 0; j < 2000; j++) {
		// pstmt.setInt(1, rand.nextInt());
		// pstmt.setString(2, Integer.toString(rand.nextInt()));
		// pstmt.addBatch();
		// }
		// pstmt.executeBatch();
		// }
		final List<Double> list = new ArrayList<Double>();

		final PreparedStatement pstmt2 = conn.prepareStatement("SELECT a, b FROM TEST" + i);
		for (int l = 0; l < 10; l++) {
			final long tini = System.currentTimeMillis();
			final ResultSet rset2 = pstmt2.executeQuery();
			int b = 0;
			while (rset2.next()) {
				final int a = rset2.getInt(1);
				final String c = rset2.getString(2);
				// if (b % 100000 == 0) {
				// System.out.println(b + " rows retrieved.");
				// }
				b++;
			}
			final long tend = System.currentTimeMillis();
			rset2.close();
			list.add((tend - tini) / 1000d);
		}
		pstmt2.close();
		System.out.println("Finishing");
		conn.close();
		return list;
	}

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(final String[] args) throws Exception {
		final Driver driver = new WDriver();
		final int iter = 1;
		final List<Double> times = new ArrayList<Double>(iter);
		for (int i = 0; i < iter; i++) {
			final List<Double> time = process(driver, i);
			times.addAll(time);
		}
		double acc = 0;
		double accsq = 0;
		for (final Double time : times) {
			acc += time;
		}
		final double mean = acc / times.size();
		for (final Double time : times) {
			accsq += Math.pow(time - mean, 2);
		}
		final double sd = Math.sqrt(accsq / (times.size() - 1));
		System.out.println("Mean = " + mean + ", SD=" + sd);
	}
}
