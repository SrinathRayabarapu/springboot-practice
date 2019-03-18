package com.springcore.basic;

import org.apache.log4j.Logger;

public class QuizMasterService {
	
	Logger logger = Logger.getLogger(getClass());
	
	private QuizMaster quizMaster;
	
	public QuizMasterService(QuizMaster quizMaster) {
		this.quizMaster = quizMaster;
	}

	public void setQuizMaster(QuizMaster quizMaster) {
		this.quizMaster = quizMaster;
	}

	public void askAQuestion(){
		logger.info("Hurray.... Auto wiring, byName/byType works ....!");
		logger.info(quizMaster.askAQuestion());
	}
}
