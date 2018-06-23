package com.springboot.components;

import org.springframework.stereotype.Component;

import com.springboot.services.FortuneService;

@Component
public class RandomService implements FortuneService {

	@Override
	public void getFortune() {
		System.out.println("It's your Random fortune today!");
	}

}
