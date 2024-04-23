package br.inatel.dm111.config;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.*;

@Configuration
public class JwtConfig {

    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        return keyGen.generateKeyPair();
    }

    @Bean
    public PrivateKey privateKey(KeyPair keyPair) {
        return keyPair.getPrivate();
    }

    @Bean
    public PublicKey publicKey(KeyPair keyPair) {
        return keyPair.getPublic();
    }

    @Bean
    public JwtParser jwtParser(PublicKey publicKey) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build();
    }
}
