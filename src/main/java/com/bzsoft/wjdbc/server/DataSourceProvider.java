package com.bzsoft.wjdbc.server;

import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * To use the DataSource-API with WJDBC a class must be provided that implements
 * the <code>DataSourceProvider</code> interface.
 */
public interface DataSourceProvider {
	/**
	 * Retrieves a DataSource object from the DataSourceProvider. This will be
	 * used to create the JDBC connections.
	 *
	 * @return DataSource to be used for creating the connections
	 * @throws SQLException in case of error
	 */
	DataSource getDataSource() throws SQLException;
}
