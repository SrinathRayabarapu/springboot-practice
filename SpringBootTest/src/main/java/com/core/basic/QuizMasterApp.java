package com.core.basic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuizMasterApp {

	public static void main(String[] args) {
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("SpringBeans.xml");
		QuizMasterService quizMasterService = (QuizMasterService) applicationContext.getBean("quizMasterService");
		
		quizMasterService.askAQuestion();
	}
}