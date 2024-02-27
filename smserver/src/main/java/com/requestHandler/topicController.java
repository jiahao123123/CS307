package com.requestHandler;

import com.Exception.*;
import com.Topic.Topic;
import com.Topic.TopicDataHandler;
import com.User.User;
import com.User.UserDataHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/topic")
public class topicController {

    private final UserDataHandler userHandler;
    private final TopicDataHandler topicHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity createTopicController(@RequestBody String s) {
        //String s looks like: topic_name, creator

        // Uncomment the below things once we can get the creator's name passed in

        String[] input = s.split(", "); //Change when parsing

        User tempUser = null;

        try {
            tempUser = userHandler.getByName(input[1]);
        } catch (NoSuchDBEntryException e) {
            System.out.println(e.getMessage());
        }
        Topic newTopic = new Topic(input[0], tempUser.getUsername()); // Change debugUser once we get the creator's name

        try {
            topicHandler.insert(newTopic);
        } //Database Error Checking
        catch (DBEntryNameAlreadyExistsException | DBEntryIDAlreadyExistsException e) {
            System.out.println("FAILED adding new topic to database.     " + newTopic.getName() + " }\n" + e);
            return ResponseEntity.ok("Error: Topic already exists");
        }

        System.out.println("New Topic: \n\tName: " + newTopic.getName() + "\n\tCreator: " + newTopic.getCreatorName());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            String json = mapper.writeValueAsString(newTopic);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Error: controlling input parsing failed");
        }

        //The client is expecting a JSON form of the newly created topic, or a corresponding error message
        //return ResponseEntity.ok(HttpStatus.OK);
    }
}
