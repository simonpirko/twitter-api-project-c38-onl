package by.tms.twitterapiprojectc38onl.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class RefreshTokenRequestDTO {
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG1haWwucnUiLCJhY2NvdW50SWQiOjEsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJ0eXBlIjoicmVmcmVzaCIsImlhdCI6MTc3NzY2MTM0MCwiZXhwIjoxNzc4MjY2MTQwfQ.wC9EgX2qjib3qDRwGLy47-y-h-EMsrPmuVmn72RkIwc", description = "The refresh token (We get it after /auth method)")
    @NotBlank
    private String refreshToken;
}
