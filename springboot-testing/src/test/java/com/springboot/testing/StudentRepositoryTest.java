package com.springboot.testing;

import com.springboot.testing.entities.Student;
import com.springboot.testing.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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

}
