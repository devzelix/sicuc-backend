package com.secretariaculturacarabobo.cultistregistration.backend.services;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.UserRequest;
import com.secretariaculturacarabobo.cultistregistration.backend.dtos.UserResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.User;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    public ResponseEntity<?> authenticate(UserRequest userRequest) {
        User user = userRepository.findByUsername(userRequest.getUsername());
        if (user != null) {
            if (BCrypt.checkpw(userRequest.getPassword(), user.getPassword()))
                return ResponseEntity.ok(toUserResponse(user));
            Map<String, String> error = new HashMap<>();
            error.put("message", "Username Or Password Incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Username Or Password Incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    public UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse(user.getId(), user.getUsername());
        return userResponse;
    }

}
