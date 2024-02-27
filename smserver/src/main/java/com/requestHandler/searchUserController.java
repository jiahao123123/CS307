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
@RequestMapping("/searchUser")
public class searchUserController {

    UserDataHandler userHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity searchUser(@RequestBody String s) {
        //String s gets pass in as: search_string

        User user = null;
        try {
            user = userHandler.getByName(s);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: user with provided name doesn't exist");
        }

        //The client is looking for a user object
        //1
        ObjectMapper mapperUser = new ObjectMapper();
        mapperUser.registerModule(new JavaTimeModule());
        try {
            String json = mapperUser.writeValueAsString(user);
            System.out.println("ResultingJSONString = " + json);
            //System.out.println(json);
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            return ResponseEntity.ok("server: can not return this user object");
        }
        //2
        //return ResponseEntity.ok(user);
    }
}
