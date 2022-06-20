package ru.weber.login.service.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.weber.login.service.model.User;
import ru.weber.login.service.service.JwtService;
import ru.weber.login.service.service.jwt.JwtKeyProvider;
import ru.weber.login.service.service.jwt.util.ResourceUtil;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
class JwtServiceImplTest {
    private JwtService jwtService;
    @MockBean
    private JwtKeyProvider jwtKeyProvider;

    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Smith";

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        when(jwtKeyProvider.getPrivateKey()).thenReturn(generateTestPrivateKey());
        jwtService = new JwtServiceImpl(jwtKeyProvider);
    }

    @Test
    void generateToken() {
        String token = jwtService.generateToken(User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build());
        assertNotNull(token);
        String decodedToken = decodeTokenForChecking(token);
        assertTrue(decodedToken.contains(FIRST_NAME));
        assertTrue(decodedToken.contains(LAST_NAME));
    }

    private PrivateKey generateTestPrivateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        return pair.getPrivate();
    }

    private String decodeTokenForChecking(String token) {
        String mainTokenPart = token.split("\\.")[1];
        return new String(Base64.getUrlDecoder().decode(mainTokenPart.getBytes(StandardCharsets.UTF_8)));
    }
}