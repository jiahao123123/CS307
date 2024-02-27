package com.requestHandler;

import com.ClientUserRequest.Request;
import com.Exception.DBEntryEmailAlreadyExistsException;
import com.Exception.DBEntryIDAlreadyExistsException;
import com.Exception.DBEntryUsernameAlreadyExists;
import com.Exception.NoSuchDBEntryException;
import com.Profile.Profile;
import com.User.*;
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
@RequestMapping("/signup")
public class signupController {

    private final UserDataHandler userHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity signupController(@RequestBody String s) {
        System.out.println("Signup: " + s);

        //String s looks like: {"username":"username_here","email":"email_here","hash":"hash_here"}
        //Uses JSON.stringify, so the {}s and quotes are necessary

        int usernameSemi = s.indexOf(":");
        int usernameComa = s.indexOf(",");
        int emailSemi = s.indexOf(":", usernameSemi + 1);
        int emailComa = s.indexOf(",", usernameComa + 1);
        int passwordSemi = s.indexOf(":", emailSemi + 1);
        int end = s.indexOf("}");
        String username = s.substring(usernameSemi + 2, usernameComa - 1);
        String email = s.substring(emailSemi + 2, emailComa - 1);
        String password = s.substring(passwordSemi + 2, end - 1);

        System.out.println("Username: " + username + " Email: " + email + " Password: " + password);

        //Other Error Checking (email format, length of fields, etc.)
        //email section
        int emailLength = email.length();
        int validSymbol = email.indexOf("@");
        int validPeriod = email.indexOf(".");

        if (emailLength == 0) {
            System.out.println("Error: Email input blank.");
            return ResponseEntity.ok("Error: Email input blank.");
        }

        if (emailLength < 7) {
            System.out.println("Error: Email length too short.");
            return ResponseEntity.ok("Error: Email length too short.");
        }

        if (validSymbol == -1 || validPeriod == -1) {
            System.out.println("Error: Incorrect email format.");
            return ResponseEntity.ok("Error: Incorrect email format.");
        }

        //check for the part before @ should be all characters
        String[] charPortion = email.split("@");
        if (charPortion.length > 2) {
            System.out.println("Error: more than one @ symbol, incorrect format.");
            return ResponseEntity.ok("Error: more than one @ symbol, incorrect format.");

        }
        String stringBeforeSymbol = charPortion[0];
        if (stringValidation(stringBeforeSymbol)) {
            System.out.println("Error: Incorrect email format, non character before @");
            return ResponseEntity.ok("Error: Incorrect email format, non character before @");
        }

        //check for the part that after @ and before .com or .edu should be all characters
        String[] postPortion = charPortion[1].split("\\.");
        if (postPortion.length > 2) {
            System.out.println("Error: more than one period, incorrect format.");
            return ResponseEntity.ok("Error: more than one period, incorrect format.");
        }

        String stringAfterSymbolBeforePeriod = postPortion[0];
        if (stringValidation(stringAfterSymbolBeforePeriod)) {
            System.out.println("Error: Incorrect email format, non character after @");
            return ResponseEntity.ok("Error: Incorrect email format, non character after @");
        }

        String validEnding = email.substring(validPeriod, emailLength);

        if (!validEnding.equals(".edu") && !validEnding.equals(".com")) {
            System.out.println("Error: Incorrect email ending.");
            return ResponseEntity.ok("Error: Incorrect email ending.");
        }

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
/*
        //password section
        int passLength = password.length();

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
        }
*/

        User user = new User(username, email, password);
        //Adds the new user to the database (make sure this is after the other error check)
        User addedUser;
        try {
            addedUser = userHandler.insert(user);
        } //Database Error Checking
        catch (DBEntryEmailAlreadyExistsException | DBEntryIDAlreadyExistsException | DBEntryUsernameAlreadyExists e) {
            System.out.println("FAILED adding new user to database.     " + user + " }\n" + e);
            return ResponseEntity.ok("Error: Email/Username/ID is already registered, please use a new one");
        }

        System.out.println("New user successfully added to database with empty Profile.     " + addedUser + " }");

        try {
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
            e.printStackTrace();
            return ResponseEntity.ok("Failed");
        }
        return ResponseEntity.ok("Error: JSON Parsing");
    }
    private boolean stringValidation(String str) {
        str = str.toLowerCase();
        char[] charArray = str.toCharArray();
        for (char ch : charArray) {
            if (!(ch >= 'a' && ch <= 'z')) {
                return true;
            }
        }
        return false;
    }
}
