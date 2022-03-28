package com.core.services;

import com.core.services.FortuneService;

public class SadFortuneService implements FortuneService {

	@Override
	public void getFortune() {
		System.out.println("Hey.. You are so Sad today!!");
	}

}
