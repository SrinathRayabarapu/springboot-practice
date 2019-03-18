package com.springcore.aop;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;

public class TracingAfterAdvice implements AfterReturningAdvice {

	@Override
	public void afterReturning(Object object, Method method, Object[] args,
			Object target) throws Throwable {
		System.out.println("Hellow World by : "+getClass().getName());
	}

}
