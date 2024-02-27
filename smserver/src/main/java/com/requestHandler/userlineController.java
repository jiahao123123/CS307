package com.requestHandler;

import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
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

@Controller
@AllArgsConstructor
@RequestMapping("/userline")
public class userlineController {

    private UserDataHandler userDataHandler;
    private PostDataHandler postHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity getUserline(@RequestBody String s) { //May not need depending on implementation
        //Has not been used yet, so I can't determine the String s or the return value

        //This function will return the posts of the userline, depending on which page they need

        //The String s looks like: "username"
        String username = s;
        User user = null;
        try {
            user = userDataHandler.getByName(username);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: Database: no such user.");
        }

        ArrayList<Post> userLine = userDataHandler.getNonblockedPosts(postHandler.getPostsCreatedByUser(user), user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            String json = mapper.writeValueAsString(userLine);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Error: JSON Parsin");


    }
}
