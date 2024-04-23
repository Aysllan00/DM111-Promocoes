package br.inatel.dm111.api.user.service;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.core.AppErrorCode;
import br.inatel.dm111.api.core.PasswordCrypto;
import br.inatel.dm111.api.user.UserResponse;
import br.inatel.dm111.api.user.controller.UserController;
import br.inatel.dm111.api.user.controller.UserRequest;
import br.inatel.dm111.persistence.user.User;
import br.inatel.dm111.persistence.user.UserFirebaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final PasswordCrypto crypto;
    private final FirebaseCloudMessagePublisher publisher;
    private final UserFirebaseRepository repository;

    public UserService(PasswordCrypto crypto, FirebaseCloudMessagePublisher publisher, UserFirebaseRepository repository) {
        this.crypto = crypto;
        this.publisher = publisher;
        this.repository = repository;
    }

    public List<UserResponse> searchUsers() throws ApiException {
        log.info("Searching the info from all users.");
        try {
            return repository.findAll().stream()
                    .map(this::toUserResponse)
                    .toList();
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.USERS_QUERY_ERROR);
        }
    }

    public UserResponse searchUser(String id) throws ApiException {
        log.info("Searching the info of the user: {}.", id);

        try {
            return repository.findById(id)
                            .map(this::toUserResponse)
                            .orElseThrow(() -> new ApiException(AppErrorCode.USER_NOT_FOUND));
        } catch (ExecutionException | InterruptedException  e) {
            throw new ApiException(AppErrorCode.USERS_QUERY_ERROR);
        }
    }

    public UserResponse createUser(UserRequest request) throws ApiException {
        validateUser(request);
        var user = buildUser(request);
        repository.save(user);

        var published = publisher.publish(user);
        if (!published) {
            System.out.println("Error to publish the message to FCM.");
        }

        return toUserResponse(user);
    }

    public UserResponse updateUser(String id, UserRequest request) throws ApiException {
        validateUser(id, request);
        try {
            var currentUserOpt = repository.findById(id);
            if (currentUserOpt.isPresent()) {
                var currentUser = currentUserOpt.get();
                currentUser.setEmail(request.email());
                currentUser.setName(request.name());
                currentUser.setRole(request.role());
                currentUser.setPassword(crypto.encryptPassword(request.password()));

                repository.save(currentUser);
                return toUserResponse(currentUser);
            } else {
                throw new ApiException(AppErrorCode.USER_NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.USERS_QUERY_ERROR);
        }
    }

    public void deleteUser(String id) throws ApiException {
        try {
            repository.delete(id);
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.USERS_QUERY_ERROR);
        }
    }

    private void validateUser(String id, UserRequest request) throws ApiException {
        try {
            var conflictedUserOpt = repository.findByEmail(request.email());
            if (conflictedUserOpt.isPresent()) {
                var conflictedUser = conflictedUserOpt.get();
                if (!conflictedUser.getId().equals(id)) {
                    throw new ApiException(AppErrorCode.USER_CONFLICT_EMAIL);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.USERS_QUERY_ERROR);
        }
    }

    private void validateUser(UserRequest request) throws ApiException {
        try {
            var conflictedUserOpt = repository.findByEmail(request.email());
            if (conflictedUserOpt.isPresent()) {
                throw new ApiException(AppErrorCode.USER_CONFLICT_EMAIL);
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.USERS_QUERY_ERROR);
        }
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }

    private User buildUser(UserRequest request) throws ApiException {
        var id = UUID.randomUUID().toString();
        return new User(id,
                request.name(),
                request.email(),
                crypto.encryptPassword(request.password()),
                request.role());
    }
}
