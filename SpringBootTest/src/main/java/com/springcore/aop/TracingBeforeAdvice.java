package com.springcore.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

public class TracingBeforeAdvice implements MethodBeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println("Hellow World by : "+getClass().getName());
	}

}
