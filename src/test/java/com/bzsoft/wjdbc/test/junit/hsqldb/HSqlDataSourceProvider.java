// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.test.junit.hsqldb;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.bzsoft.wjdbc.server.DataSourceProvider;

public class HSqlDataSourceProvider implements DataSourceProvider {

	@Override
	public DataSource getDataSource() throws SQLException {
		return new HSqlDataSource();
	}
}
