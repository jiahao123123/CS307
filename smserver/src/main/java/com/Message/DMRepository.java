package com.Message;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
// This class communicates with the database to return requested data to UserDataHandler
public interface DMRepository extends MongoRepository<Message, Integer> {
    Optional<Message> getById(int id);
    Optional<ArrayList<Message>> getBySenderIdAndRecipientId(String senderId, String recipientId);
    Optional<ArrayList<Message>> getBySenderIdOrRecipientId(String senderId, String recipientId);
}
