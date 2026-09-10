package dev.nathnael.employee_api;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import dev.nathnael.employee_api.config.RsaKeyProperties;
import dev.nathnael.employee_api.entity.Employee;
import dev.nathnael.employee_api.repository.EmployeeRepository;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class EmployeeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeApiApplication.class, args);
	}

	@Bean
	CommandLineRunner seedDatabase(EmployeeRepository employeeRepository) {
		return args -> {
			if (employeeRepository.count() == 0) {
				for(int i = 0; i < 25; i++) {
					Employee employee = new Employee();

					employee.setFirstName("Employee");
					employee.setLastName("Test" + i);
					employee.setEmail("employee" + i + "@example.com");
					employee.setDepartment("IT");
					employee.setJobTitle("Software Engineer");
					employee.setSalary(new BigDecimal("60000.00").add(
							new BigDecimal(i * 100)
					));
					employee.setHireDate(LocalDate.of(2026, 9, 1));

					employeeRepository.save(employee);
				}
			}
		};
	}

}
