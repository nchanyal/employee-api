package dev.nathnael.employee_api.service;

import java.util.List;

import dev.nathnael.employee_api.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDto);

    EmployeeDto getEmployeeById(Long employeeId);

    List<EmployeeDto> getAllEmployees();
}
