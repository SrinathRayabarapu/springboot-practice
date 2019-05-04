package com.core.basic;

import org.apache.log4j.Logger;

public class KnightOfTheRoundTable implements Knight{
	
	Logger logger = Logger.getLogger(getClass());
	private String name;
	private HolyGrailQuest quest;
	private Minstrel minstrel;
	
	public KnightOfTheRoundTable(String name) {
		this.name = name;
	}

	@Override
	public HolyGrail embarkQuest() {
		minstrel.singBefore();
		logger.info("Knight name is "+name);
		HolyGrail grail = quest.embark();
		minstrel.singAfter();
		return grail;	
	}

	public void setQuest(HolyGrailQuest quest) {
		this.quest = quest;
	}

	public void setMinstrel(Minstrel minstrel) {
		this.minstrel = minstrel;
	}
}
