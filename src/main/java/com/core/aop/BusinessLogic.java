package com.core.aop;

public class BusinessLogic implements IBusinessLogic {

	@Override
	public void foo() {
		System.out.println("Inside BusinessLogic.foo()");
	}

}
