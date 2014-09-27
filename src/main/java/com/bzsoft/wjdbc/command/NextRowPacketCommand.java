package com.bzsoft.wjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;

import com.bzsoft.wjdbc.serial.RowPacket;
import com.bzsoft.wjdbc.server.command.ResultSetHolder;

public class NextRowPacketCommand extends BaseCommand<RowPacket, ResultSetHolder> {

	private static final long	serialVersionUID	= -8463588846424302034L;

	public NextRowPacketCommand() {
		// empty
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		// empty
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		// empty
	}

	@Override
	public RowPacket execute(final ResultSetHolder rsh, final ConnectionContext ctx) throws SQLException {
		return rsh.nextRowPacket();
	}

}
