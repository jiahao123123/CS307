package com.Post;

import com.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
// This class communicates with the database to return requested data to PostDataHandler
public interface PostRepository extends MongoRepository<Post, Integer> {
    Optional<ArrayList<Post>> getPostsByCreatorName(String creatorName);
    Optional<Post> getById(Integer id);
}
