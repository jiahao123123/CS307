package com.DbSequencer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class IdSequencer {

    @Autowired
    private MongoOperations mongoOperations;

    public enum Collection {PROFILE, POST, TOPIC, COMMENT, MESSAGE}
    public static final int START_ID_VALUE = 1;
    public static int lastPostId = 0;
    public static int lastCommentId = 0;

    public int getNextId(Collection collection){
        // create query to find current id entry
        Query query = new Query(Criteria.where("id").is(collection.name()));
        // create update request to increment id
        Update update = new Update().inc("sequenceValue", 1);
        // atomically increment the id and get the new id
        IdSequenceObject idObject = mongoOperations.findAndModify(query, update, options().returnNew(true).upsert(true),
                IdSequenceObject.class);
        return !Objects.isNull(idObject) ? idObject.getSequenceValue() : START_ID_VALUE;
    }
}