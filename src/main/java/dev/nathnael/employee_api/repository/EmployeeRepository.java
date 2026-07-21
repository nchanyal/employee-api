package dev.nathnael.employee_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.nathnael.employee_api.entity.Employee;

public interface EmployeeRepository extends JpaRepository <Employee, Long> {
    
}
