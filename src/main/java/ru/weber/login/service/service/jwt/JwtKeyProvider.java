package ru.weber.login.service.service.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.weber.login.service.exception.JwtInitializationException;
import ru.weber.login.service.service.jwt.util.Base64Util;
import ru.weber.login.service.service.jwt.util.ResourceUtil;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtKeyProvider {

    private final ResourceUtil resourceUtil;
    private final Base64Util base64Util;

    @Value("${app.private-key.resource-path}")
    private String resourcePath;


    @Getter
    private PrivateKey privateKey;

    @PostConstruct
    public void init() {
        privateKey = readKey(
                "PRIVATE",
                this::privateKeySpec,
                this::privateKeyGenerator
        );
    }

    private <T extends Key> T readKey(String headerSpec, Function<String, EncodedKeySpec> keySpec, BiFunction<KeyFactory, EncodedKeySpec, T> keyGenerator) {
        try {
            String keyString = resourceUtil.asString(resourcePath);
            keyString = cleanKey(keyString, headerSpec);

            return keyGenerator.apply(KeyFactory.getInstance("RSA"), keySpec.apply(keyString));
        } catch(NoSuchAlgorithmException | IOException e) {
            throw new JwtInitializationException(e);
        }
    }

    private EncodedKeySpec privateKeySpec(String data) {
        return new PKCS8EncodedKeySpec(base64Util.decode(data));
    }

    private PrivateKey privateKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
        try {
            return kf.generatePrivate(spec);
        } catch(InvalidKeySpecException e) {
            throw new JwtInitializationException(e);
        }
    }

    private String cleanKey(String keyString, String headerSpec) {
        return keyString
                .replace("-----BEGIN " + headerSpec + " KEY-----", "")
                .replace("-----END " + headerSpec + " KEY-----", "")
                .replaceAll("\\s+", "");
    }

}

