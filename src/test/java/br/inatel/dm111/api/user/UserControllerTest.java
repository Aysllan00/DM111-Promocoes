package br.inatel.dm111.api.user;


import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.user.controller.UserController;
import br.inatel.dm111.api.user.controller.UserRequest;
import br.inatel.dm111.api.user.service.UserService;
import br.inatel.dm111.persistence.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @Test
    void getUsersShouldReturnAListOfUsers() throws ApiException {
        // given
        var expected = buildUsers();
        BDDMockito.given(service.searchUsers()).willReturn(expected);

        // when
        var result = controller.getUsers();

        // then
        Assertions.assertEquals(expected, result.getBody());
    }

    @Test
    void getUsersThrowsApiExceptionDueToSameError() throws ApiException {
        // given
        var expected = buildUsers();
        BDDMockito.given(service.searchUsers()).willThrow(ApiException.class);

        // when
        assertThrows(ApiException.class, () -> controller.getUsers());
    }

    private List<UserResponse> buildUsers() {
        return List.of(buildUser());
    }

    private UserResponse buildUser() {
        return new UserResponse("id","name","email","ADMIN");
    }
}
