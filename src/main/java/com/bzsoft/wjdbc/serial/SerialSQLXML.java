package com.bzsoft.wjdbc.serial;

import java.io.ByteArrayInputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.sql.SQLXML;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.xml.sax.InputSource;

public class SerialSQLXML implements SQLXML, Externalizable {

	private static final long		serialVersionUID	= 68757548812947189L;

	private final StringBuilder	xml;

	public SerialSQLXML() {
		this.xml = new StringBuilder();
	}

	public SerialSQLXML(final String xml) {
		this.xml = new StringBuilder(xml);
	}

	public SerialSQLXML(final SQLXML sqlxml) throws SQLException {
		this.xml = new StringBuilder(sqlxml.getString());
		sqlxml.free();
	}

	@Override
	public void free() throws SQLException {
		xml.delete(0, xml.length());
	}

	@Override
	public InputStream getBinaryStream() throws SQLException {
		return new ByteArrayInputStream(xml.toString().getBytes());
	}

	@Override
	public Reader getCharacterStream() throws SQLException {
		return new StringReader(xml.toString());
	}

	@Override
	public <T extends Source> T getSource(final Class<T> sourceClass) throws SQLException {
		try {
			final Constructor<T> constructor = sourceClass.getConstructor(InputSource.class);
			return constructor.newInstance(new InputSource(getCharacterStream()));
		} catch (final Exception e) {
			// empty
		}
		return null;
	}

	@Override
	public String getString() throws SQLException {
		return xml.toString();
	}

	@Override
	public OutputStream setBinaryStream() throws SQLException {
		throw new UnsupportedOperationException("SQLXML.setBinaryStream() not supported, use setString(String value) instead");
	}

	@Override
	public Writer setCharacterStream() throws SQLException {
		throw new UnsupportedOperationException("SQLXML.setCharacterStream() not supported, use setString(String value) instead");
	}

	@Override
	public <T extends Result> T setResult(final Class<T> resultClass) throws SQLException {
		throw new UnsupportedOperationException("SQLXML.setResult() not supported");
	}

	@Override
	public void setString(final String value) throws SQLException {
		xml.delete(0, xml.length());
		xml.append(value);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeUTF(xml.toString());
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		xml.append(in.readUTF());
	}
}
