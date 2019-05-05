package com.core.dao.template;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApplication {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		EmployeeDaoImpl daoImpl = (EmployeeDaoImpl)context.getBean("employeeDAO");
		
		System.out.println(daoImpl.getTotalNumberInstitutions());
		
	}

}
