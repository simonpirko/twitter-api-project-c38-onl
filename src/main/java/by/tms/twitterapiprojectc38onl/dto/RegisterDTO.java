package by.tms.twitterapiprojectc38onl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @Schema(example = "test@test.com", description = "User email address")
    @NotBlank(message = "The Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(example = "test", description = "User nickname")
    @NotBlank
    private String username;

    @Schema(example = "testtest", description = "User password")
    @Size(min = 8, message = "The password must contain at least 8 characters")
    private String password;
}
