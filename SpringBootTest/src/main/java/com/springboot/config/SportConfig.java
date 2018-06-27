package com.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.springboot.bean.ICoach;
import com.springboot.components.SwimCoach;
import com.springboot.services.FortuneService;
import com.springboot.services.SadFortuneService;

/**
 * class to configure java based configurations and package scans
 * 
 * @author srayabar
 *
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