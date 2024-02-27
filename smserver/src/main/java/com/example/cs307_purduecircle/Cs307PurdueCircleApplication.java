package com.example.cs307_purduecircle;

import com.Profile.Profile;
import com.User.User;
import com.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import com.requestHandler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication

// All packages containing Beans (Controllers, DataHandlers, etc.) must be added
@ComponentScan(basePackages = { "com.requestHandler", "com.User", "com.Profile", "com.Topic", "com.Post", "com.Comment", "com.DbSequencer", "com.Message"})

// All packages containing Repositories must be added
@EnableMongoRepositories(basePackages = { "com.User", "com.Profile", "com.Topic", "com.Post", "com.Comment", "com.Message" })

public class Cs307PurdueCircleApplication {

    public static void main(String[] args) {
        SpringApplication.run(Cs307PurdueCircleApplication.class, args);
    }

}
