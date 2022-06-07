package com.repositories;

import com.models.Topic;
import org.springframework.data.repository.CrudRepository;

/**
 * CrudRepository is a common repository which contains all general methods for CRUD operations
 *
 */
public interface ITopicRepository extends CrudRepository<Topic, String>	{ //Topic and it's Id column
	
}
