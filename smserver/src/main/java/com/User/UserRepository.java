package com.User;

import com.Profile.Profile;
import com.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
// This class communicates with the database to return requested data to UserDataHandler
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByProfile(Profile profile);
    Optional<User> findUserByUsername(String name);
    Optional<User> findUserById(String Id);
}