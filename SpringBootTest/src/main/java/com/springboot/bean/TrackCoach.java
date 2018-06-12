package com.springboot.bean;

public class TrackCoach implements Coach{
	
	@Override
	public void doWorkOut() {
		System.out.println("Run for 10KMs!");
	}

}
