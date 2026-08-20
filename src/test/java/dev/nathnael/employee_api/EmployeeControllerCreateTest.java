package dev.nathnael.employee_api;

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

import dev.nathnael.employee_api.dto.CreateEmployeeDto;
import dev.nathnael.employee_api.repository.EmployeeRepository;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerCreateTest {
    
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
    void createEmployee_withValidData_shouldReturn201() throws Exception {

        CreateEmployeeDto employee = new CreateEmployeeDto(
            "John",
            "Doe",
            "john@example.com",
            "IT",
            "Software Engineer",
            new BigDecimal(75000),
            LocalDate.of(2026, 8, 14)
        );

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email")
                    .value("john@example.com"))
                .andExpect(jsonPath("$.department")
                    .value("IT"));

        assertTrue(employeeRepository.findByEmail("john@example.com").isPresent());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void createEmployee_withInvalidJson_shouldReturn400() throws Exception {        

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "firstName": ,
                    "lastName": "Doe",
                    "email": "john@example.com",
                    "department": "IT",
                    "jobTitle": "Software Engineer",
                    "salary": 75000,
                    "hireDate": "2026-08-14"
                }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                    .value("Request body contains invalid JSON."));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void createEmployee_withBlankFirstName_shouldReturn400() throws Exception {

        CreateEmployeeDto employee = new CreateEmployeeDto(
            "",
            "Doe",
            "john@example.com",
            "IT",
            "Software Engineer",
            new BigDecimal(75000),
            LocalDate.of(2026, 8, 14)
        );

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName")
                    .value("must not be blank"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void createEmployee_withLongLastName_shouldReturn400() throws Exception {

        CreateEmployeeDto employee = new CreateEmployeeDto(
            "John",
            "FLbzcBeopyNqaZGYRnuObdgnTJrADhPiHfdyBBFKlVDAahtBvqcX",
            "john@example.com",
            "IT",
            "Software Engineer",
            new BigDecimal(75000),
            LocalDate.of(2026, 8, 14)
        );

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.lastName")
                    .value("size must be between 0 and 50"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void createEmployee_withInvalidEmail_shouldReturn400() throws Exception {

        CreateEmployeeDto employee = new CreateEmployeeDto(
            "John",
            "Doe",
            "not-an-email",
            "IT",
            "Software Engineer",
            new BigDecimal(75000),
            LocalDate.of(2026, 8, 14)
        );

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email")
                    .value("must be a well-formed email address"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void createEmployee_withNullSalary_shouldReturn400() throws Exception {

        CreateEmployeeDto employee = new CreateEmployeeDto(
            "John",
            "Doe",
            "john@example.com",
            "IT",
            "Software Engineer",
            null,
            LocalDate.of(2026, 8, 14)
        );

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.salary")
                    .value("must not be null"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void createEmployee_withNegativeSalary_shouldReturn400() throws Exception {

        CreateEmployeeDto employee = new CreateEmployeeDto(
            "John",
            "Doe",
            "john@example.com",
            "IT",
            "Software Engineer",
            new BigDecimal(-75000),
            LocalDate.of(2026, 8, 14)
        );

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.salary")
                    .value("must be greater than 0"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void createEmployee_withInvalidHireDate_shouldReturn400() throws Exception {

        CreateEmployeeDto employee = new CreateEmployeeDto(
            "John",
            "Doe",
            "john@example.com",
            "IT",
            "Software Engineer",
            new BigDecimal(75000),
            LocalDate.of(2027, 3, 20)
        );

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.hireDate")
                    .value("must be a date in the past or in the present"));
    }

}
