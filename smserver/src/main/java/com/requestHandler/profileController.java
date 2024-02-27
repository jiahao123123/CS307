package com.requestHandler;

import com.ClientUserRequest.Request;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Profile.Profile;
import com.ProfileContent.ProfileContent;
import com.ProfileContent.Sex;
import com.User.User;
import com.User.UserDataHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@AllArgsConstructor
@RequestMapping("/editProfile")
public class profileController {
    private final UserDataHandler userDataHandler;
    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity editProfile(@RequestBody String s) {
        System.out.println("Edited info: " + s);


        Request request;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            request = mapper.readValue(s, Request.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("FAILED JSON parsing of profileController.editProfile()'s input string");
            return ResponseEntity.ok("Error: parsing error");
        }
        String username = request.getName(); //s.substring(usernameSemi + 2, usernameComa - 1);
        String about = request.getBio(); //s.substring(aboutSemi + 2, aboutComa - 1);
        Boolean status = request.isDmStatus();
        //String password = s.substring(passwordSemi + 2, end - 1);
        User userUpdate;
        System.out.println("Username: " + username + " About: " + about/* + " Password: " + password*/);

        //update the ABOUT ME (bio) of the profile
        //But the issue is username and passwords in string s is always empty
        //all I can fetch solidly is about now
        try {
            userUpdate = userDataHandler.getByName(username);
        } catch (NoSuchDBEntryException e) {
            return ResponseEntity.ok("Error: no such user");
        }

        //save for sprint 3 ediot profile
        userDataHandler.setProfileUsername(userUpdate, username);
        userDataHandler.setProfileBio(userUpdate, about);
        userDataHandler.setProfileBioPublic(userUpdate, request.isBioIsPublic());
        userDataHandler.setProfileAge(userUpdate, request.getAge());
        userDataHandler.setProfileAgePublic(userUpdate, request.isAgeIsPublic());
        userDataHandler.setProfileSex(userUpdate, Sex.valueOf(request.getSex()));
        userDataHandler.setProfileSexPublic(userUpdate, request.isSexIsPublic());
        userDataHandler.setProfileDMStatus(userUpdate, status);
        userDataHandler.save(userUpdate);

        //As of right now, the client isn't asking for anything, so just return "OK"
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
