package com.User;

import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Profile.*;
import com.Topic.Topic;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;

@Document
public class User {
    @Id
    private String id; // This is a unique id assigned by springboot upon object creation
    private String username;
    private String email;
    private String password;
    private Profile profile;

    public User() {
        this.profile = new Profile();
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = new Profile();
    }

    public String getId() {return id;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id + '\'' +
                "name='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public ArrayList<Post> timelinePosts(UserDataHandler userHandler, PostDataHandler postHandler) {
        ArrayList<Post> allTimelinePosts = new ArrayList<>();
        for (Topic topic : getProfile().getFollowingTopicsList()) {
            allTimelinePosts.addAll(topic.getTopicPosts());
        }
        for (String curUsername : getProfile().getUserFollowing()) {
            User curUser;
            try {
                curUser = userHandler.getByName(curUsername);
            } catch (NoSuchDBEntryException e) {
                System.out.println("SKIPPING timeline posts from user: " + curUsername + " because that user's not in the DB");
                continue;
            }

            allTimelinePosts.addAll(postHandler.getPostsCreatedByUser(curUser));
        }

        Collections.sort(allTimelinePosts);

        // removes duplicate posts
        for (int i = 1; i < allTimelinePosts.size(); i++) {
            if (allTimelinePosts.get(i - 1).getId() == allTimelinePosts.get(i).getId()) {
                allTimelinePosts.remove(i);
                i--;
            }
        }

        return allTimelinePosts;
    }
}
