package by.tms.twitterapiprojectc38onl.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class RefreshTokenRequestDTO {
    @NotBlank
    private String refreshToken;
}
