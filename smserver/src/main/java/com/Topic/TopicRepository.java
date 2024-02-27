package com.Topic;

import com.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
// This class communicates with the database to return requested Topic data to TopicDataHandler
public interface TopicRepository extends MongoRepository<Topic, String> {
    Optional<Topic> getByName(String name);
    Optional<ArrayList<Topic>> getTopicsByCreatorName(String creatorName);
    Optional<Topic> getById(int id);
}
