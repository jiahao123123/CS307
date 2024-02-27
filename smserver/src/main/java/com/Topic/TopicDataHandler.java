package com.Topic;

import com.DbSequencer.IdSequencer;
import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.DBEntryIDAlreadyExistsException;
import com.Exception.DBEntryNameAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import com.User.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
// This class is used to service incoming requests related to Topic data.
// It uses repositories to get or set data and sends the result back to the server code
public class TopicDataHandler {

    private TopicRepository topicRepository;
    private IdSequencer idSequencer;

    public Topic insert(Topic topic) throws DBEntryIDAlreadyExistsException, DBEntryNameAlreadyExistsException {
        if (topic.getId() < IdSequencer.START_ID_VALUE)
            topic.setId(idSequencer.getNextId(IdSequencer.Collection.TOPIC));

        if (topicRepository.getById(topic.getId()).isPresent()) {
            System.out.println("FAILED insert topic to database.  Topic already exists.     " + topic);
            throw new DBEntryIDAlreadyExistsException("Topic with Id: " + topic.getId() + " already exists");
        }

        if (topicRepository.getByName(topic.getName()).isPresent()) {
            System.out.println("FAILED insert topic to database.  Name is taken.     " + topic);
            throw new DBEntryNameAlreadyExistsException("Topic with name: " + topic.getName() + " already exists");
        }

        return topicRepository.insert(topic);
    }

    public void remove(Topic topic) {
        topicRepository.delete(topic);
    }

    public ArrayList<Topic> getTopicsCreatedByUser(User user) {
        ArrayList<Topic> createdTopics;

        Optional<ArrayList<Topic>> createdTopicsOptional = topicRepository.getTopicsByCreatorName(user.getUsername());

        if (createdTopicsOptional.isPresent()) // User has created at least one topic
            createdTopics = createdTopicsOptional.get();
        else
            createdTopics = new ArrayList<>(); // User has created no topics

        return createdTopics;
    }

    public Topic getByName(String name) throws NoSuchDBEntryException {
        Optional<Topic> foundTopicOptional = topicRepository.getByName(name);

        if (foundTopicOptional.isEmpty())
            throw new NoSuchDBEntryException("Topic with name: " + name + " not found");

        return foundTopicOptional.get();
    }

    public Topic save(Topic topic) { return topicRepository.save(topic); }

    public List<Topic> getAll(){ return topicRepository.findAll(); }

    public void deleteAll() {
        topicRepository.deleteAll();
    }

    public boolean exists(Topic topic){
        return topicRepository.getById(topic.getId()).isPresent();
    }
}