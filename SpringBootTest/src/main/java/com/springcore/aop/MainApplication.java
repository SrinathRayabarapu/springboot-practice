package com.springcore.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApplication {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("hello.xml");
		IBusinessLogic businessLogic =  (IBusinessLogic)context.getBean("businessLogicBean");
		businessLogic.foo();
	}
}
