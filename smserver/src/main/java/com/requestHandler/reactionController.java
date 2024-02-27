package com.requestHandler;

import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Reaction.Reaction;
import com.Reaction.ReactionType;
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

@Controller
@AllArgsConstructor
@RequestMapping("/postReactions")

public class reactionController {
    private final PostDataHandler postDataHandler;
    private final UserDataHandler userDataHandler;
    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity postReactions(@RequestBody  String s) { //May be changed, probably not though
        //String s should look like: shouldAddOrRemove(true/false right now), post_id, username, reactionType/SAVE

        //This will be used when we want to add a reaction to a post
        //Implementation will be done once we figure out exactly what we need

        final String SAVE = "SAVE";

        System.out.println(s);
        String[] parse = s.split(", ");
        String username = parse[2];
        Post post;
        User user = null;
        try {
            user = userDataHandler.getByName(username);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("No such user");
        }
        String reactionTypeString;
        boolean shouldAdd;

        // Temporary while reaction is just true/false
        String shouldAddString = parse[0];  // true if reaction/save should be added
                                            // false if it should be removed
        if (shouldAddString.equals("false"))
            shouldAdd = false;
        else if (!shouldAddString.equals("true"))
            return ResponseEntity.ok("Error: passed shouldAdd/Remove is not true/false");
        else
            shouldAdd = true;

        if (!shouldAdd) {
            try {
                post = postDataHandler.getById(Integer.parseInt(parse[1]));
            } catch (NoSuchDBEntryException e) {
                return ResponseEntity.ok("Error: No such post");
            }
            try {
                if (parse[3].compareTo("SAVE") == 0) {
                    userDataHandler.unsavePost(user, post);
                }
            } catch (NoSuchDBEntryException e) {
                return ResponseEntity.ok("Fail to remove saved post");
            }

            return ResponseEntity.ok(HttpStatus.OK); // change this to remove the reaction/save
        }
        // this runs if the client wants to add a reaction or save a post
        reactionTypeString = parse[3];

        System.out.println("Reaction Controller");
        System.out.println(username);
        System.out.println(shouldAddString);
        System.out.println(Integer.parseInt(parse[1]));
        System.out.println(reactionTypeString);

        try {
            post = postDataHandler.getById(Integer.parseInt(parse[1]));
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: No such post");
        }
        try {
            user = userDataHandler.getByName(parse[2]);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: No such creator");
        }
        if (reactionTypeString.equals(SAVE)) {
            try {
                userDataHandler.savePost(user, post);
            } catch (DBEntryAlreadyExistsException e) {
                return ResponseEntity.ok("Error: This post already been saved");
            }
        }
        else {
            try {
                ReactionType reactionType = ReactionType.valueOf(parse[3]);
/*                System.out.println("\n" + reactionType.toString());
                Reaction reaction = new Reaction(reactionType, username, Integer.parseInt(parse[1]));
                user.getProfile().addInteraction(reaction);*/
                postDataHandler.addReaction(post, reactionType, user);
            } catch (Exception e) {
                return ResponseEntity.ok("Error: ReactionType (arg 3) invalid");
            }

        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            String json = mapper.writeValueAsString(user);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Error: controlling input parsing failed");
        }
    }
}
