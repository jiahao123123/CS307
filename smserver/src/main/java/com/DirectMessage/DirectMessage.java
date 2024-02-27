package com.DirectMessage;

import com.Message.*;
import com.User.*;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public class DirectMessage {
    private User sender;
    private User receiver;
    private ArrayList<Message> messages;

    public DirectMessage() {
    }

    public DirectMessage(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.messages = new ArrayList<>();
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message m) {
        this.messages.add(m);
    }
}
