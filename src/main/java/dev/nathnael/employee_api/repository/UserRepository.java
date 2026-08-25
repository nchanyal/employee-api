package dev.nathnael.employee_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.nathnael.employee_api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    public Optional<User> findByEmail(String email);
}
