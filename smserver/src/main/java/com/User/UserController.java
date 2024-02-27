package com.User;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
@AllArgsConstructor
// This class listens for URL requests and passes them off to UserDataHandler.
// For now, the url "localhost:<port#>/users" should return all users in the database.
                // I think you can try: "localhost:8080/users"
public class UserController {

    private final UserDataHandler userHandler;

    @GetMapping
    public List<User> fetchAllUsers() {
        return userHandler.getAllUsers();
    }
}
