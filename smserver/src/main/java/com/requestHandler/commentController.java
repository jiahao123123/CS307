package com.requestHandler;

import com.Comment.Comment;
import com.Post.Post;
import com.Post.PostRepository;
import com.Topic.Topic;
import com.User.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/postComments")
public class commentController {

    //private final CommentDataHandler commentHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity addNewComment(@RequestBody String s) { //May not need this function
        //This will be used for when we want need to add a comment to a post
        //Implementation will be done once we figure out exactly what we need

        //comment format and length checking if s is comment
        int commentLength = s.length();

        if (commentLength > 500) {
            return ResponseEntity.ok("Error: comment is too long.");
        }
        if (commentLength == 0) {
            return ResponseEntity.ok("Error: comment can't be blank.");
        }


        return ResponseEntity.ok(HttpStatus.OK);
    }
}
