package com.User;

import com.Exception.*;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Topic.Topic;
import com.Topic.TopicDataHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserTests {
    /*
    @Test
    void testUserlineFromTopics(){
        User newUser = new User("newUser", "newuser@gmail.com", "thisisapassword");
        try {
            userHandler.insert(newUser);
        } catch (DBEntryIDAlreadyExistsException | DBEntryEmailAlreadyExistsException | DBEntryUsernameAlreadyExists e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 5; i++) {
            Topic newTopic = new Topic("NewTopic" + i, newUser.getUsername());
            try {
                topicHandler.insert(newTopic);
            } catch (DBEntryIDAlreadyExistsException | DBEntryNameAlreadyExistsException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < 6; j++) {
                Post newPost = new Post(newUser.getUsername(), newTopic.getName(), false, false);

                try {
                    postHandler.insert(newPost);
                } catch (DBEntryAlreadyExistsException | NoSuchDBEntryException e) {
                    e.printStackTrace();
                }

                try {
                    newTopic = topicHandler.getByName(newTopic.getName());
                } catch (NoSuchDBEntryException e) {
                    e.printStackTrace();
                }
            }

            newUser.getProfile().addFollowingTopic(newTopic);
        }

        ArrayList<Post> posts = newUser.userlineFromTopics();
        userHandler.remove(userHandler.getByName("newUser"));
        for (Post p:
                posts) {
            System.out.println(p.getId());
        }

        assertTrue();
    }
    */

    /*@Test
    void sampleTest() {
        assertTrue(1 < 5);
    }*/
}