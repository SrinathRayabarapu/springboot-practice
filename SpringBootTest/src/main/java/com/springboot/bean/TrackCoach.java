package com.springboot.bean;

import com.springboot.services.FortuneService;

public class TrackCoach implements Coach{
	
	private FortuneService fortuneService;
	
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

}
