package dev.nathnael.employee_api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
public class EmployeeControllerGetTest {
    
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
    void getEmployeeById_withValidId_shouldReturn200() throws Exception {

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setJobTitle("Developer");
        employee.setSalary(new BigDecimal(75000));
        employee.setHireDate(LocalDate.of(2025, 1, 1));

        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(get("/api/employees/" + savedEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedEmployee.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void getEmployeeById_withNonexistentId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                    .value("Employee does not exist with given id: 999"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void getAllEmployees_withFirstPage_shouldReturn200() throws Exception {

        List<Employee> employees = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {
            Employee employee = new Employee();
            employee.setFirstName("Employee");
            employee.setLastName("Test" + i);
            employee.setEmail("employee" + i + "@example.com");
            employee.setDepartment("IT");
            employee.setJobTitle("Software Engineer");
            employee.setSalary(new BigDecimal(60000 + i * 100));
            employee.setHireDate(LocalDate.now());

            employees.add(employee);
        }

        employeeRepository.saveAll(employees);

        mockMvc.perform(get("/api/employees?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.content.length()").value(10))
                .andExpect(jsonPath("$.content[0].lastName").value("Test1"))
                .andExpect(jsonPath("$.content[1].lastName").value("Test2"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void getAllEmployees_withSecondPage_shouldReturn200() throws Exception {

        List<Employee> employees = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {
            Employee employee = new Employee();
            employee.setFirstName("Employee");
            employee.setLastName("Test" + i);
            employee.setEmail("employee" + i + "@example.com");
            employee.setDepartment("IT");
            employee.setJobTitle("Software Engineer");
            employee.setSalary(new BigDecimal(60000 + i * 100));
            employee.setHireDate(LocalDate.now());

            employees.add(employee);
        }

        employeeRepository.saveAll(employees);

        mockMvc.perform(get("/api/employees?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10))
                .andExpect(jsonPath("$.content[0].lastName").value("Test11"))
                .andExpect(jsonPath("$.content[1].lastName").value("Test12"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void getAllEmployees_withLastPage_shouldReturn200() throws Exception {

        List<Employee> employees = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {
            Employee employee = new Employee();
            employee.setFirstName("Employee");
            employee.setLastName("Test" + i);
            employee.setEmail("employee" + i + "@example.com");
            employee.setDepartment("IT");
            employee.setJobTitle("Software Engineer");
            employee.setSalary(new BigDecimal(60000 + i * 100));
            employee.setHireDate(LocalDate.now());

            employees.add(employee);
        }

        employeeRepository.saveAll(employees);

        mockMvc.perform(get("/api/employees?page=2&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.content[0].lastName").value("Test21"))
                .andExpect(jsonPath("$.content[1].lastName").value("Test22"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void getAllEmployees_withEmptyPage_shouldReturn200() throws Exception {

        List<Employee> employees = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {
            Employee employee = new Employee();
            employee.setFirstName("Employee");
            employee.setLastName("Test" + i);
            employee.setEmail("employee" + i + "@example.com");
            employee.setDepartment("IT");
            employee.setJobTitle("Software Engineer");
            employee.setSalary(new BigDecimal(60000 + i * 100));
            employee.setHireDate(LocalDate.now());

            employees.add(employee);
        }

        employeeRepository.saveAll(employees);

        mockMvc.perform(get("/api/employees?page=3&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empty").value(true))
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void getAllEmployees_withLetterAsParameter_shouldReturn200() throws Exception {

        mockMvc.perform(get("/api/employees?page=a&size=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                    .value("Invalid parameter type."));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void getAllEmployees_withDecimalAsParameter_shouldReturn200() throws Exception {

        mockMvc.perform(get("/api/employees?page=1.5&size=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                    .value("Invalid parameter type."));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void getAllEmployees_withNegativePage_shouldReturn200() throws Exception {

        mockMvc.perform(get("/api/employees?page=-1&size=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                    .value("Page index must not be less than zero"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void getAllEmployees_withZeroSize_shouldReturn200() throws Exception {

        mockMvc.perform(get("/api/employees?page=0&size=0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                    .value("Page size must not be less than one"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void getAllEmployees_withNoStoredEmployees_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

}
