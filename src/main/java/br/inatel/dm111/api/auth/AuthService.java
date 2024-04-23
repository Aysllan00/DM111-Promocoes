package br.inatel.dm111.api.auth;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.core.AppErrorCode;
import br.inatel.dm111.api.core.PasswordCrypto;
import br.inatel.dm111.persistence.user.User;
import br.inatel.dm111.persistence.user.UserFirebaseRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Service
public class AuthService {

    @Value("${jwt.custom.issuer}")
    private String tokenIssuer;

    private final PrivateKey privateKey;
    private final PasswordCrypto crypto;
    private final UserFirebaseRepository repository;

    public AuthService(PrivateKey privateKey, PasswordCrypto crypto, UserFirebaseRepository repository) {
        this.privateKey = privateKey;
        this.crypto = crypto;
        this.repository = repository;
    }

    public AuthResponse authUser(AuthRequest request) throws ApiException {
        try {
            var user = repository.findByEmail(request.email())
                    .orElseThrow(() -> new ApiException(AppErrorCode.INVALID_CREDENTIALS));
            var encryptedPassword = crypto.encryptPassword(request.password());
            if (encryptedPassword.equals(user.getPassword())) {
                var token = generateToken(user);
                return new AuthResponse(token, user.getId());
            } else {
                throw new ApiException(AppErrorCode.INVALID_CREDENTIALS);
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.USERS_QUERY_ERROR);
        }
    }

    private String generateToken(User user) {
        var now = Instant.now();
        return Jwts.builder()
                .issuer(tokenIssuer)
                .subject(user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(300)))
                .signWith(privateKey)
                .compact();
    }
}
