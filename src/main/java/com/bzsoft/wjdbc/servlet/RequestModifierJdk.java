// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.servlet;

import java.net.URLConnection;

/**
 * The HttpRequestModifier lets an external entity partly change the
 * Http-Request that is made by VJDBC in Servlet-Mode. To prevent abuse actually
 * only one method is delegated to the URLConnection.
 * 
 * @author Mike
 * 
 */
final class RequestModifierJdk implements RequestModifier {

	private final URLConnection	urlConnection;

	/**
	 * Package visibility, doesn't make sense for other packages.
	 * 
	 * @param urlConnection
	 *           Wrapped URLConnection
	 */
	RequestModifierJdk(final URLConnection urlConnection) {
		this.urlConnection = urlConnection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bzsoft.wjdbc.servlet.RequestModifier#addRequestProperty(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public void addRequestHeader(final String key, final String value) {
		urlConnection.addRequestProperty(key, value);
	}
}
