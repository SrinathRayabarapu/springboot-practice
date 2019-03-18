package com.springcore.services;

import com.springcore.services.FortuneService;

public class SadFortuneService implements FortuneService {

	@Override
	public void getFortune() {
		System.out.println("Hey.. You are so Sad today!!");
	}

}
