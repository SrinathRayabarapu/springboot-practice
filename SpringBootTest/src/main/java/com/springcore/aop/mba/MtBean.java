package com.springcore.aop.mba;

public class MtBean implements IMtBean {
	
	private String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void showValues(){
		System.out.println("First Name : "+this.firstName);
	}

}
