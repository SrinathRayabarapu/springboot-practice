package com.springcore.springidol;

public class Audience {
	
	public Audience() {	}
	
	// Executes before perfomance 
	public void takeSeats(){
		System.out.println("The Audience taking their seats !");
	}
	
	public void turnOffMobiles(){
		System.out.println("The Audience are turning off their mobiles !");
	}
	
	// Executes after performance
	public void applaud(){
		System.out.println("Clap Clap Clap....!");
	}
	
	// Executes after bad performance 
	public void demandReFund(){
		System.out.println("Booo...! we want our money back ");
	}

}
