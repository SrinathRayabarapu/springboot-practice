package com.springboot.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.pojos.Topic;
import com.springboot.repository.ITopicRepository;

@Service
public class TopicService {
	
	@Autowired
	private ITopicRepository topicRepository;
	
	public List<Topic> getAllTopics(){
		List<Topic> listTopics = new ArrayList<>();
		this.topicRepository.findAll().forEach(listTopics::add);
		return listTopics;
	}

	public Topic getTopic(String topicId) {
		/*
		Optional<Topic> findFirst = this.list.stream().filter(t -> t.getId().equals(topicId)).findFirst();
		if(findFirst.isPresent())
			return findFirst.get();
		return null;
		*/
		return this.topicRepository.findOne(topicId);
	}

	public void addTopic(Topic topic) {
		this.topicRepository.save(topic);
	}

	public void updateTopic(Topic topic, String id) {		
		this.topicRepository.save(topic);
	}

	public void deleteTopic(String id) {
		//this.list.removeIf(t -> t.getId().equals(id));
		this.topicRepository.delete(id);
	}
	
}