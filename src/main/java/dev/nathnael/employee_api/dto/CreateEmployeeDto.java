package dev.nathnael.employee_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEmployeeDto(
    String firstName, 
    String lastName, 
    String email, 
    String department, 
    String jobTitle, 
    BigDecimal salary, 
    LocalDate hireDate
) {
}
