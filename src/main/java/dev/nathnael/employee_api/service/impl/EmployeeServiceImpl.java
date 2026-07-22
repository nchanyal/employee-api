package dev.nathnael.employee_api.service.impl;

import org.springframework.stereotype.Service;

import dev.nathnael.employee_api.dto.EmployeeDto;
import dev.nathnael.employee_api.entity.Employee;
import dev.nathnael.employee_api.mapper.EmployeeMapper;
import dev.nathnael.employee_api.repository.EmployeeRepository;
import dev.nathnael.employee_api.service.EmployeeService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }
    
}
