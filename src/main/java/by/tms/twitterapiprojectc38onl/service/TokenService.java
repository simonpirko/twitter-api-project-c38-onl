package by.tms.twitterapiprojectc38onl.service;


import by.tms.twitterapiprojectc38onl.controller.AuthResponseDTO;
import by.tms.twitterapiprojectc38onl.entity.Account;
import by.tms.twitterapiprojectc38onl.entity.RefreshToken;
import by.tms.twitterapiprojectc38onl.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Value("${jwt.access.expiration:120000}")
    private long accessExpiration;

    @Value("${jwt.refresh.expiration:604800000}")
    private long refreshExpiration;

    private final String SECRET_KEY = "adhsajkdhaskjdhaskjdasdhashdjashdjhsakjdhaskdhasd";

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private ApplicationContext applicationContext;

    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateAccessToken(String email, Long accountId, List<String> roles) {

        return Jwts.builder()
                .setClaims(Map.of("roles", roles))
                .setSubject(email)
                .claim("accountId", accountId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email, Long accountId, List<String> roles) {

        return Jwts.builder()
                .setSubject(email)
                .claim("accountId", accountId)
                .claim("roles", roles)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build();
            return parser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new InternalAuthenticationServiceException("Token expired");
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException("Invalid token");
        }
    }

    public AuthResponseDTO generateTokens(String email, Account account,
                                          Collection<? extends GrantedAuthority> authorities) {
        refreshTokenRepository.deleteByAccountId(account.getId());

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String accessToken = generateAccessToken(email, account.getId(), roles);
        String refreshToken = generateRefreshToken(email, account.getId(), roles);

        RefreshToken refreshTokenEntity= new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setAccount(account);
        refreshTokenEntity.setExpiryDate(LocalDateTime.now().plusSeconds(refreshExpiration / 1000));

        refreshTokenRepository.save(refreshTokenEntity);

        return new AuthResponseDTO(accessToken, refreshToken);
    }


    public String getUsernameFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        try {
            validateToken(token);
            return false;
        } catch (InternalAuthenticationServiceException e) {
            return e.getMessage().equals("Token expired");
        }
    }

    public void logout(String refreshToken) {
        try {
            Claims claims = validateToken(refreshToken);
            Long accountId = claims.get("accountId", Long.class);

            refreshTokenRepository.deleteByAccountId(accountId);
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException("Logout failed");
        }
    }

    public AuthResponseDTO refreshAccessToken(String refreshToken) {
        try {
            Claims claims = this.validateToken(refreshToken);

            if (!"refresh".equals(claims.get("type"))) {
                throw new InternalAuthenticationServiceException("Invalid token type");
            }

            String tokenId = claims.getId();
            String email = claims.getSubject();
            Long accountId = claims.get("accountId", Long.class);
            List<String> roles = claims.get("roles", List.class);

            refreshTokenRepository.deleteByToken(tokenId);

            String newAccessToken = generateAccessToken(email, accountId, roles);
            String newRefreshToken = generateRefreshToken(email, accountId, roles);

            return new AuthResponseDTO(newAccessToken, newRefreshToken);

        } catch (ExpiredJwtException e) {
            throw new InternalAuthenticationServiceException("Refresh token expired");
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException("Invalid refresh token");
        }
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}
