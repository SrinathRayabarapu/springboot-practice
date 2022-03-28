package com.boot.repositories;

import com.boot.models.Employee;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EmployeeDAO {

    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @SuppressWarnings("unchecked")
    public List<Employee> getEmployees() {

        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) {

                List<Employee> employeeEntityList = new ArrayList<>();
                Query query = session.createQuery("from Employee");
                query.setMaxResults(2);
                List empList = query.list();

                for (Iterator it = empList.iterator(); it.hasNext(); ) {
                    Employee emp = (Employee) it.next();
                    employeeEntityList.add(emp);
                    System.out.println(emp.getName());
                }
                return employeeEntityList;
            }
        };
        return (ArrayList<Employee>) hibernateTemplate.execute(callback);
    }

    public void saveOrUpdate(final Employee employeeEntity) {

        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                session.saveOrUpdate(employeeEntity);
                return null;
            }
        };
        hibernateTemplate.execute(callback);
    }

    public Employee getEmployeeById(final int empId) throws DataAccessException {
        Employee emp = null;
        List<Employee> empList = new ArrayList<>();
        empList = (ArrayList<Employee>) this.hibernateTemplate.find("from Employee emp where emp.id=?", empId);
        if (empList.size() > 0 && empList.get(0) != null) {
            emp = (Employee) empList.get(0);
        }
        return emp;
    }

    public List<Employee> getEmployeeAgeWise(final int age) throws DataAccessException {

        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(Employee.class);
                criteria.add(Restrictions.gt("age", new Integer(age)));
                return criteria.list();
            }
        };
        return (List<Employee>) hibernateTemplate.execute(callback);
    }

}