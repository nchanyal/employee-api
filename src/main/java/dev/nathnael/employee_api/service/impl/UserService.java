package dev.nathnael.employee_api.service.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.nathnael.employee_api.dto.CreateUserDto;
import dev.nathnael.employee_api.entity.User;
import dev.nathnael.employee_api.entity.UserRole;
import dev.nathnael.employee_api.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        
        return user.get();
    }

    public void registerUser(CreateUserDto createUserDto) {
        
        String hashedPassword = passwordEncoder.encode(createUserDto.getPassword());

        User user = User.builder()
                        .email(createUserDto.getEmail())
                        .password(hashedPassword)
                        .role(UserRole.EMPLOYEE)
                        .build();
        
        userRepository.save(user);
    }
}
