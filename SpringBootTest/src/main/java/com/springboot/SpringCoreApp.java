package com.springboot;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.springboot.bean.Coach;

/**
 * class designed to explore spring core functionalities
 * 
 * @author srayabar
 *
 */
public class SpringCoreApp {
	
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		Coach bean = applicationContext.getBean("trackCoach", Coach.class); //creating and managing Object creation - Inversion of Control
		
		//this is Inversion of Control(IOC) where Spring only manages Object 
		bean.doWorkOut();
		
		// this is where DI comes in to picture where it injects an Object to another class constructor
		bean.getFortune();
		
		
		
	}
}
