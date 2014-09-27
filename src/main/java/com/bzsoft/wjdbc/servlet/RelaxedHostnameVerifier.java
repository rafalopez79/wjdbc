// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.servlet;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class RelaxedHostnameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(final String s, final SSLSession sslSession) {
		return true;
	}
}
