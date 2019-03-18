package com.springcore.services;

import com.springcore.services.FortuneService;

public class HappyFortuneService implements FortuneService{

	@Override
	public void getFortune() {
		System.out.println("Today is your lucky day..!!");
	}
}
