package com.springboot.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.springboot.pojos.Course;

public interface ICourseRepository extends CrudRepository<Course, String>{ //Course and it's Id
	
	public List<Course> findByTopicId(String topicId);
}
