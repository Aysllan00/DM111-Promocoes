package br.inatel.dm111.api.core.interceptor;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.core.AppErrorCode;
import br.inatel.dm111.persistence.user.UserFirebaseRepository;
import io.jsonwebtoken.impl.*;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Value("${jwt.custom.issuer}")
    private String tokenIssuer;

    private final JwtParser jwtParser;
    private final UserFirebaseRepository repository;

    public AuthInterceptor(JwtParser jwtParser, UserFirebaseRepository repository) {
        this.jwtParser = jwtParser;
        this.repository = repository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var method = request.getMethod();
        var uri = request.getRequestURI();

        if (!isJwtAuthRequired(uri, method)) {
            return true;
        }

        var token = request.getHeader("Token");
        if (!Strings.hasText(token)) {
            log.error("Token has not being provided.");
            throw new ApiException(AppErrorCode.INVALID_CREDENTIALS);
        }

        try {
            var jwt = (DefaultJws) jwtParser.parse(token);
            var payloadClaims = (Map<String, String>) jwt.getPayload();
            var issuer = payloadClaims.get("iss");
            var subject = payloadClaims.get("sub");
            var role = payloadClaims.get("role");
            var tokenPayload = new JwtTokenPayload(issuer, subject, role, method, uri);
            authenticateUser(tokenPayload);

            return true;
        } catch (JwtException e) {
            log.error("Failure to validate the token: ", e);
            throw new ApiException(AppErrorCode.INVALID_CREDENTIALS);
        }
    }

    private boolean isJwtAuthRequired(String uri, String method) {
        if (uri.equals("/dm111/users")) {
            if (method.equals(HttpMethod.POST.name())) {
                return false;
            }
        }

        if (uri.startsWith("/dm111/products")) {
            return true;
        }

        if (uri.contains("/supermarketlist")) {
            return true;
        }

        if (uri.startsWith("/dm111/promocao")) {
            return true;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("Post Handle logic, if necessary.");
    }

    private void authenticateUser(JwtTokenPayload tokenPayload) throws ApiException {
        if (!tokenPayload.issuer().equals(tokenIssuer)) {
            throw new ApiException(AppErrorCode.INVALID_CREDENTIALS);
        }

        try {
            var user = repository.findByEmail(tokenPayload.subject())
                    .orElseThrow(() -> new ApiException(AppErrorCode.INVALID_CREDENTIALS));

            if (!user.getRole().equals(tokenPayload.role())) {
                throw new ApiException(AppErrorCode.INVALID_CREDENTIALS);
            }

            if (tokenPayload.uri().startsWith("/dm111/products")) {
                // Operações relacionadas a produtos
                if (tokenPayload.method().equals(HttpMethod.POST.name()) ||
                        tokenPayload.method().equals(HttpMethod.PUT.name()) ||
                        tokenPayload.method().equals(HttpMethod.DELETE.name())) {
                    if (!user.getRole().equals("ADMIN")) {
                        throw new ApiException(AppErrorCode.PRODUCTS_OPERATION_NOT_ALLOWED);
                    }
                }
            } else if (tokenPayload.uri().startsWith("/dm111/promocao")) {
                // Operações relacionadas a promoções
                if (tokenPayload.method().equals(HttpMethod.POST.name()) ||
                        tokenPayload.method().equals(HttpMethod.PUT.name()) ||
                        tokenPayload.method().equals(HttpMethod.DELETE.name())) {
                    if (!user.getRole().equals("ADMIN")) {
                        throw new ApiException(AppErrorCode.PROMOCOES_OPERATION_NOT_ALLOWED);
                    }
                }
            } else if (tokenPayload.uri().contains("/supermarketlist")) {
                // Operações relacionadas a supermarketlist
                var splitUri = tokenPayload.uri().split("/");
                var pathUserId = splitUri[3];
                if (!user.getId().equals(pathUserId)) {
                    throw new ApiException(AppErrorCode.SUPERMARKET_LIST_OPERATION_NOT_ALLOWED);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.INVALID_CREDENTIALS);
        }
    }

}
