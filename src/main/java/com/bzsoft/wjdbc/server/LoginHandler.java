// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.server;

import com.bzsoft.wjdbc.WJdbcException;

public interface LoginHandler {

	void checkLogin(String dbId, String user, char[] password) throws WJdbcException;

}
