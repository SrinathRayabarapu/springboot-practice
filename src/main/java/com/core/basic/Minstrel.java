package com.core.basic;

import org.apache.log4j.Logger;

public class Minstrel {
	Logger logger = Logger.getLogger(getClass());
	
	public void singBefore(){
		logger.info("Singing before knight song....");
	}
	
	public void singAfter(){
		logger.info("Singing after same knight song....");
	}
}
