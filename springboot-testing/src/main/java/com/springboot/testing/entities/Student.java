package com.springboot.testing.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
@Builder
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private boolean isActive;

    private int grade;

    public Student(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}