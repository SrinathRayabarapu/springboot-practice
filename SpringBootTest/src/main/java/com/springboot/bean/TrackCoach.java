package com.springboot.bean;

import com.springboot.services.FortuneService;

public class TrackCoach implements Coach{
	
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

}