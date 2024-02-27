package com.requestHandler;

import com.Exception.DBEntryIDAlreadyExistsException;
import com.Exception.DBEntryNameAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
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

import java.util.ArrayList;
import java.util.Objects;

@Controller
@AllArgsConstructor
@RequestMapping("/followedTopic")
public class addFollowedTopic {

    private final TopicDataHandler topicHandler;
    private final UserDataHandler userDataHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity addFollowedTopic(@RequestBody String s) {
        //This will be used when we need to store a new followed topic
        //Implementation will be added once we figure out exactly what we need to do

        //String s looks like: "username, topicName, true/false"
        //Does use JSON.stringify, so quotes around the 3 are passed in and needed
        System.out.println(s);
        String s2 = s.substring(1, s.length() - 1);

        String[] parseArr = s2.split(", ");
        String username = parseArr[0];
        String topicName = parseArr[1];
        String followOrNot = parseArr[2];
        User user;
        Topic topicToAddOrRemoved;
        try {
            user = userDataHandler.getByName(username);
            topicToAddOrRemoved = topicHandler.getByName(topicName);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: database");
        }
        if (Objects.equals(followOrNot, "true")) {
            user.getProfile().addFollowingTopic(topicToAddOrRemoved);
        } else if (Objects.equals(followOrNot, "false")) {
            user.getProfile().removeFollowingTopic(topicToAddOrRemoved);
        }
        else {
            // this should return an error message because the request is malformed
            return ResponseEntity.ok("error, request is malformed");
        }

        // updates the user database entry now that the followed topics have been changed
        userDataHandler.save(user);
        //new user data structure
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            String json = mapper.writeValueAsString(user);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Error: Json Parsing");
    }
}
