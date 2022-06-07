package com.repositories;

import com.models.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICourseRepository extends CrudRepository<Course, String>{ //Course and it's Id
	
	public List<Course> findByTopicId(String topicId);
}
