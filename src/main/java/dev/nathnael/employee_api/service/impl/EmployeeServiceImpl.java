package dev.nathnael.employee_api.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dev.nathnael.employee_api.dto.CreateEmployeeDto;
import dev.nathnael.employee_api.dto.EmployeeDto;
import dev.nathnael.employee_api.entity.Employee;
import dev.nathnael.employee_api.exception.ResourceNotFoundException;
import dev.nathnael.employee_api.mapper.EmployeeMapper;
import dev.nathnael.employee_api.repository.EmployeeRepository;
import dev.nathnael.employee_api.service.EmployeeService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(CreateEmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

        Employee savedEmployee = employeeRepository.save(employee);
        
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
            () -> new ResourceNotFoundException("Employee does not exist with given id: " + employeeId)
        );
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public Page<EmployeeDto> getAllEmployees(int pageNumber, int pageSize) {

        PageRequest request = PageRequest.of(pageNumber, pageSize);

        Page<Employee> employees = employeeRepository.findAll(request);

        return employees.map(employee -> EmployeeMapper.mapToEmployeeDto(employee));
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, CreateEmployeeDto updatedEmployeeDto) {
        
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
            () -> new ResourceNotFoundException("Employee does not exist with given id: " + employeeId)
        );

        employee.setFirstName(updatedEmployeeDto.getFirstName());
        employee.setLastName(updatedEmployeeDto.getLastName());
        employee.setEmail(updatedEmployeeDto.getEmail());
        employee.setDepartment(updatedEmployeeDto.getDepartment());
        employee.setJobTitle(updatedEmployeeDto.getJobTitle());
        employee.setSalary(updatedEmployeeDto.getSalary());
        employee.setHireDate(updatedEmployeeDto.getHireDate());

        Employee updatedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if(employee.isEmpty()) throw new ResourceNotFoundException("Employee does not exist with given id: " + employeeId);

        employeeRepository.deleteById(employeeId);
    }
    
}
