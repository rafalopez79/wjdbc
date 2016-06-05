package com.bzsoft.wjdbc.server.command;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bzsoft.wjdbc.WJdbcException;
import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.DestroyCommand;
import com.bzsoft.wjdbc.command.StatementCancelCommand;
import com.bzsoft.wjdbc.server.concurrent.Executor;
import com.bzsoft.wjdbc.server.config.ConnectionConfiguration;
import com.bzsoft.wjdbc.server.config.OcctConfiguration;
import com.bzsoft.wjdbc.server.config.WJdbcConfiguration;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;

/**
 * The CommandProcessor is a singleton class which dispatches calls from the
 * client to the responsible connection object.
 */
public class CommandProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommandProcessor.class);
	private static final AtomicLong				COUNTER;

	static {
		final Random rand = new Random(System.currentTimeMillis());
		COUNTER = new AtomicLong(rand.nextLong());
		LOGGER.info("Starting server with ID " + COUNTER.get());
	}

	private final Map<Long, ConnectionEntry>	connectionEntries;
	private final WJdbcConfiguration				config;

	public CommandProcessor(final WJdbcConfiguration config) {
		this.config = config;
		connectionEntries = new ConcurrentHashMap<Long, ConnectionEntry>();
		final OcctConfiguration occtConfig = config.getOcctConfiguration();
		if (occtConfig.getCheckingPeriodInMillis() > 0) {
			LOGGER.debug("OCCT starts");
			final Executor executor = config.getExecutor();
			executor.schedule(new OrphanedConnectionCollector(), occtConfig.getCheckingPeriodInMillis(), occtConfig.getCheckingPeriodInMillis(),
					TimeUnit.MILLISECONDS);
		} else {
			LOGGER.info("OCCT is turned off");
		}
	}

	public long createConnection(final String url, final Properties props, final Properties clientInfo) throws SQLException {
		final ConnectionConfiguration connectionConfiguration = config.getConnection(url);
		if (connectionConfiguration != null) {
			final Properties auxProperties = new Properties(clientInfo);
			auxProperties.putAll(props);
			final boolean valid = validate(connectionConfiguration, auxProperties);
			auxProperties.clear();
			if (!valid) {
				throw new SQLException("Use a valid user/password, please.");
			}
			LOGGER.debug("Found connection configuration " + connectionConfiguration.getId());
			Connection conn;
			try {
				conn = connectionConfiguration.create(props);
			} catch (final WJdbcException e) {
				throw SQLExceptionHelper.wrap(e);
			}
			LOGGER.debug("Created connection, registering it now ...");
			final long connuid = registerConnection(conn, connectionConfiguration, clientInfo);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Registered " + conn.getClass().getName() + " with UID " + connuid);
			}
			return connuid;
		}
		throw new SQLException("Can't find connection configuration for " + url);
	}

	public ConnectionEntry getConnectionEntry(final long connid) {
		return connectionEntries.get(connid);
	}

	public long registerConnection(final Connection conn, final ConnectionConfiguration configuration, final Properties clientInfo) {
		final long connid = COUNTER.getAndIncrement();
		connectionEntries.put(connid,
				new ConnectionEntry(connid, conn, configuration, clientInfo, COUNTER, config.getExecutor(), configuration.getRowPacketSize()));
		return connid;
	}

	public void destroy() {
		LOGGER.info("Destroying CommandProcessor ...");
		config.getExecutor().close();
		final List<ConnectionEntry> entries = new ArrayList<ConnectionEntry>(connectionEntries.values());
		connectionEntries.clear();
		for (final ConnectionEntry connectionEntry : entries) {
			connectionEntry.close();
		}
		LOGGER.info("CommandProcessor successfully destroyed");
	}

	public <R, V> R process(final long connuid, final long uid, final Command<R, V> cmd) throws SQLException {
		R result = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Command {}",cmd);
		}
		final ConnectionEntry connentry = connectionEntries.get(connuid);
		if (connentry != null) {
			try {
				if (cmd instanceof StatementCancelCommand) {
					connentry.cancelCurrentStatementExecution(uid, (StatementCancelCommand) cmd);
				} else {
					result = connentry.executeCommand(uid, cmd);
				}
			} catch (final SQLException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("SQLException", e);
				}
				throw SQLExceptionHelper.wrap(e);
			} catch (final Throwable e) {
				LOGGER.error("Error {}",e);
				throw SQLExceptionHelper.wrap(e);
			} finally {
				if (!connentry.hasJdbcObjects()) {
					if (connectionEntries.remove(connuid) != null) {
						LOGGER.info("Connection " + connuid + " closed, statistics:");
						connentry.traceConnectionStatistics();
					}
				}
			}
		} else {
			if (cmd instanceof DestroyCommand) {
				LOGGER.debug("Connection entry already gone, DestroyCommand will be ignored");
			} else {
				final String msg = "Unknown connection entry " + connuid + " for command " + cmd.toString();
				LOGGER.error("Unknown connection entry " + connuid + " for command " + cmd.toString());
				throw new SQLException(msg);
			}
		}
		return result;
	}

	private class OrphanedConnectionCollector implements Runnable {
		@Override
		public void run() {
			try {
				LOGGER.debug("Checking for orphaned connections ...");
				final OcctConfiguration occtConfig = config.getOcctConfiguration();
				final long millis = System.currentTimeMillis();
				final Set<Entry<Long, ConnectionEntry>> set = connectionEntries.entrySet();
				for (final Entry<Long, ConnectionEntry> entry : set) {
					final Long key = entry.getKey();
					final ConnectionEntry connentry = entry.getValue();
					final long idleTime = millis - connentry.getLastAccess();
					if (!connentry.isActive() && idleTime > occtConfig.getTimeoutInMillis()) {
						LOGGER.info("Closing orphaned connection " + key + " after being idle for about " + idleTime / 1000 + "sec");
						connentry.close();
						connectionEntries.remove(key);
					}
				}
			} catch (final RuntimeException e) {
				// Any other error will be propagated so that the timer task is
				// stopped
				LOGGER.error("Unexpected Runtime-Exception in OCCT", e);
			}
		}
	}

	private static boolean validate(final ConnectionConfiguration conf, final Properties clientInfo) {
		boolean valid = false;
		try {
			// wjdbc info access
			final String wjdbcUser = conf.getWjdbcUser();
			final String wjdbcPass = conf.getWjdbcPassword();
			// client credentials
			final String clientUser = clientInfo.getProperty("user");
			final String clientPass = clientInfo.getProperty("password");
			valid = wjdbcUser.equals(clientUser) && wjdbcPass.equals(clientPass) ? true : false;
			LOGGER.info("Received login request from {user=" + clientInfo.getProperty("user") + ", wjdbc-client.name="
					+ clientInfo.getProperty("wjdbc-client.name") + ", wjdbc-client.address=" + clientInfo.getProperty("wjdbc-client.address")
					+ "} Validation was " + valid);
		} catch (final Exception e) {
			valid = false;
			LOGGER.error("Login properties don't found. You can't access the system.");
		}
		return valid;
	}
}
