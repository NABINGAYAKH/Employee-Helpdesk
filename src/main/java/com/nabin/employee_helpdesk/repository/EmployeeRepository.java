package com.nabin.employee_helpdesk.repository;

import com.nabin.employee_helpdesk.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    Employee findByEmail(String email);
}
