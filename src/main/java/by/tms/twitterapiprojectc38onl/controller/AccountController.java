package by.tms.twitterapiprojectc38onl.controller;

import by.tms.twitterapiprojectc38onl.dto.AuthDTO;
import by.tms.twitterapiprojectc38onl.dto.RegisterDTO;
import by.tms.twitterapiprojectc38onl.service.AccountService;
import by.tms.twitterapiprojectc38onl.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> auth(@Valid @RequestBody AuthDTO authDTO) {

        UserDetails userDetails = accountService.loadUserByEmail(authDTO.getEmail());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(authDTO.getPassword(), userDetails.getPassword());

        if (matches) {
            String jwt = tokenService.generateAccessToken(authDTO.getEmail(), userDetails.getAuthorities());

            return ResponseEntity.ok(jwt);
        }

        return ResponseEntity.badRequest().build();
    }

}
