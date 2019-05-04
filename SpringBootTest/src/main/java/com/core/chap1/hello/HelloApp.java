package com.core.chap1.hello;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

public class HelloApp {

	public static void main(String[] args) {

// 		This is with Application context
//		ApplicationContext context = new ClassPathXmlApplicationContext
//			("hello.xml");

//		GreetingService service = (GreetingService)context.getBean("greetingService");
//		service.sayGreeting();

		// Using Beanfactory
		BeanFactory beanFactory = new XmlBeanFactory(new FileSystemResource("src/hello.xml"));
		GreetingService service = (GreetingService)beanFactory.getBean("greetingService");
		
		service.sayGreeting();
	}
}
