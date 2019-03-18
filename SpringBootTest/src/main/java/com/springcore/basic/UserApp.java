package com.springcore.basic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserApp {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("SpringBeans.xml");
		User user = (User)applicationContext.getBean("user");
		System.out.println(user);
	}
}
