package com.bzsoft.wjdbc.serial;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class encapsulates the context in which a remote command was called. It
 * can be used to find the location of objects that weren't disposed correctly.
 */
public class CallingContext implements Externalizable {

	private static final long	serialVersionUID	= 3906934495134101813L;

	private String					stackTrace;

	public CallingContext() {
		final Throwable t = new Exception();
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		pw.println("--- The orphaned object was created within the following calling context ---");
		t.printStackTrace(pw);
		pw.println("----------------------- End of calling context -----------------------------");
		stackTrace = sw.toString();
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		stackTrace = in.readUTF();
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeUTF(stackTrace);
	}

	public String getStackTrace() {
		return stackTrace;
	}
}
