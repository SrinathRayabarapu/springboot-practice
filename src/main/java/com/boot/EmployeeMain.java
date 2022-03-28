package com.boot;

import com.boot.models.Employee;
import com.boot.repositories.EmployeeDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Hibernate with Spring Example
 *
 * @author Srinath.Rayabarapu
 **/
public class EmployeeMain {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Employee employeeEntity = new Employee();
        //employeeEntity.setId();
        employeeEntity.setName("ABC Updated");
        employeeEntity.setAge(20);
        employeeEntity.setSalary(15000.00d);

        EmployeeDAO employeeDao = (EmployeeDAO) context.getBean("employeeDao");
        employeeDao.saveOrUpdate(employeeEntity);

        List<Employee> empResultList = employeeDao.getEmployees();
        System.out.println(empResultList.get(0).getName());

        //int id = Integer.parseInt(args[0]);
        Employee emp = employeeDao.getEmployeeById(1);
        System.out.println("Employee ::" + emp.getName());

        //int age=Integer.parseInt(args[1]);
        empResultList = employeeDao.getEmployeeAgeWise(20);
        for (Employee emp1 : empResultList) {
            System.out.println("Employe  ::" + emp1.getName());
        }

    }

}