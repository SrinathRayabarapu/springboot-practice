package com.springcore.basic;

import org.springframework.stereotype.Component;

@Component("user")
public class User {
	
	private int rollNumber;
	private int mobileNumber;
	private String name;
	private String courseName;
	
	public User(int rollNumber,int mobileNumber,String name,String courseName) {
		this.rollNumber = rollNumber;
		this.mobileNumber = mobileNumber;
		this.name = name;
		this.courseName = courseName;
	}

	@Override
	public String toString() {
		return "Your name is "+name+" ,course name is "+courseName+" ,roll number is "+rollNumber
		+" ,mobile number is "+mobileNumber;
	}
}
