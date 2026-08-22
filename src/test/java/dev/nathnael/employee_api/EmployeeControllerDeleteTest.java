package dev.nathnael.employee_api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import dev.nathnael.employee_api.entity.Employee;
import dev.nathnael.employee_api.repository.EmployeeRepository;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerDeleteTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

    @DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void deleteEmployee_withValidId_shouldReturn204() throws Exception {

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setJobTitle("Developer");
        employee.setSalary(new BigDecimal(75000));
        employee.setHireDate(LocalDate.of(2025, 1, 1));

        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(delete("/api/employees/" + savedEmployee.getId()))
                .andExpect(status().isNoContent());
        
        assertTrue(employeeRepository.findByEmail("john.doe@example.com").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void deleteEmployee_withNonExistentId_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                    .value("Employee does not exist with given id: 999"));
    }
}
