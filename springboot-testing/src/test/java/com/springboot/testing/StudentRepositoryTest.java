package com.springboot.testing;

import com.springboot.testing.entities.Student;
import com.springboot.testing.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testSaveStudent(){

        // given
        testEntityManager.persistAndFlush(new Student(null, "Srinath"));

        // when
        Student student = studentRepository.getStudentByName("Srinath");

        // then
        then(student.getId()).isNotNull();
        then(student.getName()).isEqualTo("Srinath");
    }

    @Test
    void testActiveStudentsAverageGrade(){
        // given
        Student srinath = Student.builder().name("Srinath").grade(100).isActive(true).build();
        Student shubha = Student.builder().name("Shubha").grade(80).isActive(true).build();
        Student viyansh = Student.builder().name("Viyansh").grade(120).isActive(true).build();
        Student hitharsh = Student.builder().name("Hitharsh").grade(180).isActive(false).build();

        Arrays.asList(srinath, shubha, viyansh, hitharsh).forEach(testEntityManager::persistAndFlush);

        // when
        double value = studentRepository.getAverageGradeOfActiveStudents();

        // then
        then(value).isNotZero();
        then(value).isEqualTo(100);
    }

}
