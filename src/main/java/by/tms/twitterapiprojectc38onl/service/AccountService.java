package by.tms.twitterapiprojectc38onl.service;

import by.tms.twitterapiprojectc38onl.dto.RegisterDTO;
import by.tms.twitterapiprojectc38onl.entity.Account;
import by.tms.twitterapiprojectc38onl.entity.Role;
import by.tms.twitterapiprojectc38onl.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(RegisterDTO registerDTO) {
        Account account = new Account();
        account.setPassword(new BCryptPasswordEncoder().encode(registerDTO.getPassword()));
        account.setUsername(registerDTO.getUsername());
        account.setEmail(registerDTO.getEmail());
        account.setRoles(Set.of(Role.ROLE_USER));

        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> byUsername = accountRepository.findByUsername(username);

        if (byUsername.isPresent()) {
            return byUsername.get();
        }

        throw new UsernameNotFoundException("User not found");
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Optional<Account> byEmail = accountRepository.findByEmail(email);

        if (byEmail.isPresent()) {
            return byEmail.get();
        }

        throw new UsernameNotFoundException("User not found");
    }
}
