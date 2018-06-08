package com.springboot.repositories;

import org.springframework.data.repository.CrudRepository;

import com.springboot.pojos.Topic;

/**
 * CrudRepository is a common repository which contains all general methods for CRUD operations
 *
 */
public interface ITopicRepository extends CrudRepository<Topic, String>	{ //Topic and it's Id column
	
}
