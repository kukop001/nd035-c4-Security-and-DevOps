package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class UserControllerTest {
	
	private UserController userController;
	
    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }
	
    @Test
    public void testCreateUserSuccess() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("testPassword");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("testPassword");
        userRequest.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }
    
    @Test
    public void testCreateUserWithConfirmPasswordInvalid() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("testP");
        userRequest.setConfirmPassword("testP");
        ResponseEntity<?> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }
    
    @Test
    public void testFindByUserId() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        ResponseEntity<User> userReponse = userController.findById(1l);
        assertEquals(200, userReponse.getStatusCodeValue());
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        when(userRepository.findByUsername("test")).thenReturn(user);
        ResponseEntity<User> userReponse = userController.findByUserName("test");
        assertEquals(200, userReponse.getStatusCodeValue());
    }
	
	

}
