package com.Message;

import com.User.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class Message implements Comparable<Message> {
    @Id
    private int id;
    private String text;
    private String senderId;
    private String recipientId;
    private LocalDateTime creationTime;

    public Message(String text, String senderId, String recipientId) {
        this.text = text;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.creationTime = LocalDateTime.now();
    }

    @Override
    // gives most recent DM at the bottom of an ArrayList
    public int compareTo(Message compareMessage) {
        return getCreationTime().compareTo(compareMessage.getCreationTime());
    }
}