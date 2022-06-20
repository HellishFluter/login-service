package ru.weber.login.service.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.weber.login.service.model.User;
import ru.weber.login.service.service.JwtService;
import ru.weber.login.service.service.jwt.JwtKeyProvider;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final long TTL_IN_DAYS = 2;

    private final JwtKeyProvider jwtKeyProvider;

    @Override
    public String generateToken(User user) {

        return Jwts.builder()
                .setExpiration(createExpiration())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("middleName", user.getMiddleName())
                .signWith(SignatureAlgorithm.RS256, jwtKeyProvider.getPrivateKey())
                .compact();
    }

    private Date createExpiration() {
        return Date.from(LocalDateTime.now().plusDays(TTL_IN_DAYS).atZone(ZoneId.systemDefault()).toInstant());
    }
}
