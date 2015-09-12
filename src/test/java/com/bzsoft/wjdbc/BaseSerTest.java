package com.bzsoft.wjdbc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Random;

import com.bzsoft.wjdbc.rmi.sf.LZFCompressingInputStream;
import com.bzsoft.wjdbc.rmi.sf.LZFCompressingOutputStream;

public abstract class BaseSerTest {

	protected static byte[] serialize(final Object obj) throws IOException{
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final LZFCompressingOutputStream zos = new LZFCompressingOutputStream(baos);
		final ObjectOutputStream oos = new ObjectOutputStream(zos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		return baos.toByteArray();
	}

	@SuppressWarnings("unchecked")
	protected static <T> T deserialize(final byte[] barray) throws IOException, ClassNotFoundException{
		final ByteArrayInputStream bais = new ByteArrayInputStream(barray);
		final LZFCompressingInputStream zis = new LZFCompressingInputStream(bais);
		final ObjectInputStream ois = new ObjectInputStream(zis);
		final Object o = ois.readObject();
		ois.close();
		return (T)o;
	}

	protected static int randomInt(final int n){
		final Random rand = new Random();
		return rand.nextInt(n);
	}

	protected static String randomString(final int n){
		final Random rand = new Random();
		final byte[] bytes = new byte[n];
		rand.nextBytes(bytes);
		return new String(bytes);
	}

	protected static boolean equals(final byte[] a, final byte[] b){
		if (a == null && b == null){
			return true;
		}
		return Arrays.equals(a, b);
	}
}
