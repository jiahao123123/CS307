package com.requestHandler;

import com.Comment.Comment;
import com.Comment.CommentDataHandler;
import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/removeComment")
public class removeComment {

    private final CommentDataHandler commentHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity removeComment(@RequestBody String s) {
        //String s should look like: commentId, postId

        //This will be used to remove a comment from a post

        String[] parse = s.split(", ");
        Comment toRemove = null;

        try {
            toRemove = commentHandler.getById(Integer.parseInt(parse[0]));
        } catch (NoSuchDBEntryException e) {
            System.out.println("FAILED, could not find the comment in the database");
            return ResponseEntity.ok("Error: Could not find the comment");
        }


        try {
            System.out.println("Remove comment " + s);
            commentHandler.remove(toRemove);
        }
        catch (NoSuchDBEntryException e) {
            System.out.println("FAILED removing comment from database.  " + toRemove.getId() + " }\n" + e);
            return ResponseEntity.ok("Error: Database");
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
