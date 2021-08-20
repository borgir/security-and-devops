package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 *
 */
public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void create_user_happy_path() throws Exception {

        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    /**
     *
     */
    @Test
    public void create_user_unhappy_path() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("user_test");
        r.setPassword("password");
        r.setConfirmPassword("password_confirmation");
        final ResponseEntity<User> response = userController.createUser(r);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     *
     */
    @Test
    public void find_user_by_id_happy_path() {

        when(encoder.encode("pwd123456")).thenReturn("thisIsHashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("johndoe");
        userRequest.setPassword("pwd123456");
        userRequest.setConfirmPassword("pwd123456");
        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("johndoe", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        final ResponseEntity<User> findUserById = userController.findById(user.getId());
        User userFound = findUserById.getBody();
        assertEquals(200, findUserById.getStatusCodeValue());
        assertEquals(0, userFound.getId());
        assertEquals("johndoe", userFound.getUsername());
        assertEquals("thisIsHashed", userFound.getPassword());

    }

    /**
     *
     */
    @Test
    public void find_user_by_id_unhappy_path() {

        when(encoder.encode("pwd123456")).thenReturn("thisIsHashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("johndoe");
        userRequest.setPassword("pwd123456");
        userRequest.setConfirmPassword("pwd123456");
        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);

        User user = response.getBody();
        assertNotNull(user);

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        ResponseEntity<User> responseFindUser = userController.findById(123L);
        assertNotNull(responseFindUser);
        assertEquals(404, responseFindUser.getStatusCodeValue());

    }

    /**
     *
     */
    @Test
    public void find_user_by_username_happy_path() {

        when(encoder.encode("pwd123456")).thenReturn("thisIsHashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("johndoe");
        userRequest.setPassword("pwd123456");
        userRequest.setConfirmPassword("pwd123456");
        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("johndoe", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        final ResponseEntity<User> sameUser = userController.findById(user.getId());

        User userFound = sameUser.getBody();
        assertEquals(200, sameUser.getStatusCodeValue());
        assertEquals("johndoe", userFound.getUsername());

    }

    /**
     *
     */
    @Test
    public void find_user_by_username_unhappy_path() {

        when(encoder.encode("pwd123456")).thenReturn("thisIsHashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("johndoe");
        userRequest.setPassword("pwd123456");
        userRequest.setConfirmPassword("pwd123456");
        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);

        User user = response.getBody();
        assertNotNull(user);

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        final ResponseEntity<User> sameUser = userController.findById(user.getId());

        User userFound = sameUser.getBody();
        assertEquals(200, sameUser.getStatusCodeValue());
        assertNotEquals("johndoe2", userFound.getUsername());

    }

}
