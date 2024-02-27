package com.requestHandler;

import com.Exception.NoSuchDBEntryException;
import com.User.User;
import com.User.UserDataHandler;
import com.ClientUserRequest.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/login")
public class loginController {

    private final UserDataHandler userHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity loginController(@RequestBody String s) {
        System.out.println("Login: " + s);

        //String s looks like {"username":"username_here", "hash":"hash_here"}
        //Uses JSON.stringify so the {}s and quotes are needed.

        int usernameSemi = s.indexOf(":");
        int usernameComa = s.indexOf(",");
        int passwordSemi = s.indexOf(":", usernameSemi + 1);
        int end = s.indexOf("}");
        String username = s.substring(usernameSemi + 2, usernameComa - 1);
        String password = s.substring(passwordSemi + 2, end - 1);

        System.out.println("Username: " + username + " Password: " + password);
        //Normal Error Checking: username and password length (Make sure this is before the database access)
        //username section
        int userLength = username.length();

        if (userLength == 0) {
            System.out.println("Error: Username input blank.");
            return ResponseEntity.ok("Error: Username input blank.");
        }

        if (userLength > 20) {
            System.out.println("Error: Username too long.");
            return ResponseEntity.ok("Error: Username too long.");
        }

        //password section
/*        int passLength = password.length();

        if (passLength == 0) {
            System.out.println("Error: Password input blank.");
            return ResponseEntity.ok("Error: Password input blank.");
        }

        if (passLength < 5) {
            System.out.println("Error: Password too short.");
            return ResponseEntity.ok("Error: Password too short.");
        }

        if (passLength >= 15) {
            System.out.println("Error: Password too long.");
            return ResponseEntity.ok("Error: Password too long.");
        }*/

        // Determines if the authentication information matches (if error, comment this out)
        // Username is currently referring to User.name, not User.Profile.username

        boolean isMatch;
        try {
            isMatch = userHandler.doUsernamePasswordMatch(username, password);
        } catch (NoSuchDBEntryException e){
            System.out.println(e);
            System.out.println("FAILED user authentication, bad username");
            return ResponseEntity.ok("Error: Username does not match password");
        }

        if (isMatch) {
            try {
                System.out.println("Successfully authenticated user");
                User retUser = userHandler.getByName(username);
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                try {
                    String json = mapper.writeValueAsString(retUser);
                    System.out.println("ResultingJSONstring = " + json);
                    //System.out.println(json);
                    return ResponseEntity.ok(json);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            } catch (NoSuchDBEntryException e) {
                System.out.println(e);
                System.out.println("No such username");
                return ResponseEntity.ok("Error: No such username");
            }
        }
            System.out.println("FAILED user authentication");
            return ResponseEntity.ok("Error: FAILED user authentication");

            //Return value should be the user object if good, or "Wrong" if not good
    }
}
