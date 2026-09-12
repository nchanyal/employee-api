package dev.nathnael.employee_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.nathnael.employee_api.dto.CreateEmployeeDto;
import dev.nathnael.employee_api.dto.EmployeeDto;
import dev.nathnael.employee_api.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
        summary = "Create an employee",
        description = "Creates a new employee. Requires ADMIN role."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody CreateEmployeeDto employeeDto){
        EmployeeDto savedEmployeeDto = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployeeDto, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Get an employee",
        description = "Retrieves an employee by ID. Requires authentication."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long employeeId){
        EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }

    @Operation(
        summary = "Get all employees",
        description = "Retrieves a paginated list of employees. Requires authentication."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<Page<EmployeeDto>> getAllEmployees(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "10") int size
    ){
        Page<EmployeeDto> employees = employeeService.getAllEmployees(page, size);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(
        summary = "Update an employee",
        description = "Updates an existing employee by ID. Requires ADMIN role."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(
        @PathVariable("id") Long employeeId, 
        @Valid @RequestBody CreateEmployeeDto updatedEmployeeDto
    ){
        EmployeeDto employeeDto = employeeService.updateEmployee(employeeId, updatedEmployeeDto);
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }

    @Operation(
        summary = "Delete an employee",
        description = "Deletes an employee by ID. Requires ADMIN role."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable("id") Long employeeId){
        employeeService.deleteEmployee(employeeId);
    }
}
