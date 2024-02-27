package com.requestHandler;

import com.Comment.Comment;
import com.Comment.CommentDataHandler;
import com.DbSequencer.IdSequencer;
import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/addComment")
public class addCommentController {
    private final CommentDataHandler commentHandler;
    private IdSequencer idSequencer;
    @PostMapping
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity addComment(@RequestBody String s) {
        //String s looks like: commentText, creatorName, postId
        //Does not use JSON.stringify

        String[] parse = s.split(", ");
        Comment newComment = new Comment(parse[0], parse[1], Integer.parseInt(parse[2]));

        System.out.println("New comment: " + s);
        try {
            commentHandler.insert(newComment);
        }
        catch (NoSuchDBEntryException | DBEntryAlreadyExistsException e) {
            System.out.println("FAILED creating new comment to database.  " + newComment.getCreatorName() + " }\n" + e);
            return ResponseEntity.ok("Error: Database");
        }
        //Once we get the posts correct, I will put what the string is sent as

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
