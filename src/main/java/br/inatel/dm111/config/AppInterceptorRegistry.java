package br.inatel.dm111.config;

import br.inatel.dm111.api.core.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AppInterceptorRegistry implements WebMvcConfigurer {

    private static final List<String> ENDPOINTS_PATTERN = Arrays.asList(
            "/dm111/users**",
            "/dm111/users/**",
            "/dm111/products**",
            "/dm111/products/**",
            "/dm111/promocao**",
            "/dm111/promocao/**",
            "/dm111/users/*/supermarketlist**",
            "/dm111/users/*/supermarketlist/**",
            "/dm111/users/*/promocao**",
            "/dm111/users/*/promocao/**"
    );

    private final AuthInterceptor authInterceptor;

    public AppInterceptorRegistry(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns(ENDPOINTS_PATTERN);
    }
}
