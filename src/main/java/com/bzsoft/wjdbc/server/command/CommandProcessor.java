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

import org.apache.log4j.Logger;

import com.bzsoft.wjdbc.WJdbcException;
import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.ConnectResult;
import com.bzsoft.wjdbc.command.DestroyCommand;
import com.bzsoft.wjdbc.command.StatementCancelCommand;
import com.bzsoft.wjdbc.serial.CallingContext;
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

	private static final Logger					LOGGER	= Logger.getLogger(CommandProcessor.class);
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

	public ConnectResult createConnection(final String url, final Properties props, final Properties clientInfo, final CallingContext ctx)
			throws SQLException {
		final ConnectionConfiguration connectionConfiguration = config.getConnection(url);
		if (connectionConfiguration != null) {
			final Properties propAux = new Properties(clientInfo);
			propAux.putAll(props);
			final boolean b = validate(connectionConfiguration, propAux);
			propAux.clear();
			if (!b) {
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
			final ConnectResult cr = registerConnection(conn, connectionConfiguration, clientInfo, ctx);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Registered " + conn.getClass().getName() + " with UID " + cr.getUid());
			}
			return cr;
		}
		throw new SQLException("Can't find connection configuration for " + url);
	}

	public ConnectionEntry getConnectionEntry(final long connid) {
		return connectionEntries.get(connid);
	}

	public ConnectResult registerConnection(final Connection conn, final ConnectionConfiguration configuration, final Properties clientInfo,
			final CallingContext ctx) {
		final long connid = COUNTER.getAndIncrement();
		final ConnectResult result = new ConnectResult(connid, configuration.isTraceOrphanedObjects());
		connectionEntries.put(connid,
				new ConnectionEntry(connid, conn, configuration, clientInfo, ctx, COUNTER, config.getExecutor(), configuration.getRowPacketSize()));
		return result;
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

	public <R, V> R process(final long connuid, final long uid, final Command<R, V> cmd, final CallingContext ctx) throws SQLException {
		R result = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(cmd);
		}
		final ConnectionEntry connentry = connectionEntries.get(connuid);
		if (connentry != null) {
			try {
				// StatementCancelCommand can be executed asynchronously to
				// terminate a running query
				if (cmd instanceof StatementCancelCommand) {
					connentry.cancelCurrentStatementExecution(connuid, uid, (StatementCancelCommand) cmd);
				} else {
					// All other commands must be executed synchronously which is
					// done by calling the synchronous executeCommand-Method
					result = connentry.executeCommand(uid, cmd, ctx);
				}
			} catch (final SQLException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("SQLException", e);
				}
				throw SQLExceptionHelper.wrap(e);
			} catch (final Throwable e) {
				LOGGER.error(e);
				throw SQLExceptionHelper.wrap(e);
			} finally {
				// When there are no more JDBC objects left in the connection
				// entry (that means even the JDBC-Connection is gone) the
				// connection entry will be immediately destroyed and removed.
				if (!connentry.hasJdbcObjects()) {
					// As remove can be called asynchronously here, we must check the
					// return value.
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
				LOGGER.error(msg);
				throw new SQLException(msg);
			}
		}
		return result;
	}

	/**
	 * The orphaned connection collector task periodically checks the existing
	 * connection entries for orphaned entries that means connections which
	 * weren't used for a specific time and where the client didn't send keep
	 * alive pings.
	 */
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
					// Synchronize here so that the process-Method doesn't
					// access the same entry concurrently
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
				final String msg = "Unexpected Runtime-Exception in OCCT";
				LOGGER.fatal(msg, e);
			}
		}
	}

	private static boolean validate(final ConnectionConfiguration conf, final Properties clientInfo) {
		boolean valid = false;
		try {
			// vjdbc info access
			final String vjdbcUser = conf.getWjdbcUser();
			final String vjdbcPass = conf.getWjdbcPassword();
			// client credentials
			final String clientUser = clientInfo.getProperty("user");
			final String clientPass = clientInfo.getProperty("password");

			valid = vjdbcUser.equals(clientUser) && vjdbcPass.equals(clientPass) ? true : false;

			LOGGER.info("received login request from {user=" + clientInfo.getProperty("user") + ", wjdbc-client.name="
					+ clientInfo.getProperty("wjdbc-client.name") + ", wjdbc-client.address=" + clientInfo.getProperty("wjdbc-client.address")
					+ "} Validation was " + valid);

		} catch (final Exception e) {
			valid = false;
			LOGGER.error("login properties don't found. You can't access the system.");
		}

		return valid;
	}
}
