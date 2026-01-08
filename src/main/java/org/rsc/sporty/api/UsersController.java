package org.rsc.sporty.api;

import lombok.RequiredArgsConstructor;
import org.rsc.sporty.entity.User;
import org.rsc.sporty.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.created(URI.create("/users/" + savedUser.getId())).body(savedUser);
    }
}
