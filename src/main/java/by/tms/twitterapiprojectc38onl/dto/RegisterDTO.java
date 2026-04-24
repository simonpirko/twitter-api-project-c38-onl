package by.tms.twitterapiprojectc38onl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank(message = "The Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    private String username;

    @NotBlank(message = "The password cannot be blank")
    @Size(min = 8, message = "The password must contain at least 8 characters")
    private String password;
}
