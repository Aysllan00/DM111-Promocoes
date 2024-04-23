package br.inatel.dm111.api.user.controller;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.user.UserResponse;
import br.inatel.dm111.api.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//http://localhost:8080/dm111/users/{userId}
@RestController
@RequestMapping("/dm111")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() throws ApiException {
        log.info("Requesting all users...");
        var users = service.searchUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") String id) throws ApiException {
        log.info("Requesting the user {} information.", id);
        var user = service.searchUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> postUser(@RequestBody UserRequest request) throws ApiException {
        var user = service.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> putUser(@PathVariable("id") String id,
                                                @RequestBody UserRequest request) throws ApiException {
        var user = service.updateUser(id, request);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) throws ApiException {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
