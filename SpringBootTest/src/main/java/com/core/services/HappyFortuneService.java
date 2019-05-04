package com.core.services;

import com.core.services.FortuneService;

public class HappyFortuneService implements FortuneService{

	@Override
	public void getFortune() {
		System.out.println("Today is your lucky day..!!");
	}
}
