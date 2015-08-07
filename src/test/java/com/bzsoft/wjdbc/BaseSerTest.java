package com.bzsoft.wjdbc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

public abstract class BaseSerTest {

	protected static byte[] serialize(final Object obj) throws IOException{
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		return baos.toByteArray();
	}

	@SuppressWarnings("unchecked")
	protected static <T> T deserialize(final byte[] barray) throws IOException, ClassNotFoundException{
		final ByteArrayInputStream bais = new ByteArrayInputStream(barray);
		final ObjectInputStream ois = new ObjectInputStream(bais);
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

}
