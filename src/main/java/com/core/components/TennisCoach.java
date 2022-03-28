package com.core.components;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import com.core.bean.ICoach;

@Component
//@Scope("prototype") //prototype is used for non singleton. Default is singleton.
public class TennisCoach implements ICoach{
	
    // @Autowired
    // @Qualifier("randomService") //if more than one implementations are present
    // private FortuneService fortuneService;
	
	/*
	// constructor injection
	@Autowired
	public TennisCoach(FortuneService fortuneService) {
		this.fortuneService = fortuneService;
	}
	*/
	
	public TennisCoach() {
		System.out.println("TennisCoach() - In side Default constructor!");
	}
	
	@PostConstruct
	private void doSomeInitialization() {
		System.out.println("Initialized something successfully..!!");
	}
	
	@PreDestroy
	private void doSomeCleanup() {
		System.out.println("Cleaned up something successfully!!");
	}
	
	/*
	 * //setter method injection : this need not to be on setter method only, in
	 * latest spring, any method with Autowired will work
	 * 
	 * @Autowired //public void setFortuneService(FortuneService fortuneService) {
	 * public void getSomeCoolFortuneService(FortuneService fortuneService) {
	 * this.fortuneService = fortuneService; }
	 */

	@Override
	public void doWorkOut() {
		System.out.println("Boss, I will do workout!!");
	}
	
	@Override
	public void getFortune() {
        // this.fortuneService.getFortune();
	}
}
