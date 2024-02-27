package com.requestHandler;

import com.Exception.NoSuchDBEntryException;
import com.Topic.Topic;
import com.Topic.TopicDataHandler;
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
@RequestMapping("/searchTopic")
public class searchTopicController {

    TopicDataHandler topicHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity SearchTopicController(@RequestBody String s) {
        //String s looks like: topicName

        Topic searchTopic;
        try {
            searchTopic = topicHandler.getByName(s);
            searchTopic.setTopicPosts(searchTopic.getSortedPosts());
        } //Database Error Checking
        catch (NoSuchDBEntryException e) {
            System.out.println("FAILED searching specific topic.");
            return ResponseEntity.ok("Error: topic not exist.");
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            String json = mapper.writeValueAsString(searchTopic);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
