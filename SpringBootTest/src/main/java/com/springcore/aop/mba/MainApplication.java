package com.springcore.aop.mba;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class MainApplication {
	
	public static void main(String[] args) {
		BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("hello.xml"));
		
		IMtBean bean = (IMtBean)beanFactory.getBean("afterBean");
		
		bean.showValues();
		
	}	
}
