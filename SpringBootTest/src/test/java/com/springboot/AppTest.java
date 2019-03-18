package com.springboot;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppTest {
	
	Logger LOG = LoggerFactory.getLogger("SpringBoot");

	@Test
	public void testSomething() {
		LOG.debug("Testing testSomething() method...");
	}

}
