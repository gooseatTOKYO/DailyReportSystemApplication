package com.techacademy.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.UserDetail;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findByReportDateAndEmployee(LocalDate reportDate,Employee employee);
    List<Report> findByEmployee(Employee employee);
}

