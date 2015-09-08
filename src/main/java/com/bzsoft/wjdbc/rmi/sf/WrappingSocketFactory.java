package com.bzsoft.wjdbc.rmi.sf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;

/**
 * A base class for RMI socket factories which do their work by wrapping the
 * streams of Sockets from another Socket factory.
 *
 * Subclasses have to overwrite the {@link #wrap} method.
 *
 * Instances of this class can be used as both client and server socket
 * factories, or as only one of them.
 */
public abstract class WrappingSocketFactory extends RMISocketFactory implements Serializable {

	/**
	 * A simple holder class for a pair of streams.
	 */
	protected static class StreamPair {
		private final InputStream	input;
		private final OutputStream	output;

		public StreamPair(final InputStream in, final OutputStream out) {
			this.input = in;
			this.output = out;
		}

		public InputStream getInput() {
			return input;
		}

		public OutputStream getOutput() {
			return output;
		}
	}

	/**
	 * The base client socket factory. This will be serialized.
	 */
	private final RMIClientSocketFactory		baseCFactory;

	/**
	 * The base server socket factory. This will not be serialized, since the
	 * server socket factory is used only on the server side.
	 */
	private transient RMIServerSocketFactory	baseSFactory;

	private static final long						serialVersionUID	= 1;

	// --------------------------

	/**
	 * Creates a WrappingSocketFactory based on a pair of socket factories.
	 *
	 * @param cFac
	 *           the base socket factory used for creating client sockets. This
	 *           may be {@code null}, then we will use the default socket
	 *           factory of client system where this object is finally used for
	 *           creating sockets. If not null, it should be serializable.
	 * @param sFac
	 *           the base socket factory used for creating server sockets. This
	 *           may be {@code null}, then we will use the default RMI Socket
	 *           factory. This will not be serialized to the client.
	 */
	public WrappingSocketFactory(final RMIClientSocketFactory cFac, final RMIServerSocketFactory sFac) {
		this.baseCFactory = cFac;
		this.baseSFactory = sFac;
	}

	/**
	 * Creates a WrappingSocketFactory based on a socket factory.
	 *
	 * This constructor is equivalent to {@code WrappingSocketFactory(fac, fac)}.
	 *
	 * @param fac
	 *           the factory to be used as a base for both client and server
	 *           socket. This should be either serializable or {@code null} (then
	 *           we will use the default socket factory as a base).
	 */
	public WrappingSocketFactory(final RMISocketFactory fac) {
		this(fac, fac);
	}

	/**
	 * Creates a WrappingSocketFactory based on the
	 * {@link RMISocketFactory#getSocketFactory global socket factory}.
	 *
	 * This uses the global socket factory at the time of the constructor call.
	 * If this is {@code null}, we will use the
	 * RMISocketFactory.getDefault() instead.
	 */
	public WrappingSocketFactory() {
		this(RMISocketFactory.getSocketFactory());
	}

	/**
	 * Wraps a pair of streams. Subclasses must implement this method to do the
	 * actual work.
	 *
	 * @param input
	 *           the input stream from the base socket.
	 * @param output
	 *           the output stream to the base socket.
	 * @throws IOException
	 * 			 the IOException
	 * @return {@link StreamPair}
	 * 			 the pair with streams
	 */
	protected abstract StreamPair wrap(InputStream input, OutputStream output) throws IOException;

	/**
	 * returns the current base client socket factory. This is either the factory
	 * given to the constructor (if not {@code null}) or the default RMI socket
	 * factory.
	 */
	private RMIClientSocketFactory getClientSocketFactory() {
		if (baseCFactory == null) {
			return RMISocketFactory.getDefaultSocketFactory();
		}
		return baseCFactory;
	}

	/**
	 * returns the current base server socket factory. This is either the factory
	 * given to the constructor (if not {@code null}) or the default RMI socket
	 * factory.
	 */
	private RMIServerSocketFactory getServerSocketFactory() {
		if (baseSFactory == null) {
			return RMISocketFactory.getDefaultSocketFactory();
		}
		return baseSFactory;
	}

	/**
	 * Creates a client socket and connects it to the given host/port pair.
	 *
	 * This retrieves a socket to the host/port from the base client socket
	 * factory and then wraps a new socket (with a custom SocketImpl) around it.
	 *
	 * @param host
	 *           the host we want to be connected with.
	 * @param port
	 *           the port we want to be connected with.
	 * @return a new Socket connected to the host/port pair.
	 * @throws IOException
	 *            if something goes wrong.
	 */
	@Override
	public Socket createSocket(final String host, final int port) throws IOException {
		final Socket baseSocket = getClientSocketFactory().createSocket(host, port);
		final StreamPair streams = wrap(baseSocket.getInputStream(), baseSocket.getOutputStream());
		final SocketImpl wrappingImpl = new WrappingSocketImpl(streams, baseSocket);
		return new Socket(wrappingImpl) {
			@Override
			public boolean isConnected() {
				return true;
			}
		};
	}

	/**
	 * Creates a server socket listening on the given port.
	 *
	 * This retrieves a ServerSocket listening on the given port from the base
	 * server socket factory, and then creates a custom server socket, which on
	 * {@link ServerSocket#accept accept} wraps new Sockets (with a custom
	 * SocketImpl) around the sockets from the base server socket.
	 *
	 * @param port
	 *           the port we want to be connected with.
	 * @return a new Socket connected to the host/port pair.
	 * @throws IOException
	 *            if something goes wrong.
	 */
	@Override
	public ServerSocket createServerSocket(final int port) throws IOException {
		final ServerSocket baseSocket = getServerSocketFactory().createServerSocket(port);
		final ServerSocket ss = new WrappingServerSocket(baseSocket);
		return ss;
	}

	/**
	 * A server socket subclass which wraps our custom sockets around the sockets
	 * retrieves by a base server socket.
	 *
	 * We only override enough methods to work. Basically, this is a unbound
	 * server socket, which handles {@link #accept} specially.
	 */
	private class WrappingServerSocket extends ServerSocket {
		private final ServerSocket	base;

		public WrappingServerSocket(final ServerSocket b) throws IOException {
			this.base = b;
		}

		/**
		 * returns the local port this ServerSocket is bound to.
		 */
		@Override
		public int getLocalPort() {
			return base.getLocalPort();
		}

		/**
		 * accepts a connection from some remote host. This will accept a socket
		 * from the base socket, and then wrap a new custom socket around it.
		 */
		@Override
		public Socket accept() throws IOException {
			final Socket baseSocket = base.accept();
			final StreamPair streams = wrap(baseSocket.getInputStream(), baseSocket.getOutputStream());
			final SocketImpl wrappingImpl = new WrappingSocketImpl(streams, baseSocket);
			final Socket result = new Socket(wrappingImpl) {
				@Override
				public boolean isConnected() {
					return true;
				}

				@Override
				public boolean isBound() {
					return true;
				}

				@Override
				public int getLocalPort() {
					return baseSocket.getLocalPort();
				}

				@Override
				public InetAddress getLocalAddress() {
					return baseSocket.getLocalAddress();
				}
			};
			return result;
		}

		@Override
		public void close() throws IOException {
			super.close();
		}
	}

	/**
	 * A SocketImpl implementation which works on a pair of streams.
	 *
	 * A instance of this class represents an already connected socket, thus all
	 * the methods relating to connecting, accepting and such are not
	 * implemented.
	 *
	 * The implemented methods are {@link #getInputStream},
	 * {@link #getOutputStream}, {@link #available} and the shutdown methods
	 * {@link #close}, {@link #shutdownInput}, {@link #shutdownOutput}.
	 */
	private static class WrappingSocketImpl extends SocketImpl {
		private final InputStream	inStream;
		private final OutputStream	outStream;

		private final Socket			base;

		protected WrappingSocketImpl(final StreamPair pair, final Socket base) {
			this.inStream = pair.input;
			this.outStream = pair.output;
			this.base = base;
		}

		@Override
		protected InputStream getInputStream() {
			return inStream;
		}

		@Override
		protected OutputStream getOutputStream() {
			return outStream;
		}

		@Override
		protected void close() throws IOException {
			base.close();
		}

		@Override
		protected int available() throws IOException {
			return inStream.available();
		}

		@Override
		protected void shutdownInput() throws IOException {
			base.shutdownInput();
			// TODO: inStream.close() ?
		}

		@Override
		protected void shutdownOutput() throws IOException {
			base.shutdownOutput();
			// TODO: outStream.close()?
		}

		@Override
		protected void create(final boolean stream) {
			if (!stream) {
				throw new IllegalArgumentException("datagram socket not supported.");
			}
		}

		@Override
		public Object getOption(final int optID) throws SocketException {
			try {
				switch (optID) {
				case TCP_NODELAY:
					return base.getTcpNoDelay();
				case SO_LINGER:
					return base.getSoLinger();
				case SO_RCVBUF:
					return base.getReceiveBufferSize();
				case SO_SNDBUF:
					return base.getSendBufferSize();
				case SO_TIMEOUT:
					return base.getSoTimeout();
				case SO_REUSEADDR:
					return base.getReuseAddress();
				case SO_KEEPALIVE:
					return base.getKeepAlive();
				default:
					return null;
				}
			} catch (final Exception e) {
				throw new SocketException(e.getMessage());
			}
		}

		@Override
		public void setOption(final int optID, final Object value) throws SocketException {
			try {
				switch (optID) {
				case TCP_NODELAY:
					base.setTcpNoDelay((Boolean) value);
					break;
				case SO_LINGER:
					base.setSoLinger(true, (Integer) value);
					break;
				case SO_RCVBUF:
					base.setReceiveBufferSize((Integer) value);
					break;
				case SO_SNDBUF:
					base.setSendBufferSize((Integer) value);
					break;
				case SO_TIMEOUT:
					base.setSoTimeout((Integer) value);
					break;
				case SO_REUSEADDR:
					base.setReuseAddress((Boolean) value);
					break;
				case SO_KEEPALIVE:
					base.setKeepAlive((Boolean) value);
				default:
					break;
				}
			} catch (final Exception e) {
				throw new SocketException(e.getMessage());
			}
		}

		// unsupported operations:

		@Override
		protected void connect(final String host, final int p) {
			System.err.println("connect(" + host + ", " + p + ")");
			throw new UnsupportedOperationException();
		}

		@Override
		protected void connect(final InetAddress addr, final int p) {
			System.err.println("connect(" + addr + ", " + p + ")");
			throw new UnsupportedOperationException();
		}

		@Override
		protected void connect(final SocketAddress addr, final int timeout) {
			System.err.println("connect(" + addr + ", " + timeout + ")");
			throw new UnsupportedOperationException();
		}

		@Override
		protected void bind(final InetAddress host, final int p) {
			System.err.println("bind(" + host + ", " + p + ")");
			throw new UnsupportedOperationException();
		}

		@Override
		protected void listen(final int backlog) {
			System.err.println("listen(" + backlog + ")");
			throw new UnsupportedOperationException();
		}

		@Override
		protected void accept(final SocketImpl otherSide) {
			System.err.println("accept(" + otherSide + ")");
			throw new UnsupportedOperationException();
		}

		@Override
		protected void sendUrgentData(final int data) {
			System.err.println("sendUrgentData()");
			throw new UnsupportedOperationException();
		}
	}
}
