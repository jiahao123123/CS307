package com.requestHandler;

import com.ClientUserRequest.Request;
import com.Exception.NoSuchDBEntryException;
import com.User.User;
import com.User.UserDataHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("getProfileInfo")
public class profileInfoController {

    UserDataHandler userHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity getProfileInfo(@RequestBody String s) {

        //String s looks like: username

        System.out.println(s);

        try {
            User temp = userHandler.getByName(s);

            System.out.println(temp.getUsername());

            Request cr = new Request(temp);

            System.out.println("Request entity: " + cr.toString());

            return ResponseEntity.ok(cr.toString());
        }
        catch (NoSuchDBEntryException e) {
            System.out.println(e);
        }

        System.out.println("We don't want to be here");

        //Returns the Request object if good, but "User not found"
        return ResponseEntity.ok("User not found");
    }
}
