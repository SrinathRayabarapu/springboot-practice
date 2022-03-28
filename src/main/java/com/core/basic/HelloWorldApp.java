package com.core.basic;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

public class HelloWorldApp {

	public static void main(String[] args) {
		
		Logger logger = Logger.getLogger(HelloWorld.class);

//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext
//				("SpringBeans.xml");
//		HelloWorld helloWorld =  (HelloWorld) applicationContext.getBean("helloWorld");
//		logger.info("Printing : "+helloWorld);

		BeanFactory factory = new XmlBeanFactory(new 
				FileSystemResource("src/SpringBeans.xml"));
		HelloWorld world = (HelloWorld)factory.getBean("helloWorld");
		logger.info(world);
	}
}