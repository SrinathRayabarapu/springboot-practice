package com.springboot.repositories;

import org.springframework.data.repository.CrudRepository;

import com.springboot.pojos.Topic;

public interface ITopicRepository extends CrudRepository<Topic, String>	{ //Topic and it's Id column
	
}
