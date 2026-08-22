package dev.nathnael.employee_api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import dev.nathnael.employee_api.entity.Employee;
import dev.nathnael.employee_api.repository.EmployeeRepository;
import tools.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerUpdateTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void updateEmployee_withValidIdAndData_shouldReturn200() throws Exception {

        Employee employee1 = new Employee();
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setEmail("john@example.com");
        employee1.setDepartment("IT");
        employee1.setJobTitle("Developer");
        employee1.setSalary(new BigDecimal(75000));
        employee1.setHireDate(LocalDate.of(2025, 1, 1));

        Employee employee2 = new Employee();
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setEmail("jane@example.com");
        employee2.setDepartment("HR");
        employee2.setJobTitle("Manager");
        employee2.setSalary(new BigDecimal(80000));
        employee2.setHireDate(LocalDate.of(2025, 2, 1));

        Employee savedEmployee = employeeRepository.save(employee1);

        mockMvc.perform(put("/api/employees/" + savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedEmployee.getId()))
                .andExpect(jsonPath("$.firstName").value(employee2.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(employee2.getLastName()))
                .andExpect(jsonPath("$.email").value(employee2.getEmail()));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void updateEmployee_withNonExistentId_shouldReturn404() throws Exception {

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setJobTitle("Developer");
        employee.setSalary(new BigDecimal(75000));
        employee.setHireDate(LocalDate.of(2025, 1, 1));

        mockMvc.perform(put("/api/employees/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                    .value("Employee does not exist with given id: 999"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void updateEmployee_withBlankFirstName_shouldReturn400() throws Exception {

        Employee employee1 = new Employee();
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setEmail("john@example.com");
        employee1.setDepartment("IT");
        employee1.setJobTitle("Developer");
        employee1.setSalary(new BigDecimal(75000));
        employee1.setHireDate(LocalDate.of(2025, 1, 1));

        Employee employee2 = new Employee();
        employee2.setFirstName("");
        employee2.setLastName("Smith");
        employee2.setEmail("jane@example.com");
        employee2.setDepartment("HR");
        employee2.setJobTitle("Manager");
        employee2.setSalary(new BigDecimal(80000));
        employee2.setHireDate(LocalDate.of(2025, 2, 1));

        Employee savedEmployee = employeeRepository.save(employee1);

        mockMvc.perform(put("/api/employees/" + savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName")
                    .value("must not be blank"));
    }
}
