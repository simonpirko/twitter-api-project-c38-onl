package by.tms.twitterapiprojectc38onl.controller;

import by.tms.twitterapiprojectc38onl.dto.AuthRequestDTO;
import by.tms.twitterapiprojectc38onl.dto.RegisterDTO;
import by.tms.twitterapiprojectc38onl.entity.Account;
import by.tms.twitterapiprojectc38onl.service.AccountService;
import by.tms.twitterapiprojectc38onl.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        accountService.save(registerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@Valid @RequestBody AuthRequestDTO authDTO) {

        Account account = accountService.loadUserByEmail(authDTO.getEmail());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(authDTO.getPassword(), account.getPassword());

        if (matches) {
            AuthResponseDTO tokens = tokenService.generateTokens(authDTO.getEmail(), account, account.getAuthorities());

            return ResponseEntity.ok(tokens);
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequestDTO refreshRequest) {
        try {
            AuthResponseDTO newTokens = tokenService.refreshAccessToken(refreshRequest.getRefreshToken());
            System.out.println(newTokens);
            return ResponseEntity.ok(newTokens);
        } catch (InternalAuthenticationServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        tokenService.logout(token);
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
