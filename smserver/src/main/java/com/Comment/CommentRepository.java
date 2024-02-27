package com.Comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
// This class communicates with the database to return requested data to CommentDataHandler
public interface CommentRepository extends MongoRepository<Comment, Integer> {
    Optional<ArrayList<Comment>> getByPostId(int postId);
    Optional<Comment> getById(int id);
}
