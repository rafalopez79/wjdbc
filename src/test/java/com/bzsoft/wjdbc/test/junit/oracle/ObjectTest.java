package com.bzsoft.wjdbc.test.junit.oracle;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ObjectTest extends Oracle9iTest {
	public static Test suite() {
		final TestSuite suite = new TestSuite();

		suite.addTest(new ObjectTest("testQueryObjects"));

		final TestSetup wrapper = new TestSetup(suite) {
			@Override
			protected void setUp() throws Exception {
				new ObjectTest("").oneTimeSetup();
			}

			@Override
			protected void tearDown() throws Exception {
				new ObjectTest("").oneTimeTearDown();
			}
		};

		return wrapper;
	}

	public ObjectTest(final String s) {
		super(s);
	}

	@Override
	protected void oneTimeSetup() throws Exception {
		super.oneTimeSetup();

		final Connection connVJdbc = createVJdbcConnection();

		System.out.println("Creating tables ...");
		final Statement stmt = connVJdbc.createStatement();
		dropTables(stmt, new String[] { "course_tbl" });
		dropTypes(stmt, new String[] { "course", "course_sections", "section" });
		System.out.println("... create type section");
		stmt.executeUpdate("create or replace type section as object " + "(section_no char(3), " + "professor char(20), " + "current_enrol number(3))");
		System.out.println("... create type course_sections");
		stmt.executeUpdate("create or replace type course_sections as table of section");
		System.out.println("... create type course");
		stmt.executeUpdate("create or replace type course as object " + "(course_no char(8)," + "course_title char(30)," + "sections course_sections)");
		System.out.println("... create table course_tbl");
		stmt.executeUpdate("create table course_tbl of course (primary key (course_no)) nested table sections store as sections_tbl");

		System.out.println("... insert courses");
		stmt.executeUpdate("insert into course_tbl values ("
				+ "'DAT2210', 'Computer Fundamentals', course_sections(section('010', 'Brown', 15), section('020', 'White', 20)))");
		stmt.executeUpdate("insert into course_tbl values ("
				+ "'DAT2219', 'Computer Programming I', course_sections(section('010', 'Black', 30), section('020', 'Green', 25), section('030', 'Grey', 33)))");

		stmt.close();
		connVJdbc.close();
	}

	public void testQueryObjects() throws Exception {
		Statement stmtOrig = null;
		Statement stmtVJdbc = null;
		try {
			stmtOrig = _connOther.createStatement();
			stmtVJdbc = _connVJdbc.createStatement();

			final ResultSet rsOrig = stmtOrig.executeQuery("select * from course_tbl");
			final ResultSet rsVJdbc = stmtVJdbc.executeQuery("select * from course_tbl");

			assertTrue(flattenCourseResultSet(rsOrig).equals(flattenCourseResultSet(rsVJdbc)));
		} finally {
			closeStatement(stmtOrig);
			closeStatement(stmtVJdbc);
		}
	}

	private ArrayList flattenCourseResultSet(final ResultSet rs) throws SQLException {
		final ArrayList objlist = new ArrayList();
		while (rs.next()) {
			objlist.add(rs.getString(1));
			objlist.add(rs.getString(2));
			final Array arr = rs.getArray(3);
			final Object[] vals = (Object[]) arr.getArray();
			for (final Object val : vals) {
				final Struct str = (Struct) val;
				objlist.add(str.getSQLTypeName());
				final Object[] atts = str.getAttributes();
				for (final Object att : atts) {
					objlist.add(att);
				}
			}
		}
		return objlist;
	}
}
