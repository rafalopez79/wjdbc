package com.bzsoft.wjdbc.servlet.jakarta;

import org.apache.commons.httpclient.methods.PostMethod;

import com.bzsoft.wjdbc.servlet.RequestModifier;

/**
 * The RequestModifierHttpClient lets an external entity partly change the
 * Http-Request that is made by WJDBC in Servlet-Mode. To prevent abuse actually
 * only one method is delegated to the URLConnection.
 *
 * @author Mike
 *
 */
final class RequestModifierJakartaHttpClient implements RequestModifier {

	private final PostMethod	postMethod;

	/**
	 * Package visibility, doesn't make sense for other packages.
	 *
	 * @param urlConnection
	 *           Wrapped URLConnection
	 */
	RequestModifierJakartaHttpClient(final PostMethod postMethod) {
		this.postMethod = postMethod;
	}

	@Override
	public void addRequestHeader(final String key, final String value) {
		postMethod.addRequestHeader(key, value);
	}
}
