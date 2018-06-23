package com.springboot;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.springboot.bean.ICoach;
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
		ICoach bean = applicationContext.getBean("trackCoach", ICoach.class); //creating and managing Object creation - Inversion of Control		
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
		
		//checks whether the two objects are same or not
		System.out.println("Two objects are same : " + (cricketCoach == cricketCoach1));
		
		//checks whether same objects prrint same string or not
		System.out.println(cricketCoach.toString());
		System.out.println(cricketCoach1.toString());
		
		//default scope of bean is Singleton - value set by Shubha! will be printed
		System.out.println(cricketCoach1.getUserValue());
		
		
		System.out.println("Annotation based component configuration");
		ICoach tennisCoach = applicationContext.getBean("tennisCoach", ICoach.class);
		tennisCoach.doWorkOut();
		tennisCoach.getFortune();
		
		
		
		
		
		
		
		//closing the context
		applicationContext.close();
	}
}
