package com.bart.employee.repository;

import com.bart.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllActiveEmployees();
}
