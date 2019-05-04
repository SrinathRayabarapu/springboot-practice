package com.boot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.models.Course;
import com.boot.models.Topic;
import com.boot.services.CourseService;

@RestController
public class CourseController {
	
	@Autowired
	private CourseService courseService;
	
	//default GET
	@RequestMapping("/topics/{id}/courses")
	public List<Course> getAllCourses(@PathVariable String id) {
		return this.courseService.getAllCourses(id);
	}
	
	@RequestMapping("/topics/{topicId}/courses/{courseId}")
	public Course getCourse(@PathVariable String topicId, @PathVariable String courseId) {
		return this.courseService.getCourse(courseId);
	}
		
	@RequestMapping(method=RequestMethod.POST, value="/topics/{topicId}/courses")
	public void addCourse(@RequestBody Course course, @PathVariable String topicId) {
		course.setTopic(new Topic(topicId, "", ""));
		this.courseService.addCourse(course);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/topics/{topicId}/courses/{courseId}")
	public void updateCourse(@RequestBody Course course, @PathVariable String topicId, @PathVariable String courseId) {
		course.setTopic(new Topic(topicId, "", ""));
		this.courseService.updateCourse(course);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/topics/{topicId}/courses/{courseId}")
	public void deleteCourse(@PathVariable String topicId, @PathVariable String courseId) {
		this.courseService.deleteCourse(courseId);
	}
	
}