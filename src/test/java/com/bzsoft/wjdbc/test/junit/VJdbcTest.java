package com.bzsoft.wjdbc.test.junit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.bzsoft.wjdbc.WDriver;
import com.bzsoft.wjdbc.WJdbcProperties;
import com.bzsoft.wjdbc.serial.StreamSerializer;

public abstract class VJdbcTest extends TestCase {
	protected Connection	_connOther;
	protected Connection	_connVJdbc;

	protected DateFormat	_dateTimeFormat	= DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

	public VJdbcTest(final String s) {
		super(s);
	}

	protected void oneTimeSetup() throws Exception {
		DriverManager.registerDriver(new WDriver());
	}

	protected void oneTimeTearDown() throws Exception {
	}

	@Override
	protected void setUp() throws Exception {
		_connOther = createNativeDatabaseConnection();
		_connOther.setAutoCommit(true);
		_connVJdbc = createVJdbcConnection();
	}

	@Override
	protected void tearDown() throws Exception {
		_connOther.close();
		_connVJdbc.close();
	}

	protected Connection createVJdbcConnection() throws SQLException {
		final String connectionUrl = System.getProperty("VJDBC_CONNECTION_URL");
		if (connectionUrl == null) {
			throw new SQLException("System-Property VJDBC_CONNECTION_URL not set");
		}
		final Connection conn = DriverManager.getConnection(connectionUrl + "#" + getVJdbcDatabaseShortcut(), getVJdbcProperties());
		// Connection conn = DriverManager.getConnection(connectionUrl, "root",
		// "");
		conn.setAutoCommit(true);
		return conn;
	}

	protected abstract Connection createNativeDatabaseConnection() throws Exception;

	protected abstract String getVJdbcDatabaseShortcut();

	protected Properties getVJdbcProperties() {
		final Properties props = new Properties();
		props.setProperty(WJdbcProperties.CLIENTINFO_PROPERTIES, "user.name;java.version;os.name");

		final Properties systemProps = System.getProperties();
		for (final Enumeration e = systemProps.keys(); e.hasMoreElements();) {
			final String keyName = (String) e.nextElement();
			if (keyName.startsWith("vjdbc")) {
				props.setProperty(keyName, systemProps.getProperty(keyName));
			}
		}
		return props;
	}

	protected void dropTables(final Statement stmt, final String[] tables) {
		for (final String table : tables) {
			System.out.println("... drop table " + table);
			try {
				stmt.executeUpdate("drop table " + table);
			} catch (final SQLException e) {
				System.out.println("... doesn't exist");
			}
		}
	}

	protected void compareResultSets(final ResultSet rs1, final ResultSet rs2) throws Exception {
		final ResultSetMetaData rsmeta1 = rs1.getMetaData();
		final ResultSetMetaData rsmeta2 = rs2.getMetaData();

		final int columnCount1 = rsmeta1.getColumnCount();
		final int columnCount2 = rsmeta2.getColumnCount();
		assertEquals(columnCount1, columnCount2);

		for (int i = 1; i <= columnCount1; i++) {
			assertEquals(rsmeta1.getColumnName(i), rsmeta2.getColumnName(i));
			assertEquals(rsmeta1.getColumnLabel(i), rsmeta2.getColumnLabel(i));
			if (rsmeta1.getColumnType(i) != Types.STRUCT) {
				assertEquals(rsmeta1.getColumnType(i), rsmeta2.getColumnType(i));
			}
		}

		boolean doLoop = true;

		while (doLoop) {
			final boolean nextRow1 = rs1.next();
			final boolean nextRow2 = rs2.next();

			assertEquals(nextRow1, nextRow2);

			if (nextRow1) {
				// First pass with column indizes
				for (int i = 1; i <= rsmeta1.getColumnCount(); i++) {
					final Object obj1 = rs1.getObject(i);
					final Object obj2 = rs2.getObject(i);

					if (obj1 != null || obj2 != null) {
						if (obj1 == null) {
							fail("ResultSets not equal");
						}
						if (obj2 == null) {
							fail("ResultSets not equal");
						}

						final int columnType = rsmeta1.getColumnType(i);
						switch (columnType) {
						case Types.BINARY:
							final byte[] barr1 = rs1.getBytes(i);
							final byte[] barr2 = rs2.getBytes(i);
							assertEquals(barr1.length, barr2.length);
							for (int idx = 0; idx < barr1.length; ++idx) {
								assertEquals(barr1[idx], barr2[idx]);
							}
							break;
						case Types.CHAR:
						case Types.VARCHAR:
						case Types.LONGVARCHAR:
							String str1 = rs1.getString(i);
							String str2 = rs2.getString(i);
							assertEquals(str1, str2);
							assertTrue(Arrays.equals(StreamSerializer.toCharArray(rs1.getCharacterStream(i)),
									StreamSerializer.toCharArray(rs2.getCharacterStream(i))));
							break;
						case Types.NCHAR:
						case Types.NVARCHAR:
						case Types.LONGNVARCHAR:
							str1 = rs1.getNString(i);
							str2 = rs2.getNString(i);
							assertEquals(str1, str2);
							assertTrue(Arrays.equals(StreamSerializer.toCharArray(rs1.getNCharacterStream(i)),
									StreamSerializer.toCharArray(rs2.getNCharacterStream(i))));
							break;
						case Types.ARRAY:
							final Object[] arr1 = (Object[]) rs1.getArray(i).getArray();
							final Object[] arr2 = (Object[]) rs2.getArray(i).getArray();
							if (arr1.length == arr2.length) {
								for (int j = 0; j < arr1.length; j++) {
									// Oracle-specific: skip equality check if the nested
									// element is a SQL-Struct
									if (!Struct.class.isAssignableFrom(arr1[j].getClass())) {
										assertEquals(arr1[j], arr2[j]);
									}
								}
							} else {
								fail("Array-Size not equal !");
							}
							break;
						case Types.NUMERIC:
						case Types.DECIMAL:
							assertEquals(rs1.getBigDecimal(i), rs2.getBigDecimal(i));
							break;
						case Types.DATE:
							final Date date1 = getCleanDate(rs1.getDate(i).getTime());
							final Date date2 = rs2.getDate(i);
							if (!date1.equals(date2)) {
								final String msg = "Date1 " + _dateTimeFormat.format(date1) + " != Date2 " + _dateTimeFormat.format(date2);
								fail(msg);
							}
							break;
						case Types.TIME:
							final Time time1 = getCleanTime(rs1.getTime(i).getTime());
							// VJDBC already returns a clear time
							final Time time2 = rs2.getTime(i);
							if (!time1.equals(time2)) {
								String msg = "Time1 " + _dateTimeFormat.format(time1) + " != Time2 " + _dateTimeFormat.format(time2);
								msg += "(" + time1.getTime() + " != " + time2.getTime() + ")";
								fail(msg);
							}
							break;
						case Types.TIMESTAMP:
							final Timestamp ts1 = rs1.getTimestamp(i);
							final Timestamp ts2 = rs2.getTimestamp(i);
							if (!ts1.equals(ts2)) {
								final String msg = "Timestamp1 " + _dateTimeFormat.format(ts1) + " != Timestamp2 " + _dateTimeFormat.format(ts2);
								fail(msg);
							}
							break;
						case Types.BLOB:
							final Blob blob1 = rs1.getBlob(i);
							final Blob blob2 = rs2.getBlob(i);
							assertEquals(blob1.length(), blob2.length());
							assertTrue(Arrays.equals(blob1.getBytes(1, (int) blob1.length()), blob2.getBytes(1, (int) blob2.length())));
							break;
						case Types.CLOB:
							final Clob clob1 = rs1.getClob(i);
							final Clob clob2 = rs2.getClob(i);
							assertTrue(Arrays.equals(StreamSerializer.toCharArray(clob1.getCharacterStream(), (int) clob1.length()),
									StreamSerializer.toCharArray(clob2.getCharacterStream(), (int) clob2.length())));
							assertEquals(clob1.getSubString(1, (int) clob1.length()), clob2.getSubString(1, (int) clob2.length()));
							break;
						case Types.NCLOB:
							final NClob nclob1 = rs1.getNClob(i);
							final NClob nclob2 = rs2.getNClob(i);
							assertTrue(Arrays.equals(StreamSerializer.toCharArray(nclob1.getCharacterStream(), (int) nclob1.length()),
									StreamSerializer.toCharArray(nclob2.getCharacterStream(), (int) nclob2.length())));
							assertEquals(nclob1.getSubString(1, (int) nclob1.length()), nclob2.getSubString(1, (int) nclob2.length()));
							break;
						case Types.SQLXML:
							final SQLXML xml1 = rs1.getSQLXML(i);
							final SQLXML xml2 = rs2.getSQLXML(i);
							assertEquals(xml1.getString(), xml2.getString());
							break;
						default:
							if (!obj1.equals(obj2)) {
								fail("ResultSets not equal: " + obj1.toString() + " != " + obj2.toString());
							}
						}
					}
				}
			} else {
				doLoop = false;
			}
		}

		rs1.close();
		rs2.close();
	}

	protected Date getCleanDate(final long millis) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	protected Time getCleanTime(final long millis) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);
		return new Time(cal.getTimeInMillis());
	}

	protected void closeStatement(final Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (final SQLException e) {
			}
		}
	}

	public void testConnection() throws Exception {
		assertEquals(_connOther.getAutoCommit(), _connVJdbc.getAutoCommit());
		assertEquals(_connOther.getCatalog(), _connVJdbc.getCatalog());
	}

	public void testDatabaseMetaData() throws Exception {
		final DatabaseMetaData metaOra = _connOther.getMetaData();
		final DatabaseMetaData metaVJdbc = _connVJdbc.getMetaData();

		compareResultSets(metaOra.getCatalogs(), metaVJdbc.getCatalogs());
		compareResultSets(metaOra.getSchemas(), metaVJdbc.getSchemas());
		compareResultSets(metaOra.getTables(null, "VJDBC", null, null), metaVJdbc.getTables(null, "VJDBC", null, null));

		final Statement stmtOra = _connOther.createStatement();
		final Statement stmtVJdbc = _connVJdbc.createStatement();

		final ResultSet tables = metaVJdbc.getTables(null, "VJDBC", null, null);
		while (tables.next()) {
			final String tablename = tables.getString("TABLE_NAME");
			if (!tablename.equalsIgnoreCase("supplier_tbl") && !tablename.equalsIgnoreCase("SECTIONS_TBL")) {
				System.out.println("... querying table " + tablename);
				final ResultSet rsOra = stmtOra.executeQuery("select * from " + tablename);
				final ResultSet rsVJdbc = stmtVJdbc.executeQuery("select * from " + tablename);
				compareResultSets(rsOra, rsVJdbc);
			}
		}
		tables.close();

		stmtOra.close();
		stmtVJdbc.close();
	}

	protected static void addAllTestMethods(final TestSuite suite, final Class clazz) throws Exception {
		// First search for an fitting constructor
		final Constructor ctor = clazz.getConstructor(new Class[] { String.class });

		final Method[] methods = clazz.getMethods();
		for (final Method method : methods) {
			if (method.getName().startsWith("test") && method.getParameterTypes().length == 0) {
				suite.addTest((Test) ctor.newInstance(new Object[] { method.getName() }));
			}
		}
	}
}
