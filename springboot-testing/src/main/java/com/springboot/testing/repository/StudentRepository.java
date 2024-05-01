package com.springboot.testing.repository;

import com.springboot.testing.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student getStudentByName(String name);

}