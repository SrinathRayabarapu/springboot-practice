package com.springcore.chap1.knight;

public class KnightOfTheRoundTable {
	
	private String name = "";
	private HolyGrailQuest quest;
	
	public KnightOfTheRoundTable(String name) {
		this.name = name;
	}
	
	public HolyGrail embarkOnQuest(){
		return quest.embark();
	}

}
