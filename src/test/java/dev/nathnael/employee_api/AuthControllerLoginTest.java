package dev.nathnael.employee_api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import dev.nathnael.employee_api.dto.CreateUserDto;
import dev.nathnael.employee_api.dto.LoginRequestDto;
import dev.nathnael.employee_api.repository.UserRepository;
import dev.nathnael.employee_api.service.impl.UserService;
import tools.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerLoginTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
	private UserRepository userRepository;

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
		userRepository.deleteAll();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

    @Test
    void login_withValidData_shouldReturn200() throws Exception {
        
        CreateUserDto userDto = new CreateUserDto(
            "user@example.com", 
            "password@1"
        );

        userService.registerUser(userDto);

        LoginRequestDto loginRequest = new LoginRequestDto(
            "user@example.com", 
            "password@1"
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login_withNonexistentEmail_shouldReturn401() throws Exception {
        
        CreateUserDto userDto = new CreateUserDto(
            "user@example.com", 
            "password@1"
        );

        userService.registerUser(userDto);

        LoginRequestDto loginRequest = new LoginRequestDto(
            "john@example.com", 
            "password@1"
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                    .value("Invalid username or password."));
    }

    @Test
    void login_withIncorrectPassword_shouldReturn401() throws Exception {
        
        CreateUserDto userDto = new CreateUserDto(
            "user@example.com", 
            "password@1"
        );

        userService.registerUser(userDto);

        LoginRequestDto loginRequest = new LoginRequestDto(
            "user@example.com", 
            "password@123"
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                    .value("Invalid username or password."));
    }

    @Test
    void login_withInvalidEmail_shouldReturn400() throws Exception {
        
        CreateUserDto userDto = new CreateUserDto(
            "user@example.com", 
            "password@1"
        );

        userService.registerUser(userDto);

        LoginRequestDto loginRequest = new LoginRequestDto(
            "not-an-email", 
            "password@1"
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email")
                    .value("must be a well-formed email address"));
    }
}
