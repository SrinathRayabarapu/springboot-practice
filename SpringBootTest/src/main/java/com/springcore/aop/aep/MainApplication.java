package com.springcore.aop.aep;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class MainApplication {
	
	public static void main(String[] args) {
		MyClass target = new MyClass();
		
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		
		pointcut.setExpression("execution(* bean..*.get*(..))");
		
		Advisor advisor = new DefaultPointcutAdvisor(pointcut,new SimpleAfterAdvice());
		
		ProxyFactory factory = new ProxyFactory();
		factory.setTarget(target);
		factory.addAdvisor(advisor);
		
		MyClass proxy = (MyClass)factory.getProxy();
		
		System.out.println(proxy.getName());
		proxy.setName("Valtech India");
		
		System.out.println(proxy.getHeight());
		
	}
}
