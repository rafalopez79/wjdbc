package com.bzsoft.wjdbc.rmi;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;

/**
 * Modified from JDK
 * 
 * @author Rafa
 */
public final class Naming {

	private Naming() {
		// empty
	}

	public static Remote lookup(final String name, final RMIClientSocketFactory csf) throws NotBoundException, MalformedURLException, RemoteException {
		final ParsedNamingURL parsed = parseURL(name);
		final Registry registry = getRegistry(parsed, csf);
		if (parsed.name == null) {
			return registry;
		}
		return registry.lookup(parsed.name);
	}

	public static void bind(final String name, final Remote obj, final RMIClientSocketFactory csf) throws AlreadyBoundException,
			MalformedURLException, RemoteException {
		final ParsedNamingURL parsed = parseURL(name);
		final Registry registry = getRegistry(parsed, csf);
		if (obj == null) {
			throw new NullPointerException("cannot bind to null");
		}
		registry.bind(parsed.name, obj);
	}

	public static void unbind(final String name, final RMIClientSocketFactory csf) throws RemoteException, NotBoundException, MalformedURLException {
		final ParsedNamingURL parsed = parseURL(name);
		final Registry registry = getRegistry(parsed, csf);

		registry.unbind(parsed.name);
	}

	public static void rebind(final String name, final Remote obj, final RMIClientSocketFactory csf) throws RemoteException, MalformedURLException {
		final ParsedNamingURL parsed = parseURL(name);
		final Registry registry = getRegistry(parsed, csf);
		if (obj == null) {
			throw new NullPointerException("cannot bind to null");
		}
		registry.rebind(parsed.name, obj);
	}

	public static String[] list(final String name, final RMIClientSocketFactory csf) throws RemoteException, MalformedURLException {
		final ParsedNamingURL parsed = parseURL(name);
		final Registry registry = getRegistry(parsed, csf);
		String prefix = "";
		if (parsed.port > 0 || !parsed.host.equals("")) {
			prefix += "//" + parsed.host;
		}
		if (parsed.port > 0) {
			prefix += ":" + parsed.port;
		}
		prefix += "/";
		final String[] names = registry.list();
		for (int i = 0; i < names.length; i++) {
			names[i] = prefix + names[i];
		}
		return names;
	}

	/**
	 * Returns a registry reference obtained from information in the URL.
	 */
	private static Registry getRegistry(final ParsedNamingURL parsed, final RMIClientSocketFactory csf) throws RemoteException {
		return LocateRegistry.getRegistry(parsed.host, parsed.port, csf);
	}

	/**
	 * Dissect Naming URL strings to obtain referenced host, port and object
	 * name.
	 * 
	 * @return an object which contains each of the above components.
	 * 
	 * @exception MalformedURLException
	 *               if given url string is malformed
	 */
	private static ParsedNamingURL parseURL(final String str) throws MalformedURLException {
		try {
			return intParseURL(str);
		} catch (final URISyntaxException ex) {
			/*
			 * With RFC 3986 URI handling, 'rmi://:<port>' and '//:<port>' forms
			 * will result in a URI syntax exception Convert the authority to a
			 * localhost:<port> form
			 */
			final MalformedURLException mue = new MalformedURLException("invalid URL String: " + str);
			mue.initCause(ex);
			final int indexSchemeEnd = str.indexOf(':');
			final int indexAuthorityBegin = str.indexOf("//:");
			if (indexAuthorityBegin < 0) {
				throw mue;
			}
			if (indexAuthorityBegin == 0 || indexSchemeEnd > 0 && indexAuthorityBegin == indexSchemeEnd + 1) {
				final int indexHostBegin = indexAuthorityBegin + 2;
				final String newStr = str.substring(0, indexHostBegin) + "localhost" + str.substring(indexHostBegin);
				try {
					return intParseURL(newStr);
				} catch (final URISyntaxException inte) {
					throw mue;
				} catch (final MalformedURLException inte) {
					throw inte;
				}
			}
			throw mue;
		}
	}

	private static ParsedNamingURL intParseURL(final String str) throws MalformedURLException, URISyntaxException {
		URI uri = new URI(str);
		if (uri.isOpaque()) {
			throw new MalformedURLException("not a hierarchical URL: " + str);
		}
		if (uri.getFragment() != null) {
			throw new MalformedURLException("invalid character, '#', in URL name: " + str);
		} else if (uri.getQuery() != null) {
			throw new MalformedURLException("invalid character, '?', in URL name: " + str);
		} else if (uri.getUserInfo() != null) {
			throw new MalformedURLException("invalid character, '@', in URL host: " + str);
		}
		final String scheme = uri.getScheme();
		if (scheme != null && !scheme.equals("rmi")) {
			throw new MalformedURLException("invalid URL scheme: " + str);
		}

		String name = uri.getPath();
		if (name != null) {
			if (name.startsWith("/")) {
				name = name.substring(1);
			}
			if (name.length() == 0) {
				name = null;
			}
		}

		String host = uri.getHost();
		if (host == null) {
			host = "";
			try {
				/*
				 * With 2396 URI handling, forms such as 'rmi://host:bar' or
				 * 'rmi://:<port>' are parsed into a registry based authority. We
				 * only want to allow server based naming authorities.
				 */
				uri.parseServerAuthority();
			} catch (final URISyntaxException use) {
				// Check if the authority is of form ':<port>'
				String authority = uri.getAuthority();
				if (authority != null && authority.startsWith(":")) {
					// Convert the authority to 'localhost:<port>' form
					authority = "localhost" + authority;
					try {
						uri = new URI(null, authority, null, null, null);
						// Make sure it now parses to a valid server based
						// naming authority
						uri.parseServerAuthority();
					} catch (final URISyntaxException use2) {
						throw new MalformedURLException("invalid authority: " + str);
					}
				} else {
					throw new MalformedURLException("invalid authority: " + str);
				}
			}
		}
		int port = uri.getPort();
		if (port == -1) {
			port = Registry.REGISTRY_PORT;
		}
		return new ParsedNamingURL(host, port, name);
	}

	/**
	 * Simple class to enable multiple URL return values.
	 */
	private static class ParsedNamingURL {
		final String	host;
		final int		port;
		final String	name;

		ParsedNamingURL(final String host, final int port, final String name) {
			this.host = host;
			this.port = port;
			this.name = name;
		}
	}
}
