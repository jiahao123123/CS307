package com.requestHandler;

import com.Comment.Comment;
import com.Comment.CommentDataHandler;
import com.DbSequencer.IdSequencer;
import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import com.Message.Message;
import com.User.User;
import com.User.UserDataHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/sendMessage")
public class addDMController {
    private final UserDataHandler userDataHandler;
    private IdSequencer idSequencer;
    @PostMapping
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity sendMessage(@RequestBody String s) {
        //String s looks like: messageText, senderId, receiverId
        //Does not use JSON.stringify
        System.out.println("Send dm:" + s);
        String[] parse = s.split(", ");
        Message messageToSend = new Message(parse[0], parse[1], parse[2]);

        try {
            userDataHandler.sendDM(messageToSend);
        } catch (DBEntryAlreadyExistsException | NoSuchDBEntryException e) {
            return ResponseEntity.ok("Database: no such message/ message already exist");
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
