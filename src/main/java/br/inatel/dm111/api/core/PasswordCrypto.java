package br.inatel.dm111.api.core;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswordCrypto {

    public String encryptPassword(String password) throws ApiException {
        MessageDigest crypt = null;
        try {
            crypt = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(AppErrorCode.PASSWORD_ENCRYPTION_ERROR);
        }
        crypt.reset();
        crypt.update(password.getBytes(StandardCharsets.UTF_8));

        return new BigInteger(1, crypt.digest()).toString();
    }
}
