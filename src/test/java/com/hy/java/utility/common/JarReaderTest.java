package com.hy.java.utility.common;

import org.junit.Test;

public class JarReaderTest {
	@Test
	public void currentProperties() {
		for (String s : JarReader.currentProperties()) {
			System.out.println(s);
		}
	}
}
