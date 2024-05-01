package com.springboot.testing.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
