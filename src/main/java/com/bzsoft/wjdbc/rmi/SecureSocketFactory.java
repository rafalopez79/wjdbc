// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class SecureSocketFactory extends RMISocketFactory implements Serializable {

	private static final long					serialVersionUID	= -2918614178042234949L;

	private final RMIClientSocketFactory	sslSocketFactory;
	private final RMIServerSocketFactory	sslServerSocketFactory;

	public SecureSocketFactory() {
		sslSocketFactory = new SslRMIClientSocketFactory();
		sslServerSocketFactory = new SslRMIServerSocketFactory(new String[] { "TLS_RSA_WITH_AES_128_CBC_SHA" }, null, false);
	}

	@Override
	public Socket createSocket(final String host, final int port) throws IOException {
		final Socket sock = sslSocketFactory.createSocket(host, port);
		final SSLSocket sslSocket = (SSLSocket) sock;
		sslSocket.setEnabledCipherSuites(new String[] { "TLS_RSA_WITH_AES_128_CBC_SHA" });
		return sock;
	}

	@Override
	public ServerSocket createServerSocket(final int port) throws IOException {
		final ServerSocket sock = sslServerSocketFactory.createServerSocket(port);
		return sock;
	}

	public static void main(final String[] args) throws NoSuchAlgorithmException {
		final SSLServerSocketFactory fact = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		fact.getDefaultCipherSuites();
		final SSLContext c = SSLContext.getDefault();
		System.out.println(c.getProtocol());
	}
}
