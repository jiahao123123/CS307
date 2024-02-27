package com.requestHandler;

import com.Exception.DBEntryAlreadyExistsException;
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
import com.Post.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

@Controller
@AllArgsConstructor
@RequestMapping("/post")
public class postController {

    private final PostDataHandler postHandler;
    private final UserDataHandler userDataHandler;
    private final TopicDataHandler topicDataHandler;

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    public ResponseEntity createPostController(@RequestBody String s) { //String s may be changed depending on how we post
        //This "post" class will be used for storing the posts
        //Implementation once we find out how posts are sent

        // String s should look like: "topic_name, post_title, post_info, anonymous, image, creator"
        // Uses JSON.stringify so the quotes at the ends are needed

        System.out.println("Post Send: " + s);

        int length = s.length();

        String s2 = s.substring(1, length - 1);

        System.out.println(s2 + " This is string to parse\n");
        String[] parseArr = s2.split(", ");
        String topicName = parseArr[0];

        String postTitle = parseArr[1];

        String imageString = parseArr[4]; //image for farther usage; probably not string form at the back of server then
                                    //needed to be expanded

        String username = parseArr[5];
        System.out.println(username + " this is username\n");

        boolean isAnonymous = Boolean.parseBoolean(parseArr[3]); // farther parse JSON object to get this value
        Post postToCreate = new Post(username, topicName, postTitle, false, true, isAnonymous);

        System.out.println("Image: " + imageString);
        if (imageString.compareTo("null") != 0) {
            System.out.println("Has image");
            postToCreate.setImage(imageString);
            postToCreate.setHasImage(true);
        }

        if (parseArr[2].length() > 600) {
            return ResponseEntity.ok("Error: Caption too long");
        }
        postToCreate.setCaption(parseArr[2]);

        //before adding post to topic, determine if that topic existed or not
        //if not exist, create the topic so that the post can be posted to it
        try {
            topicDataHandler.getByName(topicName);
        } catch (NoSuchDBEntryException e) {
            System.out.println("Topic doesn't exist yet, trying to insert the topic to the database right now\n");
            Topic topicNewCreate = new Topic(topicName, username);
            try {
                topicDataHandler.insert(topicNewCreate);

            } catch (DBEntryNameAlreadyExistsException | DBEntryIDAlreadyExistsException ie) {
                System.out.println("FAILED adding new topic to database.     " + topicNewCreate.getName() + " }\n" + e);
                return ResponseEntity.ok("Error: Database");
            }
        }

        try{
            postHandler.insert(postToCreate);
        } catch (NoSuchDBEntryException | DBEntryAlreadyExistsException e) {
            System.out.println("FAILED creating new post to database.     " + "in topic  " + postToCreate.getTopicName()
                    + " by  " + postToCreate.getCreatorName() + " }\n" + e);
            return ResponseEntity.ok("Error: Database");
        }

        System.out.println("New Post: \n\tTopic Name: " + postToCreate.getTopicName()
                + "\n\tCreator: " + postToCreate.getCreatorName());

        try {
            Topic topicOfThePost = topicDataHandler.getByName(topicName);
            topicOfThePost.setTopicPosts(topicOfThePost.getSortedPosts());
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            try {
                String json = mapper.writeValueAsString(topicOfThePost);
                System.out.println("ResultingJSONstring = " + json);
                //System.out.println(json);
                return ResponseEntity.ok(json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Something wrong with creating topic to the database");
        }
        return ResponseEntity.ok("Error: no branches triggered");
        //Returns the topic object if good, or a corresponding error message if not good.
    }
}
