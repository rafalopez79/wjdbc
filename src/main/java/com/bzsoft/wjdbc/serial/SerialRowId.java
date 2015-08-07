package com.bzsoft.wjdbc.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.RowId;
import java.sql.SQLException;

public class SerialRowId implements RowId, Externalizable {

	private static final long	serialVersionUID	= 3359567957805294836L;

	private byte[]					bytes;
	private String					str;
	private int						hashCode;

	public SerialRowId(final RowId rowId) throws SQLException {
		bytes = rowId.getBytes();
		str = rowId.toString();
		hashCode = rowId.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof RowId) {
			final RowId rowId = (RowId) o;
			final byte[] otherBytes = rowId.getBytes();
			if (bytes.length == otherBytes.length) {
				for (int i = 0; i < bytes.length; ++i) {
					if (bytes[i] != otherBytes[i]) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

	@Override
	public String toString() {
		return str;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(bytes.length);
		for (final byte b : bytes) {
			out.writeByte(b);
		}
		out.writeUTF(str);
		out.writeInt(hashCode);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		final int len = in.readInt();
		bytes = new byte[len];
		for (int i = 0; i < len; i++) {
			bytes[i] = in.readByte();
		}
		str = in.readUTF();
		hashCode = in.readInt();
	}


}
