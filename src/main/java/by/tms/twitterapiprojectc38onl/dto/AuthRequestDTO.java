package by.tms.twitterapiprojectc38onl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDTO {
    @NotBlank(message = "The Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "The Email cannot be blank")
    private String password;
}
