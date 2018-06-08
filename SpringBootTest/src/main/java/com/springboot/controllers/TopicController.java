package com.springboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.pojos.Topic;
import com.springboot.services.TopicService;

@RestController
public class TopicController {
	
	//dependency injection - keeps singleton object
	@Autowired
	private TopicService topicService; 
	
	//default GET
	@RequestMapping("/topics")
	public List<Topic> getAllTopics() {
		return this.topicService.getAllTopics();
	}

	@RequestMapping("/topics/{topicId}")
	public Topic getAllTopics(@PathVariable String topicId) {
		return this.topicService.getTopic(topicId);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/topics")
	public void addTopic(@RequestBody Topic topic) {
		this.topicService.addTopic(topic);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/topics/{id}")
	public void updateTopic(@RequestBody Topic topic, @PathVariable String id) {
		this.topicService.updateTopic(topic, id);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/topics/{id}")
	public void deleteTopic(@PathVariable String id) {
		this.topicService.deleteTopic(id);
	}
}
