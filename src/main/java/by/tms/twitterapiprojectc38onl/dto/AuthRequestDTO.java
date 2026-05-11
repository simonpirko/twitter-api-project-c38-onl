package by.tms.twitterapiprojectc38onl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDTO {
    @Schema(example = "test@test.com", description = "User email address")
    @NotBlank(message = "The Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(example = "testtest", description = "User password")
    @NotBlank(message = "The Email cannot be blank")
    private String password;
}
