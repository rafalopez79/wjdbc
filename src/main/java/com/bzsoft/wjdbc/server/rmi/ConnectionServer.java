package com.bzsoft.wjdbc.server.rmi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.bzsoft.wjdbc.rmi.ConnectionBrokerRmi;
import com.bzsoft.wjdbc.rmi.SecureSocketFactory;
import com.bzsoft.wjdbc.rmi.sf.CompressedRMISocketFactory;
import com.bzsoft.wjdbc.server.command.CommandProcessor;
import com.bzsoft.wjdbc.server.config.ConnectionConfiguration;
import com.bzsoft.wjdbc.server.config.RmiConfiguration;
import com.bzsoft.wjdbc.server.config.SharedConnectionPoolConfiguration;
import com.bzsoft.wjdbc.server.config.WJdbcConfiguration;

public class ConnectionServer {

	private static final Logger		LOGGER	= Logger.getLogger(ConnectionServer.class);

	private final WJdbcConfiguration	config;
	private RmiConfiguration rmiConf;
	private Registry registry;
	private CommandProcessor cproc;
	private ConnectionBrokerRmi broker;

	public ConnectionServer(final WJdbcConfiguration config) {
		this.config = config;
	}

	public void serve() throws IOException {
		RmiConfiguration rmiConfig = config.getRmiConfiguration();
		if (rmiConfig == null) {
			LOGGER.debug("No RMI-Configuration specified in WJdbc-Configuration, using default configuration");
			rmiConfig = new RmiConfiguration();
		}
		final RMISocketFactory socketFactory;
		if (rmiConfig.isUseSSL()) {
			LOGGER.info("Using SSL sockets for communication");
			final SecureSocketFactory ssf = new SecureSocketFactory();
			socketFactory = new CompressedRMISocketFactory(ssf, ssf);
		} else {
			socketFactory = new CompressedRMISocketFactory();
		}
		final Registry reg;
		if (rmiConfig.isCreateRegistry()) {
			LOGGER.info("Starting RMI-Registry on port " + rmiConfig.getPort());
			reg = LocateRegistry.createRegistry(rmiConfig.getPort(), socketFactory, socketFactory);
		} else {
			LOGGER.info("Using RMI-Registry on port " + rmiConfig.getPort());
			reg = LocateRegistry.getRegistry(rmiConfig.getPort());
		}
		final Map<String, SharedConnectionPoolConfiguration> sharedConnMap = config.getSharedPoolConfiguration();
		if (!sharedConnMap.isEmpty()) {
			LOGGER.info("Creating shared-pools ...");
			for (final SharedConnectionPoolConfiguration scp : sharedConnMap.values()) {
				final String poolurlid = scp.createSharedConnectionPool();
				if (poolurlid != null) {
					final String id = scp.getId();
					final ConnectionConfiguration cc = config.getConnectionBySharedPoolId(id);
					if (cc != null) {
						cc.setSharedPoolUrl(poolurlid);
					}
				}
			}
		}
		final CommandProcessor commandProcessor = new CommandProcessor(config);
		LOGGER.info("Binding remote object to '" + rmiConfig.getObjectName() + "'");
		final ConnectionBrokerRmi brk = new ConnectionBrokerRmiImpl(commandProcessor, socketFactory, rmiConfig.getRemotingPort());
		reg.rebind(rmiConfig.getObjectName(), brk);
		this.broker = brk;
		this.cproc = commandProcessor;
		this.registry = reg;
		this.rmiConf = rmiConfig;
	}

	public void shutdown(){
		shutdown(rmiConf, registry, cproc, broker);
	}

	private static void shutdown(final RmiConfiguration rmiConf, final Registry registry, final CommandProcessor cp, final ConnectionBrokerRmi broker){
		try {
			LOGGER.info("Unbinding remote object");
			registry.unbind(rmiConf.getObjectName());
		} catch (final RemoteException e) {
			LOGGER.error("Remote exception", e);
		} catch (final NotBoundException e) {
			LOGGER.error("Not bound exception", e);
		}
		try {
			cp.destroy();
		} catch (final Exception e) {
			LOGGER.error("Error destroying command processor", e);
		}
		try {
			broker.shutdown();
			UnicastRemoteObject.unexportObject(broker, true);
		} catch (final Exception e) {
			LOGGER.error("Error destroying broker", e);
		}
		try {
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (final Exception e) {
			LOGGER.error("Error destroying registry", e);
		}
	}

	private static void installShutdownHook(final RmiConfiguration rmiConf, final Registry registry, final CommandProcessor cp, final ConnectionBrokerRmi broker) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				shutdown(rmiConf, registry, cp, broker);
			}
		}));
	}

	public static void main(final String[] args) {
		try {
			final WJdbcConfiguration config;
			if (args.length == 1) {
				config = WJdbcConfiguration.of(args[0]);
			} else if (args.length == 2) {
				// Second argument is a properties file with variables that are
				// replaced by Digester when the configuration is read in
				final Properties props = new Properties();
				InputStream is = null;
				try {
					is = new FileInputStream(args[1]);
					props.load(is);
					config = WJdbcConfiguration.of(args[0], props);
				} finally {
					if (is != null) {
						is.close();
					}
				}
			} else {
				throw new RuntimeException("You must specify a configuration file as the first parameter");
			}
			final ConnectionServer connectionServer = new ConnectionServer(config);
			installShutdownHook(connectionServer.rmiConf, connectionServer.registry, connectionServer.cproc, connectionServer.broker);
			connectionServer.serve();
		} catch (final Throwable e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
