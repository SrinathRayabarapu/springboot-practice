package com.springcore;

import java.util.Objects;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.springcore.bean.CricketCoach;
import com.springcore.bean.ICoach;
import com.springcore.config.SportConfig;

/**
 * main class to explore spring core functionalities such as mvc, ioc, templates etc.
 * 
 * @author Srinath.Rayabarapu
 *
 */
public class SpringCoreApp {
	
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext applicationContext = null;
		AnnotationConfigApplicationContext applicationContext2 = null;
		
		try {
			//XML based configuration
			applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
			
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
			
			//checks whether same objects print same string or not
			System.out.println(cricketCoach.toString());
			System.out.println(cricketCoach1.toString());
			
			//default scope of bean is Singleton - value set by Shubha! will be printed
			System.out.println(cricketCoach1.getUserValue());
			
			//closing the context
			applicationContext.close();
			
			
			//Java based configurations
			applicationContext2 = new AnnotationConfigApplicationContext(SportConfig.class);
			
			System.out.println("Annotation based component configuration");
			
			//this is with component scan - @Qualifier used here
			//ICoach tennisCoach = applicationContext2.getBean("tennisCoach", ICoach.class);
			//tennisCoach.doWorkOut();
			//tennisCoach.getFortune();
			
			//this is without component scan
			ICoach swimCoach = applicationContext2.getBean("swimCoach", ICoach.class);
			swimCoach.doWorkOut();
			swimCoach.getFortune();
			
		} catch (Exception e) {
			System.err.println("Exception in main() : " + e);
		} finally {
			
			//close the contexts
			if(Objects.nonNull(applicationContext))
				applicationContext.close();
			
			if(Objects.nonNull(applicationContext2))
				applicationContext2.close();
		}
		
	}
}
