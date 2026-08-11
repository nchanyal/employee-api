package dev.nathnael.employee_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    
    @NotBlank
    @Email
    @Size(max = 100)
    public String email;

    @NotBlank
    @Size(min = 8, max = 100, message = "size must be between 8 and 100 characters")
    public String password;
}
