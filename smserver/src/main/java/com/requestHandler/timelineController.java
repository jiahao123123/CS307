package com.requestHandler;

import com.Exception.NoSuchDBEntryException;
import com.Post.PostDataHandler;
import com.Topic.TopicDataHandler;
import com.User.User;
import com.User.UserDataHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/timeline")
public class timelineController {
    private final UserDataHandler userHandler;
    private final PostDataHandler postHandler;
    @PostMapping
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity getTimelineController(@RequestBody String s) {
        //The String s looks like: username
        User tempUser = null;
        try {
            tempUser = userHandler.getByName(s);
        } catch (NoSuchDBEntryException e) {
            System.out.println(e.getMessage());
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            String json = mapper.writeValueAsString(userHandler.getNonblockedPosts(tempUser.timelinePosts(userHandler, postHandler), tempUser));
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //The client is looking for a list of posts (in timeline order) to show on the userline page for a specific user
        return ResponseEntity.ok("Error: JSON Parsing");
    }
}
