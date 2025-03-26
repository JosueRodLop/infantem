package com.isppG8.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.isppG8.infantem.infantem.InfantemApplication;
import com.isppG8.infantem.infantem.user.User;
import com.isppG8.infantem.infantem.user.UserRepository;
import com.isppG8.infantem.infantem.user.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = { InfantemApplication.class, UserService.class })
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
@Transactional
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    private User u1;
    private User u2;

    @BeforeEach
    public void setUp() {
        u1 = new User();
        u1.setId(1);
        u1.setUsername("user1");
        u1.setName("Test");
        u1.setSurname("User");
        u1.setPassword("password1");
        u1.setEmail("user1@example.com");
        
        u2 = new User();
        u2.setId(2);
        u2.setUsername("user2");
        u2.setName("Test");
        u2.setSurname("User");
        u2.setPassword("password2");
        u2.setEmail("user2@example.com");
        userRepository.save(u1);
        userRepository.save(u2);
        userService.login("user1", "password1");
    }

    @Test
    public void TestGetAllUsers() {
        
        List<User> users = userService.getAllUsers();
        assertFalse(users.isEmpty());
        assertTrue(users.contains(userRepository.findByUsername("user1").get()));
    }

    @Test
    public void TestFindByUsername() {
        
        User user = userService.findByUsername("user1");
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }

    @Test
    public void TestFindByUsernameNotFound() {
        
        User user = userService.findByUsername("NonExistentUser");
        assertNull(user);
    }

    @Test
    public void TestCreateUser() {
        User newUser = new User();
        newUser.setUsername("user3");
        newUser.setName("Test");
        newUser.setSurname("User");
        newUser.setPassword("password3");
        newUser.setEmail("newEmail@test.com");

       
        User saved = userService.createUser(newUser);
        assertNotNull(saved);
        assertEquals("newEmail@test.com", saved.getEmail());
    }

    @Test
    public void TestSaveUser() {
        User u = new User();
        u.setUsername("user4");
        u.setName("Test");
        u.setSurname("User");
        u.setPassword("password4");
        u.setEmail("newEmail@test.com");

        User saved = userService.save(u);
        assertNotNull(saved);
        assertEquals("newEmail@test.com", saved.getEmail());
    }

    @Test
    public void TestSaveUser_InvalidUsername() {
        User u = new User();
        u.setUsername("user1");
        u.setName("Test");
        u.setSurname("User");
        u.setPassword("password4");
        u.setEmail("newEmail@test.com");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.save(u);
        });
    }

    @Test
    public void TestSaveUser_InvalidEmail() {
        User u = new User();
        u.setUsername("user4");
        u.setName("Test");
        u.setSurname("User");
        u.setPassword("password4");
        u.setEmail("user1@example.com");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.save(u);
        });
    }

    @Test
    public void TestFindCurrentUser() {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("user1", "password")
        );

        User user = userService.findCurrentUser();
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }

    @Test
    public void TestUpdateUser() {
        User updatedDetails = new User();
        updatedDetails.setName("Updated");
        updatedDetails.setSurname("User");
        updatedDetails.setUsername("updatedUsername");
        updatedDetails.setEmail("updated@example.com");

        User updatedUser = userService.updateUser((long)u1.getId(), updatedDetails);

        assertNotNull(updatedUser);
        assertEquals("Updated", updatedUser.getName());
        assertEquals("updatedUsername", updatedUser.getUsername());
        assertEquals("updated@example.com", updatedUser.getEmail());
    }

    @Test
    public void TestDeleteUser() {
        boolean result = userService.deleteUser((long)u1.getId());
        assertTrue(result);
    }

    @Test
    public void TestDeleteUserNotFound() {
        boolean result = userService.deleteUser((long)100);
        assertFalse(result);
    }
    
}
