package dev.nathnael.employee_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.nathnael.employee_api.dto.CreateUserDto;
import dev.nathnael.employee_api.service.TokenService;
import dev.nathnael.employee_api.service.impl.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final TokenService tokenService;
    private final UserService userService;

    public AuthController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        String token = tokenService.generateToken(authentication);
        return token;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody CreateUserDto createUserDto) {
        userService.registerUser(createUserDto);
    }
    
}
