package com.core.springidol;

public class Instrumentalist implements Performer {
	
	public Instrumentalist() {	}
	
	private Audience audience; 
	
	@Override
	public void perform() {
		audience.takeSeats();
		audience.turnOffMobiles();
		
		try {
			System.out.println("Playing.......");
			audience.applaud();
			
			int i = 6/0;
		} catch (Exception e) {
			audience.demandReFund();
		}
		
	}

	public Audience getAudience() {
		return audience;
	}

	public void setAudience(Audience audience) {
		this.audience = audience;
	}
	
}
