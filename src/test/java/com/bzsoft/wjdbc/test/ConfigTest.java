package com.bzsoft.wjdbc.test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Properties;

import com.bzsoft.wjdbc.server.config.ConfigurationException;
import com.bzsoft.wjdbc.server.config.WJdbcConfiguration;
import com.bzsoft.wjdbc.util.StreamCloser;

public class ConfigTest {

	public static void main(final String[] args) throws ConfigurationException {
		InputStream is = null;
		try {
			is = ConfigTest.class.getClassLoader().getResourceAsStream("wjdbc_config.xml");
			final WJdbcConfiguration config = WJdbcConfiguration.init(is, new Properties());
			System.out.println(config);
			final BigDecimal[] arr = new BigDecimal[100];
			final Object[] objarr = new Object[100];
			for (int i = 0; i < 100; i++) {
				arr[i] = new BigDecimal(i);
				objarr[i] = arr[i];
			}
			System.out.println(arr);
			System.out.println(objarr);
			System.out.println(Arrays.equals(arr, objarr));
			final BigDecimal[] arr2 = (BigDecimal[]) objarr;
		} finally {
			StreamCloser.close(is);
		}
	}
}
