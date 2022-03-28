package com.core.chap1.hello;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloApp {

    public static void main(String[] args) {
// 		This is with Application context
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        GreetingService service = (GreetingService) context.getBean("greetingService");
        service.sayGreeting();
    }

}