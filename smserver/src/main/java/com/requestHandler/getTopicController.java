package com.requestHandler;

import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Topic.Topic;
import com.Topic.TopicDataHandler;
import com.Topic.TopicRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@AllArgsConstructor
@RequestMapping("/getTopicPosts")
public class getTopicController { //May not need this class if we can use the "get" in postController

    private final TopicDataHandler topicHandler;

    @GetMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity getTopicPosts(@RequestBody String s) {
        //Not used yet, so String s and return value can not be determined yet

        Topic topic = null;
        try {
            topic = topicHandler.getByName(s);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("exception");
        }
        ArrayList<Post> postArrayList = topic.getSortedPosts(); // topic.getTopicPosts();

        Post[] postList = new Post[postArrayList.size()];
        postList = postArrayList.toArray(postList);

        return ResponseEntity.ok(postList);
    }
}
