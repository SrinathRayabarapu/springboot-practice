package com.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Employee")
public class Employee {

    private int id;
    private String name;
    private int age;
    private double salary;

    @Id
    @GeneratedValue
    @Column(name = "EMP_ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "EMP_NAME", length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String toString() {
        return "Id = " + id + ", Name = " + name + ", Age = " + age
                + ", Salary = " + salary;
    }

}
