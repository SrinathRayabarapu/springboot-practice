package com.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.core.bean.ICoach;
import com.core.components.SwimCoach;
import com.core.services.FortuneService;
import com.core.services.SadFortuneService;

/**
 * class to configure java based configurations and package scans
 * 
 * @author Srinath.Rayabarapu
 */
@Configuration
//@ComponentScan("com.springboot.components")
@PropertySource("classpath:mySpringData.properties")
public class SportConfig {
	
	//defining bean for sad fortune service
	@Bean
	public FortuneService sadFortuneService() {
		return new SadFortuneService();
	}
	
	//define bean for our swim coach AND inject dependency
	@Bean
	public ICoach swimCoach() {
		return new SwimCoach(sadFortuneService());
	}
	
}