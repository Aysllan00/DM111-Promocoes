package br.inatel.dm111.api.auth;

import br.inatel.dm111.api.core.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dm111")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> postAuth(@RequestBody AuthRequest request)
            throws ApiException {
        AuthResponse auth = service.authUser(request);
        return ResponseEntity.ok(auth);
    }
}
