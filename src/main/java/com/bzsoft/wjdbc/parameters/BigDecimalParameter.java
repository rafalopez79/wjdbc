package com.bzsoft.wjdbc.parameters;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BigDecimalParameter implements PreparedStatementParameter {

	private static final long	serialVersionUID	= -8577950851500487084L;

	private BigDecimal			value;

	public BigDecimalParameter() {
		// empty
	}

	public BigDecimalParameter(final BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		return value;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		value = (BigDecimal) in.readObject();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(value);
	}

	@Override
	public void setParameter(final PreparedStatement pstmt, final int index) throws SQLException {
		pstmt.setBigDecimal(index, value);
	}

	@Override
	public String toString() {
		return "BigDecimal: " + value.toString();
	}
}
