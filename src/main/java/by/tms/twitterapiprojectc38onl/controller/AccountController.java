package by.tms.twitterapiprojectc38onl.controller;

import by.tms.twitterapiprojectc38onl.dto.AuthRequestDTO;
import by.tms.twitterapiprojectc38onl.dto.RegisterDTO;
import by.tms.twitterapiprojectc38onl.entity.Account;
import by.tms.twitterapiprojectc38onl.service.AccountService;
import by.tms.twitterapiprojectc38onl.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Accounts")
@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "To add new account", description = "This method doesnt return data")
    @PostMapping("/register")
    ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        accountService.save(registerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "To sign in", description = "This method return refresh and access tokens")
    @PostMapping("/auth")
    public ResponseEntity<AuthResponseDTO> auth(@Valid @RequestBody AuthRequestDTO authDTO) {

        Account account = accountService.loadUserByEmail(authDTO.getEmail());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(authDTO.getPassword(), account.getPassword());

        if (matches) {
            AuthResponseDTO tokens = tokenService.generateTokens(authDTO.getEmail(), account, account.getAuthorities());

            return ResponseEntity.ok(tokens);
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Refresh access token", description = "This method is needed when the access token is expired and we need update it")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO refreshRequest) {

        return tokenService.refreshAccessToken(refreshRequest.getRefreshToken())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @Operation(summary = "To logout", description = "This method remove refresh token from DB")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        tokenService.logout(token);
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
