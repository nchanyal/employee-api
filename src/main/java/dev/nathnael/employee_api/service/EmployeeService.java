package dev.nathnael.employee_api.service;

import org.springframework.data.domain.Page;

import dev.nathnael.employee_api.dto.CreateEmployeeDto;
import dev.nathnael.employee_api.dto.EmployeeDto;

public interface EmployeeService {
    
    EmployeeDto createEmployee(CreateEmployeeDto employeeDto);

    EmployeeDto getEmployeeById(Long employeeId);

    Page<EmployeeDto> getAllEmployees(int pageNumber, int pageSize);

    EmployeeDto updateEmployee(Long employeeId, CreateEmployeeDto updatedEmployee);

    void deleteEmployee(Long employeeId);
}
