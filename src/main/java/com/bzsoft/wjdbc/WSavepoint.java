package com.bzsoft.wjdbc;

import java.sql.SQLException;
import java.sql.Savepoint;

import com.bzsoft.wjdbc.command.DecoratedCommandSink;
import com.bzsoft.wjdbc.command.JdbcInterfaceType;
import com.bzsoft.wjdbc.command.ReflectiveCommand;

/**
 * The Class WSavepoint.
 */
public class WSavepoint extends WBase implements Savepoint {

	/**
	 * Instantiates a new w savepoint.
	 *
	 * @param uid
	 *           the uid
	 * @param sink
	 *           the sink
	 */
	WSavepoint(final long uid, final DecoratedCommandSink sink) {
		super(uid, sink);
	}

	@Override
	public int getSavepointId() throws SQLException {
		return sink.processWithIntResult(objectUid, ReflectiveCommand.<Integer, Object> of(JdbcInterfaceType.SAVEPOINT, "getSavepointId"));
	}

	@Override
	public String getSavepointName() throws SQLException {
		return sink.process(objectUid, ReflectiveCommand.<String, Object> of(JdbcInterfaceType.SAVEPOINT, "getSavepointName"));
	}
}
