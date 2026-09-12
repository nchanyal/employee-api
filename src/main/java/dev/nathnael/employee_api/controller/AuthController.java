package dev.nathnael.employee_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.nathnael.employee_api.dto.CreateUserDto;
import dev.nathnael.employee_api.dto.LoginRequestDto;
import dev.nathnael.employee_api.dto.LoginResponseDto;
import dev.nathnael.employee_api.service.TokenService;
import dev.nathnael.employee_api.service.impl.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final TokenService tokenService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(
            TokenService tokenService, 
            UserService userService, 
            AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), 
                request.getPassword()
            )
        );

        String token = tokenService.generateToken(authentication);
        
        return new ResponseEntity<>(new LoginResponseDto(token), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody CreateUserDto createUserDto) {
        userService.registerUser(createUserDto);
    }
    
}
