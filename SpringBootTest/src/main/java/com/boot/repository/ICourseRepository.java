package com.boot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.boot.model.Course;

public interface ICourseRepository extends CrudRepository<Course, String>{ //Course and it's Id
	
	public List<Course> findByTopicId(String topicId);
}
