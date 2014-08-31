// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package com.bzsoft.wjdbc.server.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

public class QueryFilterConfiguration {

	private static final Logger	LOGGER	= Logger.getLogger(QueryFilterConfiguration.class);

	private final List<Filter>		filters	= new ArrayList<Filter>();

	private static final class Filter {

		private final boolean	isDenyFilter;
		private final String		regExp;
		private final Pattern	pattern;
		private final boolean	containsType;

		private Filter(final boolean isDenyFilter, final String regExp, final Pattern pattern, final boolean containsType) {
			this.isDenyFilter = isDenyFilter;
			this.regExp = regExp;
			this.pattern = pattern;
			this.containsType = containsType;
		}
	}

	public void addDenyEntry(final String regexp, final String type) throws ConfigurationException {
		addEntry(true, regexp, type);
	}

	public void addAllowEntry(final String regexp, final String type) throws ConfigurationException {
		addEntry(false, regexp, type);
	}

	private void addEntry(final boolean isDenyFilter, final String regexp, final String type) throws ConfigurationException {
		try {
			final Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
			filters.add(new Filter(isDenyFilter, regexp, pattern, type != null && type.equals("contains")));
		} catch (final PatternSyntaxException e) {
			throw new ConfigurationException("Malformed RegEx-Pattern", e);
		}
	}

	public void checkAgainstFilters(final String sql) throws SQLException {
		if (!filters.isEmpty()) {
			for (final Filter filter : filters) {
				final Matcher matcher = filter.pattern.matcher(sql);
				final boolean matched;
				if (filter.containsType) {
					// matcher.contains(sql, filter.pattern);
					// TODO: contains support
					matched = matcher.find();
				} else {
					matched = matcher.matches();
				}

				if (matched) {
					if (filter.isDenyFilter) {
						final String msg = "SQL [" + sql + "] is denied due to Deny-Filter [" + filter.regExp + "]";
						LOGGER.warn(msg);
						throw new SQLException(msg);
					}
					if (LOGGER.isDebugEnabled()) {
						final String msg = "SQL [" + sql + "] is allowed due to Allow-Filter [" + filter.regExp + "]";
						LOGGER.debug(msg);
					}
					return;
				}
			}
			final String msg = "SQL [" + sql + "] didn't match any query filter and won't be executed";
			LOGGER.error(msg);
			throw new SQLException(msg);
		}
	}

	protected void log() {
		LOGGER.info("  Query Filter-Configuration:");
		for (final Filter filter : filters) {
			if (filter.isDenyFilter) {
				LOGGER.info("    Deny  : [" + filter.regExp + "]");
			} else {
				LOGGER.info("    Allow : [" + filter.regExp + "]");
			}
		}
	}
}
