package com.bzsoft.wjdbc;

public final class WJdbcProperties {

	private WJdbcProperties() {
		// empty
	}

	// System properties to transfer to the server when opening a connection
	public static final String	CLIENTINFO_PROPERTIES				= "wjdbc.clientinfo.properties";
	// Login-Handler-Class which authenticates the user
	public static final String	LOGIN_USER								= "wjdbc.login.user";
	public static final String	LOGIN_PASSWORD							= "wjdbc.login.password";
	// Signaling using of SSL sockets for RMI communication (true or false,
	// default: false)
	public static final String	RMI_SSL									= "wjdbc.rmi.ssl";
	// Flag that signals usage of Jakarta HTTP-Client instead of the default
	// implementation
	public static final String	SERVLET_USE_JAKARTA_HTTP_CLIENT	= "wjdbc.servlet.use_jakarta_http_client";
	// Factory class that create Servlet-Request enhancers which can put
	// additional Request-Properties in HTTP-Requests
	public static final String	SERVLET_REQUEST_ENHANCER_FACTORY	= "wjdbc.servlet.request_enhancer_factory";
}
