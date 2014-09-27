package com.bzsoft.wjdbc.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SerialJavaObject implements Externalizable {
	private static final long	serialVersionUID	= 4050198631045215540L;

	private Object					javaObject;

	public SerialJavaObject(final Object javaObject) {
		this.javaObject = javaObject;
	}

	public SerialJavaObject() {
		// empty
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeObject(javaObject);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		javaObject = in.readObject();
	}
}
