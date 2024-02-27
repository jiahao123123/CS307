package com.ClientUserRequest;

import com.User.*;
import com.Topic.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

@Data
public class Request {
    String name;
    String email;
    String sex;
    boolean sexIsPublic;
    int age;
    boolean ageIsPublic;
    String bio;
    boolean bioIsPublic;
    String[] followedUsers;
    String[] usersFollowing;
    String[] blockedUsers;
    String[] followedTopics;
    String[] blockedTopics;
    int numPosts;
    boolean dmStatus;

    public Request(){
        super();
    }

    public Request(User u) {
        this.name = u.getUsername();
        this.email = u.getEmail();
        this.sex = u.getProfile().getSexString();
        this.sexIsPublic = u.getProfile().sexIsPublic();
        this.age = u.getProfile().getAgeInt();
        this.ageIsPublic = u.getProfile().ageIsPublic();
        this.bio = u.getProfile().getBioString();
        this.bioIsPublic = u.getProfile().bioIsPublic();
        this.numPosts = u.getProfile().getNumPosts();
        this.dmStatus = u.getProfile().getDMStatus();
        ArrayList<String> tempUsers = u.getProfile().getUserFollowing();
        ArrayList<String> tempListString = new ArrayList<>();
        for (String us : tempUsers) {
            tempListString.add("\"" + us + "\"");
        }
        this.followedUsers = new String[tempListString.size()];
        this.followedUsers = tempListString.toArray(this.followedUsers);

        tempUsers = u.getProfile().getUserFollowers();
        tempListString.clear();
        for (String us : tempUsers) {
            tempListString.add("\"" + us + "\"");
        }
        this.usersFollowing = new String[tempListString.size()];
        this.followedUsers = tempListString.toArray(this.usersFollowing);

        tempUsers = u.getProfile().getUsersThisUserHasBlocked();
        tempListString.clear();
        for (String us : tempUsers) {
            tempListString.add("\"" + us + "\"");
        }
        this.blockedUsers = new String[tempListString.size()];
        this.blockedUsers = tempListString.toArray(this.usersFollowing);

        ArrayList<Topic> tempTopics = u.getProfile().getFollowingTopicsList();
        tempListString.clear();
        for (Topic t : tempTopics) {
            tempListString.add("\"" + t.getName() + "\"");
        }
        this.followedTopics = new String[tempListString.size()];
        this.followedTopics = tempListString.toArray(this.followedTopics);

        tempTopics = u.getProfile().getBlockedTopic();
        tempListString.clear();
        for (Topic t : tempTopics) {
            tempListString.add("\"" + t.getName() + "\"");
        }
        this.blockedTopics = new String[tempListString.size()];
        this.blockedTopics = tempListString.toArray(this.blockedTopics);
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + '\"' +
                ", \"email\":\"" + email + '\"' +
                ", \"sex\":\"" + sex + '\"' +
                ", \"sexIsPublic\":" + sexIsPublic +
                ", \"age\":" + age +
                ", \"ageIsPublic\":" + ageIsPublic +
                ", \"bio\":\"" + bio + '\"' +
                ", \"bioIsPublic\":" + bioIsPublic +
                ", \"followedUsers\":" + Arrays.toString(followedUsers) +
                ", \"usersFollowing\":" + Arrays.toString(usersFollowing) +
                ", \"blockedUsers\":" + Arrays.toString(blockedUsers) +
                ", \"followedTopics\":" + Arrays.toString(followedTopics) +
                ", \"blockedTopics\":" + Arrays.toString(blockedTopics) +
                ", \"dmStatus\":" + dmStatus +
                ", \"numPosts\":" + numPosts +
                '}';
    }
}
