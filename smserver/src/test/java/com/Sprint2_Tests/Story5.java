package com.Sprint2_Tests;

import com.ClientUserRequest.Request;
import com.Exception.NoSuchDBEntryException;
import com.User.User;
import com.User.UserDataHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.requestHandler.addFollowedTopic;
import com.requestHandler.addFollowedUser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Story5 {
    private static final int USER_STORY_NUM = 5;
    private static final String USER1 = "follow2User1";
    private static final String USER2 = "follow2User2";
    private static final String USER3 = "follow2User3";

    addFollowedUser addfollowedUser;
    addFollowedTopic addfollowedTopic;
    UserDataHandler userHandler;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 3;
        int numTestsPassed = 0;
        numTestsPassed += test1() ? 1 : 0;
        numTestsPassed += test2() ? 1 : 0;
        numTestsPassed += test3() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 1
    public boolean test1() {
        boolean testPassed = true;

        // creates test users
        testLibrary.createTestUser(USER1, USER1 + "@gmail.com");
        testLibrary.createTestUser(USER2, USER2 + "@gmail.com");
        testLibrary.createTestUser(USER3, USER3 + "@gmail.com");

        // the first user follows the other users
        addfollowedUser.followUser(TestLibrary.getFollowUserString(USER2, USER1, true));
        addfollowedUser.followUser(TestLibrary.getFollowUserString(USER3, USER1, true));

        // the first user follows the test topic, and gets the user object
        addfollowedTopic.addFollowedTopic(TestLibrary.getFollowTopicString(USER1, TestLibrary.TOPIC_NAME, true)).getBody().toString();
        User user;
        try {
            user = userHandler.getByName(USER1);
        } catch (NoSuchDBEntryException e) {
            TestLibrary.criterionFailPrint(1, "User: " + USER1 + " not found in DB");
            return false;
        }

        if (!user.getProfile().getUserFollowing().contains(USER2)) {
            TestLibrary.criterionFailPrint(1, "User2 not in following list");
            return false;
        }
        if (!user.getProfile().getUserFollowing().contains(USER3)) {
            TestLibrary.criterionFailPrint(1, "User3 not in following list");
            return false;
        }
        if (!user.getProfile().isFollowingTopic(TestLibrary.TOPIC_NAME)) {
            TestLibrary.criterionFailPrint(1, "Topic: " + TestLibrary.TOPIC_NAME + " not in following list");
            return false;
        }

        TestLibrary.criterionSucceedPrint(1);
        return testPassed;
    }

    // Acceptance Criterion 2
    public boolean test2() {
        boolean testPassed = true;

        // unfollows User3
        addfollowedUser.followUser(TestLibrary.getFollowUserString(USER3, USER1, false)).getBody().toString();
        User user;
        try {
            user = userHandler.getByName(USER1);
        } catch (NoSuchDBEntryException e) {
            TestLibrary.criterionFailPrint(1, "User: " + USER1 + " not found in DB");
            return false;
        }

        if (user.getProfile().getUserFollowing().contains(USER3)) {
            TestLibrary.criterionFailPrint(2, "User3 still in following list");
            return false;
        }

        TestLibrary.criterionSucceedPrint(2);
        return testPassed;
    }

    // Acceptance Criterion 3
    public boolean test3() {
        boolean testPassed = true;

        long timeToBeat = 2000; // view profile info in less than 2 seconds
        long startTime = System.currentTimeMillis();

        // the first user follows the other user and test topic
        addfollowedTopic.addFollowedTopic(TestLibrary.getFollowTopicString(USER1, TestLibrary.TOPIC_NAME, false));

        long endTime = System.currentTimeMillis();

        long timeDiff = endTime - startTime;

        if (timeDiff > timeToBeat) {
            TestLibrary.criterionFailPrint(3, "addFollowTopic unfollow time (" + timeDiff + " > " + timeToBeat + ")");
            return false;
        }

        addfollowedUser.followUser(TestLibrary.getFollowUserString(USER2, USER1, false)).getBody().toString();
        User user;
        try {
            user = userHandler.getByName(USER1);
        } catch (NoSuchDBEntryException e) {
            TestLibrary.criterionFailPrint(3, "User: " + USER1 + " not found in DB");
            return false;
        }

        if (user.getProfile().getUserFollowing().contains(USER2)) {
            TestLibrary.criterionFailPrint(3, "User2 still in following list");
            return false;
        }
        if (user.getProfile().isFollowingTopic(TestLibrary.TOPIC_NAME)) {
            TestLibrary.criterionFailPrint(3, "Topic: " + TestLibrary.TOPIC_NAME + " still in following list");
            return false;
        }

        TestLibrary.criterionSucceedPrint(3);
        return testPassed;
    }
}
