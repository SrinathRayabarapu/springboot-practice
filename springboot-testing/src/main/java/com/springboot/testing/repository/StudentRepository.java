package com.springboot.testing.repository;

import com.springboot.testing.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student getStudentByName(String name);

    @Query(value = "select avg(grade) from student where is_active=true", nativeQuery = true)
    double getAverageGradeOfActiveStudents();

}