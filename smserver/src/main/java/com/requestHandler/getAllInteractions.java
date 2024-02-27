package com.requestHandler;

import com.ClientInteractions.ClientInteractions;
import com.Comment.Comment;
import com.Comment.CommentDataHandler;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Profile.Profile;
import com.Reaction.Reaction;
import com.User.User;
import com.User.UserDataHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Controller
@AllArgsConstructor
@RequestMapping("/allInteract")
public class getAllInteractions {

    UserDataHandler userHandler;
    CommentDataHandler commentHandler;
    PostDataHandler postHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity getInteractions(@RequestBody String s) {
        //String s should look like: username

        User u = null;

        try {
            u = userHandler.getByName(s);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: No such user.");
        }

        Profile p = u.getProfile();

        ArrayList<ClientInteractions> ret = new ArrayList<>();

        ArrayList<Integer> allComments = p.getCommentIds();

        ClientInteractions temp = null;
        int postId = 0;

        try {
            for (int i : allComments) {
                postId = commentHandler.getById(i).getPostId();
                temp = new ClientInteractions(postHandler.getById(postId), "Comment");
                ret.add(temp);
            }
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: Could not find the post.");
        }

        ArrayList<Reaction> allReactions = p.getInteractions();

        try {
            for (Reaction r : allReactions) {
                postId = r.getPostId();
                temp = new ClientInteractions(postHandler.getById(postId), r.getType().toString());
                ret.add(temp);
            }
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: Could not find the post.");
        }

        ArrayList<Integer> allSavedPosts = p.getSavedPostIds();

        try {
            for (int in : allSavedPosts) {
                temp = new ClientInteractions(postHandler.getById(in), "Saved");
                ret.add(temp);
            }
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: Could not find the post.");
        }

        Collections.sort(ret, new SortByPost());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String json = "";

        try {
            json = mapper.writeValueAsString(ret);
        } catch (JsonProcessingException e) {
            return ResponseEntity.ok("Error: Could not JSON parse.");
        }

        return ResponseEntity.ok(json);
    }

    class SortByPost implements Comparator<ClientInteractions> {
        public int compare(ClientInteractions a, ClientInteractions b) {
            return a.getPost().getCreationTime().compareTo(b.getPost().getCreationTime());
        }
    }
}
