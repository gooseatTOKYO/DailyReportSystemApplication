package com.techacademy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.UserDetail;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

}