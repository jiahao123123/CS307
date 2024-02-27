package com.requestHandler;

import com.DirectMessage.DirectMessage;
import com.Exception.NoSuchDBEntryException;
import com.Message.DMRepository;
import com.Message.Message;
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

import java.util.ArrayList;

@Controller
@AllArgsConstructor
@RequestMapping("/getDM")
public class directMessageController {

    UserDataHandler userDataHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity getDM(@RequestBody String s) {
        //String s should look like: id_num(sender), id_num(reciever)
        //Where the id_num corresponds to a direct message list in the database
        ArrayList<Message> dm = null;
        String[] parse = s.split(", ");
        System.out.println("DMS: " + s);
        dm = userDataHandler.getDMsBetweenUsers(parse[0], parse[1]);
        //probably need another send DM controller to send out dm between users (this one is get DM)

        //return 1
        ObjectMapper mapperMessage = new ObjectMapper();
        mapperMessage.registerModule(new JavaTimeModule());
        try {
            String json = mapperMessage.writeValueAsString(dm);
            System.out.println("ResultingJSONString = " + json);
            //System.out.println(json);
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            return ResponseEntity.ok("server: can not return this user object");
        }
        //return 2
        //return ResponseEntity.ok(dm);

        //The client will be looking for the Direct Message object corresponding to the id_num sent in
    }
}
