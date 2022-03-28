package com.core.bean;

import com.core.services.FortuneService;

public class TrackCoach implements ICoach{
	
	private FortuneService fortuneService;
	
	private String userValue;
	
	//this is where dependency injection happens
	public TrackCoach(FortuneService fortuneService) {
		this.fortuneService = fortuneService;
	}
	
	@Override
	public void doWorkOut() {
		System.out.println("Run for 10KMs!");
	}
	
	@Override
	public void getFortune() {
		fortuneService.getFortune();
	}

	/**
	 * @return the userValue
	 */
	public String getUserValue() {
		return userValue;
	}

	/**
	 * @param userValue the userValue to set
	 */
	public void setUserValue(String userValue) {
		this.userValue = userValue;
	}
	
	private void startItUp() {
		System.out.println("Let's start it..");
	}
	
	private void loosenItUp() {
		System.out.println("That's it, end!!");
	}
}