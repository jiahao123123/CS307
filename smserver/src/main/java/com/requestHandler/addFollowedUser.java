package com.requestHandler;

import com.Exception.NoSuchDBEntryException;
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
@RequestMapping("/followUser")
public class addFollowedUser {

    UserDataHandler userHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity followUser(@RequestBody String s) {
        //String s should look like: userToFollow, userWhoIsFollowing, true/false
        System.out.println("Follow user string: "+s);
        String[] parseArr = s.split(", ");
        User userToFollow = null;
        User userWhoIsFollowing = null;
        String FollowOrFollow = parseArr[2];

        try {
            userToFollow = userHandler.getByName(parseArr[0]);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: User does not exist.");
        }

        try {
            userWhoIsFollowing = userHandler.getByName(parseArr[1]);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: User is not logged in.");
        }

        if (FollowOrFollow.equals("true")) {
            userWhoIsFollowing.getProfile().addUserFollowing(userToFollow.getUsername());
            userToFollow.getProfile().addUserFollowers(userWhoIsFollowing.getUsername());
            userHandler.save(userWhoIsFollowing);
            userHandler.save(userToFollow);
        }
        if (FollowOrFollow.equals("false")) {
            userWhoIsFollowing.getProfile().removeFollowing(userToFollow.getUsername());
            userToFollow.getProfile().removeFollower(userWhoIsFollowing.getUsername());
            userHandler.save(userWhoIsFollowing);
            userHandler.save(userToFollow);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            String json = mapper.writeValueAsString(userWhoIsFollowing);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //Client isn't looking for anything in particular, so just send OK if everything went fine
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
