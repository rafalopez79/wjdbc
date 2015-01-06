package com.bzsoft.wjdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

import com.bzsoft.wjdbc.command.Command;
import com.bzsoft.wjdbc.command.DecoratedCommandSink;
import com.bzsoft.wjdbc.command.JdbcInterfaceType;
import com.bzsoft.wjdbc.command.ParameterTypeCombinations;
import com.bzsoft.wjdbc.command.ReflectiveCommand;
import com.bzsoft.wjdbc.serial.StreamingResultSet;
import com.bzsoft.wjdbc.util.SQLExceptionHelper;
import com.bzsoft.wjdbc.util.Validate;

public class WDatabaseMetaData extends WBase implements DatabaseMetaData {

	private final Connection	connection;

	public WDatabaseMetaData(final Connection conn, final long uid, final DecoratedCommandSink sink) {
		super(uid, sink);
		connection = conn;
	}

	@Override
	public boolean allProceduresAreCallable() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "allProceduresAreCallable"));
	}

	@Override
	public boolean allTablesAreSelectable() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "allTablesAreSelectable"));
	}

	@Override
	public String getURL() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getURL"));
	}

	@Override
	public String getUserName() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getUserName"));
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "isReadOnly"));
	}

	@Override
	public boolean nullsAreSortedHigh() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "nullsAreSortedHigh"));
	}

	@Override
	public boolean nullsAreSortedLow() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink
				.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "nullsAreSortedLow"));
	}

	@Override
	public boolean nullsAreSortedAtStart() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "nullsAreSortedAtStart"));
	}

	@Override
	public boolean nullsAreSortedAtEnd() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "nullsAreSortedAtEnd"));
	}

	@Override
	public String getDatabaseProductName() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getDatabaseProductName"));
	}

	@Override
	public String getDatabaseProductVersion() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getDatabaseProductVersion"));
	}

	@Override
	public String getDriverName() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getDriverName"));
	}

	@Override
	public String getDriverVersion() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getDriverVersion"));
	}

	@Override
	public int getDriverMajorVersion() {
		try {
			Validate.isFalse(connection.isClosed(), "Connection closed");
			return sink.processWithIntResult(objectUid,
					ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getDriverMajorVersion"));
		} catch (final SQLException e) {
			return 0;
		}
	}

	@Override
	public int getDriverMinorVersion() {
		try {
			Validate.isFalse(connection.isClosed(), "Connection closed");
			return sink.processWithIntResult(objectUid,
					ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getDriverMinorVersion"));
		} catch (final SQLException e) {
			return 0;
		}
	}

	@Override
	public boolean usesLocalFiles() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "usesLocalFiles"));
	}

	@Override
	public boolean usesLocalFilePerTable() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "usesLocalFilePerTable"));
	}

	@Override
	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsMixedCaseIdentifiers"));
	}

	@Override
	public boolean storesUpperCaseIdentifiers() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "storesUpperCaseIdentifiers"));
	}

	@Override
	public boolean storesLowerCaseIdentifiers() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "storesLowerCaseIdentifiers"));
	}

	@Override
	public boolean storesMixedCaseIdentifiers() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "storesMixedCaseIdentifiers"));
	}

	@Override
	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsMixedCaseQuotedIdentifiers"));
	}

	@Override
	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "storesUpperCaseQuotedIdentifiers"));
	}

	@Override
	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "storesLowerCaseQuotedIdentifiers"));
	}

	@Override
	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "storesMixedCaseQuotedIdentifiers"));
	}

	@Override
	public String getIdentifierQuoteString() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getIdentifierQuoteString"));
	}

	@Override
	public String getSQLKeywords() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getSQLKeywords"));
	}

	@Override
	public String getNumericFunctions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getNumericFunctions"));
	}

	@Override
	public String getStringFunctions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getStringFunctions"));
	}

	@Override
	public String getSystemFunctions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getSystemFunctions"));
	}

	@Override
	public String getTimeDateFunctions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getTimeDateFunctions"));
	}

	@Override
	public String getSearchStringEscape() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getSearchStringEscape"));
	}

	@Override
	public String getExtraNameCharacters() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getExtraNameCharacters"));
	}

	@Override
	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsAlterTableWithAddColumn"));
	}

	@Override
	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsAlterTableWithDropColumn"));
	}

	@Override
	public boolean supportsColumnAliasing() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsColumnAliasing"));
	}

	@Override
	public boolean nullPlusNonNullIsNull() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "nullPlusNonNullIsNull"));
	}

	@Override
	public boolean supportsConvert() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsConvert"));
	}

	@Override
	public boolean supportsConvert(final int fromType, final int toType) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(
				objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsConvert", new Object[] { Integer.valueOf(fromType),
						Integer.valueOf(toType) }, ParameterTypeCombinations.INTINT));
	}

	@Override
	public boolean supportsTableCorrelationNames() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsTableCorrelationNames"));
	}

	@Override
	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsDifferentTableCorrelationNames"));
	}

	@Override
	public boolean supportsExpressionsInOrderBy() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsExpressionsInOrderBy"));
	}

	@Override
	public boolean supportsOrderByUnrelated() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsOrderByUnrelated"));
	}

	@Override
	public boolean supportsGroupBy() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsGroupBy"));
	}

	@Override
	public boolean supportsGroupByUnrelated() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsGroupByUnrelated"));
	}

	@Override
	public boolean supportsGroupByBeyondSelect() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsGroupByBeyondSelect"));
	}

	@Override
	public boolean supportsLikeEscapeClause() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsLikeEscapeClause"));
	}

	@Override
	public boolean supportsMultipleResultSets() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsMultipleResultSets"));
	}

	@Override
	public boolean supportsMultipleTransactions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsMultipleTransactions"));
	}

	@Override
	public boolean supportsNonNullableColumns() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsNonNullableColumns"));
	}

	@Override
	public boolean supportsMinimumSQLGrammar() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsMinimumSQLGrammar"));
	}

	@Override
	public boolean supportsCoreSQLGrammar() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsCoreSQLGrammar"));
	}

	@Override
	public boolean supportsExtendedSQLGrammar() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsExtendedSQLGrammar"));
	}

	@Override
	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsANSI92EntryLevelSQL"));
	}

	@Override
	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsANSI92IntermediateSQL"));
	}

	@Override
	public boolean supportsANSI92FullSQL() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsANSI92FullSQL"));
	}

	@Override
	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsIntegrityEnhancementFacility"));
	}

	@Override
	public boolean supportsOuterJoins() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsOuterJoins"));
	}

	@Override
	public boolean supportsFullOuterJoins() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsFullOuterJoins"));
	}

	@Override
	public boolean supportsLimitedOuterJoins() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsLimitedOuterJoins"));
	}

	@Override
	public String getSchemaTerm() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getSchemaTerm"));
	}

	@Override
	public String getProcedureTerm() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getProcedureTerm"));
	}

	@Override
	public String getCatalogTerm() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getCatalogTerm"));
	}

	@Override
	public boolean isCatalogAtStart() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "isCatalogAtStart"));
	}

	@Override
	public String getCatalogSeparator() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getCatalogSeparator"));
	}

	@Override
	public boolean supportsSchemasInDataManipulation() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsSchemasInDataManipulation"));
	}

	@Override
	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsSchemasInProcedureCalls"));
	}

	@Override
	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsSchemasInTableDefinitions"));
	}

	@Override
	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsSchemasInIndexDefinitions"));
	}

	@Override
	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsSchemasInPrivilegeDefinitions"));
	}

	@Override
	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsCatalogsInDataManipulation"));
	}

	@Override
	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsCatalogsInProcedureCalls"));
	}

	@Override
	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsCatalogsInTableDefinitions"));
	}

	@Override
	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsCatalogsInIndexDefinitions"));
	}

	@Override
	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsCatalogsInPrivilegeDefinitions"));
	}

	@Override
	public boolean supportsPositionedDelete() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsPositionedDelete"));
	}

	@Override
	public boolean supportsPositionedUpdate() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsPositionedUpdate"));
	}

	@Override
	public boolean supportsSelectForUpdate() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsSelectForUpdate"));
	}

	@Override
	public boolean supportsStoredProcedures() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsStoredProcedures"));
	}

	@Override
	public boolean supportsSubqueriesInComparisons() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsSubqueriesInComparisons"));
	}

	@Override
	public boolean supportsSubqueriesInExists() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsSubqueriesInExists"));
	}

	@Override
	public boolean supportsSubqueriesInIns() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsSubqueriesInIns"));
	}

	@Override
	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsSubqueriesInQuantifieds"));
	}

	@Override
	public boolean supportsCorrelatedSubqueries() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsCorrelatedSubqueries"));
	}

	@Override
	public boolean supportsUnion() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsUnion"));
	}

	@Override
	public boolean supportsUnionAll() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsUnionAll"));
	}

	@Override
	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsOpenCursorsAcrossCommit"));
	}

	@Override
	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsOpenCursorsAcrossRollback"));
	}

	@Override
	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsOpenStatementsAcrossCommit"));
	}

	@Override
	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsOpenStatementsAcrossRollback"));
	}

	@Override
	public int getMaxBinaryLiteralLength() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxBinaryLiteralLength"));
	}

	@Override
	public int getMaxCharLiteralLength() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxCharLiteralLength"));
	}

	@Override
	public int getMaxColumnNameLength() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxColumnNameLength"));
	}

	@Override
	public int getMaxColumnsInGroupBy() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxColumnsInGroupBy"));
	}

	@Override
	public int getMaxColumnsInIndex() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxColumnsInIndex"));
	}

	@Override
	public int getMaxColumnsInOrderBy() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxColumnsInOrderBy"));
	}

	@Override
	public int getMaxColumnsInSelect() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink
				.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxColumnsInSelect"));
	}

	@Override
	public int getMaxColumnsInTable() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxColumnsInTable"));
	}

	@Override
	public int getMaxConnections() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxConnections"));
	}

	@Override
	public int getMaxCursorNameLength() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxCursorNameLength"));
	}

	@Override
	public int getMaxIndexLength() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxIndexLength"));
	}

	@Override
	public int getMaxSchemaNameLength() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxSchemaNameLength"));
	}

	@Override
	public int getMaxProcedureNameLength() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxProcedureNameLength"));
	}

	@Override
	public int getMaxCatalogNameLength() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxCatalogNameLength"));
	}

	@Override
	public int getMaxRowSize() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxRowSize"));
	}

	@Override
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "doesMaxRowSizeIncludeBlobs"));
	}

	@Override
	public int getMaxStatementLength() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink
				.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxStatementLength"));
	}

	@Override
	public int getMaxStatements() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxStatements"));
	}

	@Override
	public int getMaxTableNameLength() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink
				.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxTableNameLength"));
	}

	@Override
	public int getMaxTablesInSelect() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxTablesInSelect"));
	}

	@Override
	public int getMaxUserNameLength() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getMaxUserNameLength"));
	}

	@Override
	public int getDefaultTransactionIsolation() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getDefaultTransactionIsolation"));
	}

	@Override
	public boolean supportsTransactions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsTransactions"));
	}

	@Override
	public boolean supportsTransactionIsolationLevel(final int level) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(
				objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsTransactionIsolationLevel",
						new Object[] { Integer.valueOf(level) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsDataDefinitionAndDataManipulationTransactions"));
	}

	@Override
	public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsDataManipulationTransactionsOnly"));
	}

	@Override
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "dataDefinitionCausesTransactionCommit"));
	}

	@Override
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "dataDefinitionIgnoredInTransactions"));
	}

	@Override
	public ResultSet getProcedures(final String catalog, final String schemaPattern, final String procedureNamePattern) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getProcedures", new Object[] { catalog,
				schemaPattern, procedureNamePattern }, ParameterTypeCombinations.STRSTRSTR));
	}

	@Override
	public ResultSet getProcedureColumns(final String catalog, final String schemaPattern, final String procedureNamePattern,
			final String columnNamePattern) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getProcedureColumns", new Object[] {
				catalog, schemaPattern, procedureNamePattern, columnNamePattern }, ParameterTypeCombinations.STRSTRSTRSTR));
	}

	@Override
	public ResultSet getTables(final String catalog, final String schemaPattern, final String tableNamePattern, final String types[])
			throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getTables", new Object[] { catalog,
				schemaPattern, tableNamePattern, types }, ParameterTypeCombinations.STRSTRSTRSTRA));
	}

	@Override
	public ResultSet getSchemas() throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getSchemas"));
	}

	@Override
	public ResultSet getCatalogs() throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getCatalogs"));
	}

	@Override
	public ResultSet getTableTypes() throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getTableTypes"));
	}

	@Override
	public ResultSet getColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern)
			throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getColumns", new Object[] { catalog,
				schemaPattern, tableNamePattern, columnNamePattern }, ParameterTypeCombinations.STRSTRSTRSTR));
	}

	@Override
	public ResultSet getColumnPrivileges(final String catalog, final String schema, final String table, final String columnNamePattern)
			throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getColumnPrivileges", new Object[] {
				catalog, schema, table, columnNamePattern }, ParameterTypeCombinations.STRSTRSTRSTR));
	}

	@Override
	public ResultSet getTablePrivileges(final String catalog, final String schemaPattern, final String tableNamePattern) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getTablePrivileges", new Object[] {
				catalog, schemaPattern, tableNamePattern }, ParameterTypeCombinations.STRSTRSTR));
	}

	@Override
	public ResultSet getBestRowIdentifier(final String catalog, final String schema, final String table, final int scope, final boolean nullable)
			throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getBestRowIdentifier", new Object[] {
				catalog, schema, table, Integer.valueOf(scope), nullable ? Boolean.TRUE : Boolean.FALSE }, ParameterTypeCombinations.STRSTRSTRINTBOL));
	}

	@Override
	public ResultSet getVersionColumns(final String catalog, final String schema, final String table) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getVersionColumns", new Object[] { catalog,
				schema, table }, ParameterTypeCombinations.STRSTRSTR));
	}

	@Override
	public ResultSet getPrimaryKeys(final String catalog, final String schema, final String table) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getPrimaryKeys", new Object[] { catalog,
				schema, table }, ParameterTypeCombinations.STRSTRSTR));
	}

	@Override
	public ResultSet getImportedKeys(final String catalog, final String schema, final String table) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getImportedKeys", new Object[] { catalog,
				schema, table }, ParameterTypeCombinations.STRSTRSTR));
	}

	@Override
	public ResultSet getExportedKeys(final String catalog, final String schema, final String table) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getExportedKeys", new Object[] { catalog,
				schema, table }, ParameterTypeCombinations.STRSTRSTR));
	}

	@Override
	public ResultSet getCrossReference(final String primaryCatalog, final String primarySchema, final String primaryTable,
			final String foreignCatalog, final String foreignSchema, final String foreignTable) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getCrossReference", new Object[] {
				primaryCatalog, primarySchema, primaryTable, foreignCatalog, foreignSchema, foreignTable }, ParameterTypeCombinations.STRSTRSTRSTRSTRSTR));
	}

	@Override
	public ResultSet getTypeInfo() throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getTypeInfo"));
	}

	@Override
	public ResultSet getIndexInfo(final String catalog, final String schema, final String table, final boolean unique, final boolean approximate)
			throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getIndexInfo", new Object[] { catalog,
				schema, table, unique ? Boolean.TRUE : Boolean.FALSE, approximate ? Boolean.TRUE : Boolean.FALSE },
				ParameterTypeCombinations.STRSTRSTRBOLBOL));
	}

	@Override
	public boolean supportsResultSetType(final int type) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA,
				"supportsResultSetType", new Object[] { Integer.valueOf(type) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean supportsResultSetConcurrency(final int type, final int concurrency) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(
				objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsResultSetConcurrency",
						new Object[] { Integer.valueOf(type), Integer.valueOf(concurrency) }, ParameterTypeCombinations.INTINT));
	}

	@Override
	public boolean ownUpdatesAreVisible(final int type) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA,
				"ownUpdatesAreVisible", new Object[] { Integer.valueOf(type) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean ownDeletesAreVisible(final int type) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA,
				"ownDeletesAreVisible", new Object[] { Integer.valueOf(type) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean ownInsertsAreVisible(final int type) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA,
				"ownInsertsAreVisible", new Object[] { Integer.valueOf(type) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean othersUpdatesAreVisible(final int type) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(
				objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "othersUpdatesAreVisible",
						new Object[] { Integer.valueOf(type) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean othersDeletesAreVisible(final int type) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(
				objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "othersDeletesAreVisible",
						new Object[] { Integer.valueOf(type) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean othersInsertsAreVisible(final int type) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(
				objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "othersInsertsAreVisible",
						new Object[] { Integer.valueOf(type) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean updatesAreDetected(final int type) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA,
				"updatesAreDetected", new Object[] { Integer.valueOf(type) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean deletesAreDetected(final int type) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA,
				"deletesAreDetected", new Object[] { Integer.valueOf(type) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean insertsAreDetected(final int type) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid, ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA,
				"insertsAreDetected", new Object[] { Integer.valueOf(type) }, ParameterTypeCombinations.INT));
	}

	@Override
	public boolean supportsBatchUpdates() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsBatchUpdates"));
	}

	@Override
	public ResultSet getUDTs(final String catalog, final String schemaPattern, final String typeNamePattern, final int[] types) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getUDTs", new Object[] { catalog,
				schemaPattern, typeNamePattern, types }, ParameterTypeCombinations.STRSTRSTRINTA));
	}

	@Override
	public Connection getConnection() throws SQLException {
		return connection;
	}

	@Override
	public boolean supportsSavepoints() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsSavepoints"));
	}

	@Override
	public boolean supportsNamedParameters() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsNamedParameters"));
	}

	@Override
	public boolean supportsMultipleOpenResults() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsMultipleOpenResults"));
	}

	@Override
	public boolean supportsGetGeneratedKeys() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsGetGeneratedKeys"));
	}

	@Override
	public ResultSet getSuperTypes(final String catalog, final String schemaPattern, final String typeNamePattern) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getSuperTypes", new Object[] { catalog,
				schemaPattern, typeNamePattern }, ParameterTypeCombinations.STRSTRSTR));
	}

	@Override
	public ResultSet getSuperTables(final String catalog, final String schemaPattern, final String tableNamePattern) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getSuperTables", new Object[] { catalog,
				schemaPattern, tableNamePattern }, ParameterTypeCombinations.STRSTRSTR));
	}

	@Override
	public ResultSet getAttributes(final String catalog, final String schemaPattern, final String typeNamePattern, final String attributeNamePattern)
			throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getAttributes", new Object[] { catalog,
				schemaPattern, typeNamePattern, attributeNamePattern }, ParameterTypeCombinations.STRSTRSTRSTR));
	}

	@Override
	public boolean supportsResultSetHoldability(final int holdability) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(
				objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsResultSetHoldability",
						new Object[] { Integer.valueOf(holdability) }, ParameterTypeCombinations.INT));
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getResultSetHoldability"));
	}

	@Override
	public int getDatabaseMajorVersion() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getDatabaseMajorVersion"));
	}

	@Override
	public int getDatabaseMinorVersion() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid,
				ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getDatabaseMinorVersion"));
	}

	@Override
	public int getJDBCMajorVersion() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getJDBCMajorVersion"));
	}

	@Override
	public int getJDBCMinorVersion() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getJDBCMinorVersion"));
	}

	@Override
	public int getSQLStateType() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getSQLStateType"));
	}

	@Override
	public boolean locatorsUpdateCopy() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "locatorsUpdateCopy"));
	}

	@Override
	public boolean supportsStatementPooling() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsStatementPooling"));
	}

	protected <T> ResultSet queryResultSet(final Command<ResultSet, T> cmd) throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		try {
			final StreamingResultSet rs = (StreamingResultSet) sink.process(objectUid, cmd);
			rs.setCommandSink(sink);
			return rs;
		} catch (final Exception e) {
			throw SQLExceptionHelper.wrap(e);
		}
	}

	@Override
	public RowIdLifetime getRowIdLifetime() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		final String s = sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getRowIdLifetime"));
		return RowIdLifetime.valueOf(s);
	}

	@Override
	public ResultSet getSchemas(final String catalog, final String schemaPattern) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getSchemas", new Object[] { catalog,
				schemaPattern }, ParameterTypeCombinations.STRSTR));
	}

	@Override
	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "supportsStoredFunctionsUsingCallSyntax"));
	}

	@Override
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "autoCommitFailureClosesAllResultSets"));
	}

	@Override
	public ResultSet getClientInfoProperties() throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getClientInfoProperties"));
	}

	@Override
	public ResultSet getFunctions(final String catalog, final String schemaPattern, final String functionNamePattern) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getFunctions", new Object[] { catalog,
				schemaPattern, functionNamePattern }, ParameterTypeCombinations.STRSTR));
	}

	@Override
	public ResultSet getFunctionColumns(final String catalog, final String schemaPattern, final String functionNamePattern,
			final String columnNamePattern) throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getFunctionColumns", new Object[] {
				catalog, schemaPattern, functionNamePattern, columnNamePattern }, ParameterTypeCombinations.STRSTRSTRSTR));
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return iface.isAssignableFrom(WDatabaseMetaData.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return (T) this;
	}

	public ResultSet getPseudoColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern)
			throws SQLException {
		return queryResultSet(ReflectiveCommand.<ResultSet, Object> of(JdbcInterfaceType.DATABASEMETADATA, "getPseudoColumns", new Object[] { catalog,
				schemaPattern, tableNamePattern, columnNamePattern }, ParameterTypeCombinations.STRSTRSTRSTR));
	}

	public boolean generatedKeyAlwaysReturned() throws SQLException {
		Validate.isFalse(connection.isClosed(), "Connection closed");
		return sink.processWithBooleanResult(objectUid,
				ReflectiveCommand.<Boolean, Object> of(JdbcInterfaceType.DATABASEMETADATA, "generatedKeyAlwaysReturned"));
	}
}
