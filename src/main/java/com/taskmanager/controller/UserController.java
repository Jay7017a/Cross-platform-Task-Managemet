package com.taskmanager.controller;

import com.taskmanager.model.User;
import com.taskmanager.model.User.Role;
import com.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET users by role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    // POST register user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String email    = payload.get("email");
        String password = payload.get("password");
        Role role       = payload.get("role") != null ? Role.valueOf(payload.get("role")) : Role.USER;

        User user = userService.createUser(username, email, password, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // PATCH update role (admin only)
    @PatchMapping("/{id}/role")
    public ResponseEntity<User> updateRole(@PathVariable Long id,
                                           @RequestBody Map<String, String> payload) {
        Role newRole = Role.valueOf(payload.get("role"));
        return ResponseEntity.ok(userService.updateUserRole(id, newRole));
    }

    // DELETE user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }
}
