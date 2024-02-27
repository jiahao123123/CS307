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
@RequestMapping("/blockUser")
public class addBlockedUser {

    UserDataHandler userHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity blockUser(@RequestBody String s) {
        //String s will look like: userToBlock, userWhoIsBlocking, true/false

        String[] parseArr = s.split(", ");
        User userToBlock = null;
        User userWhoIsBlocking = null;
        String BlockOrUnblock = parseArr[2];
        System.out.println("Block user string:" + s);
        try {
            userToBlock = userHandler.getByName(parseArr[0]);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: User does not exist.");
        }

        try {
            userWhoIsBlocking = userHandler.getByName(parseArr[1]);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: User is not logged in.");
        }

        if (BlockOrUnblock.equals("true")) {
            userWhoIsBlocking.getProfile().addUsersThisUserHasBlocked(userToBlock.getUsername());
            userToBlock.getProfile().addUsersThatBlockedThisUser(userWhoIsBlocking.getUsername());
            userHandler.save(userWhoIsBlocking);
            userHandler.save(userToBlock);
        }

        if (BlockOrUnblock.equals("false")) {
            userWhoIsBlocking.getProfile().removeUsersThisUserHasBlocked(userToBlock.getUsername());
            userToBlock.getProfile().removeUsersThatBlockedThisUser(userWhoIsBlocking.getUsername());
            userHandler.save(userWhoIsBlocking);
            userHandler.save(userToBlock);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            String json = mapper.writeValueAsString(userWhoIsBlocking);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //Client isn't really looking for anything in particular, so just send OK if everything worked.
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
