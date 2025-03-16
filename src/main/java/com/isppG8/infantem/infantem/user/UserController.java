package com.isppG8.infantem.infantem.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.isppG8.infantem.infantem.user.dto.UserDTO;

import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = this.userService.getAllUsers().stream().map(UserDTO::new).toList();
        return users;
    }

    @PreAuthorize("hasAuthority('admin') or #id == principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return this.userService.getUserById(id)
                .map(user -> ResponseEntity.ok(new UserDTO(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody User user) {
        User createdUser = this.userService.createUser(user);
        return ResponseEntity.ok(new UserDTO(createdUser));
    }

    @PreAuthorize("hasAuthority('admin') or #id == principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        return this.userService.updateUser(id, userDetails)
                .map(user -> ResponseEntity.ok(new UserDTO(userDetails)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id,
            @RequestHeader(name = "Authorization") String token) {

        String jwtId = jwtUtils.getIdFromJwtToken(token.substring(6));

        if (!(jwtId.equals(id.toString()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Not your user"));
        }

        if (userService.deleteUser(id)) {
            return ResponseEntity.ok().body(new MessageResponse("User deleted successfully"));
        }

        // Unreachable code unless some kind of race condition happens
        return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong"));

    }
}
