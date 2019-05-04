package com.core.springidol;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

public class AudienceAdvice implements MethodBeforeAdvice,
	AfterReturningAdvice,ThrowsAdvice {
	
	public AudienceAdvice() {	}
	
	private Audience audience;
	
	@Override
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		audience.takeSeats();
		audience.turnOffMobiles();
	}
	
	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args,
			Object target) throws Throwable {
		audience.applaud();
	}
	
	public void afterThrowing(Throwable throwable) {
		audience.demandReFund(); 
	}

	public Audience getAudience() {
		return audience;
	}

	public void setAudience(Audience audience) {
		this.audience = audience;
	}
	
}
