package com.ada.moviesbattle.security.service;


import com.ada.moviesbattle.security.domain.dto.UserDTO;
import com.ada.moviesbattle.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void addUserSuccessfully() {
        UserDTO newUser = new UserDTO("testUser", "password");
        assertDoesNotThrow(() -> userService.addUser(newUser));

        assertNotNull(userRepository.findByUsername("testUser"));
    }

    @Test
    void addUserFailureUserExists() {
        UserDTO newUser = new UserDTO("existingUser", "password");
        userService.addUser(newUser);
        assertThrows(IllegalArgumentException.class, () -> userService.addUser(newUser));
    }

    @Test
    void loadUserByUsernameSuccessfully() {
        UserDTO newUser = new UserDTO("loadUserTest", "password");
        userService.addUser(newUser);

        assertDoesNotThrow(() -> {
            UserDetails userDetails = userService.loadUserByUsername("loadUserTest");
            assertEquals("loadUserTest", userDetails.getUsername());
        });
    }

    @Test
    void loadUserByUsernameFailure() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonExistingUser"));
    }
}
