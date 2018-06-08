package com.springboot.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.pojos.Course;
import com.springboot.repositories.ICourseRepository;

@Service
public class CourseService {
	
	@Autowired
	private ICourseRepository courseRepository;
	
	public List<Course> getAllCourses(String topicId){
		List<Course> listCourses = new ArrayList<>();
		this.courseRepository.findByTopicId(topicId).forEach(listCourses::add);
		return listCourses;
	}

	public Course getCourse(String courseId) {
		return this.courseRepository.findOne(courseId);
	}

	public void addCourse(Course course) {
		this.courseRepository.save(course);
	}

	public void updateCourse(Course course) {		
		this.courseRepository.save(course);
	}

	public void deleteCourse(String id) {
		this.courseRepository.delete(id);
	}

}
