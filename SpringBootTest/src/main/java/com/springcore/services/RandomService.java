package com.springcore.services;

import com.springcore.services.FortuneService;

public class RandomService implements FortuneService {

	@Override
	public void getFortune() {
		System.out.println("It's your Random fortune today!");
	}

}
