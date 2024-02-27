package com.requestHandler;

import com.Comment.Comment;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Topic.Topic;
import com.Topic.TopicDataHandler;
import com.User.User;
import com.sun.net.httpserver.HttpServer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@AllArgsConstructor
@RequestMapping("/removeReaction")
public class removeReaction {

    private final PostDataHandler postHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    // String s needs:
        // 1 - the id of the post that the reaction is on
        // 2 - the creatorName
    public ResponseEntity removeReaction(@RequestBody String s) {
        //String s should look like: username, post_id

        //This function will be used when we need to remove a reaction from a post
        //Implementation will be added once we figure out what we need
        String[] parse = s.split(", ");
        String username = parse[0];
        int postId = Integer.parseInt(parse[1]);
        Post post;
        try {
            post = postHandler.getById(postId);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Database");
        }

        // remove the reaction of specific post
        postHandler.removeReaction(post, username);

        //Client really isn't looking for anything, so just send "OK" if it works, or "Error" or something if it doesn't.
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
