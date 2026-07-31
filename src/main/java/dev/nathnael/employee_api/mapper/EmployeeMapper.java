package dev.nathnael.employee_api.mapper;

import dev.nathnael.employee_api.dto.EmployeeDto;
import dev.nathnael.employee_api.entity.Employee;

public class EmployeeMapper {
    
    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        return new EmployeeDto(
            employee.getId(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getEmail(),
            employee.getDepartment(),
            employee.getJobTitle(),
            employee.getSalary(),
            employee.getHireDate()
        );
    }

    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();

        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setDepartment(employeeDto.getDepartment());
        employee.setJobTitle(employeeDto.getJobTitle());
        employee.setSalary(employeeDto.getSalary());
        employee.setHireDate(employeeDto.getHireDate());
        
        return employee;
    }
}
