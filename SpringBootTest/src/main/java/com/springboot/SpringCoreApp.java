package com.springboot;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.springboot.bean.Coach;
import com.springboot.bean.CricketCoach;

/**
 * class designed to explore spring core functionalities
 * 
 * @author srayabar
 *
 */
public class SpringCoreApp {
	
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		//constructor DI
		Coach bean = applicationContext.getBean("trackCoach", Coach.class); //creating and managing Object creation - Inversion of Control		
		//this is Inversion of Control(IOC) where Spring only manages Object 
		bean.doWorkOut();		
		// this is where DI comes in to picture where it injects an Object to another class constructor
		bean.getFortune();
		
		//setter method DI
		CricketCoach cricketCoach = applicationContext.getBean("cricketCoach", CricketCoach.class);
		
		cricketCoach.doWorkOut();
		
		cricketCoach.getFortune();
		
		//literal dependency injections
		cricketCoach.getEmailAddress();
		cricketCoach.getTeamName();
		
		cricketCoach.setUserValue("value set by Shubha!");
		
		
		CricketCoach cricketCoach1 = applicationContext.getBean("cricketCoach", CricketCoach.class);
		//default scope of bean is Singleton - value set by Shubha! will be printed
		System.out.println(cricketCoach1.getUserValue());
		
		
		
		
		
		
		//closing the context
		applicationContext.close();
	}
}
