package com.Sprint2_Tests;

import com.ClientUserRequest.Request;
import com.DbSequencer.IdSequencer;
import com.Exception.NoSuchDBEntryException;
import com.Post.PostDataHandler;
import com.User.User;
import com.User.UserDataHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.requestHandler.postController;
import com.requestHandler.profileInfoController;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Story6 {
    private static final int USER_STORY_NUM = 6;
    public static String USERNAME = "Story6TestUser24";

    profileInfoController profileinfoController;
    postController postcontroller;
    UserDataHandler userHandler;
    PostDataHandler postHandler;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 2;
        int numTestsPassed = 0;
        numTestsPassed += test1() ? 1 : 0;
        numTestsPassed += test2() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 1
    public boolean test1() {
        boolean testPassed = true;

        // Gets profile info
        String responseString = profileinfoController.getProfileInfo(TestLibrary.USERNAME).getBody().toString();
        Request responseObj;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            responseObj = mapper.readValue(responseString, Request.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(1,
                    "JSON parsing of profileinfoController failed");
            return false;
        }

        try {
            if(!responseObj.equals(new Request(userHandler.getByName(TestLibrary.USERNAME)))) {
                TestLibrary.criterionFailPrint(1, "profileinfoController response wasn't updated with new agePublic");
                return false;
            }
        } catch (NoSuchDBEntryException e) {
            TestLibrary.criterionFailPrint(1, "Test user not in DB");
            return false;
        }

        TestLibrary.criterionSucceedPrint(1);
        return testPassed;
    }

    // Acceptance Criterion 2
    public boolean test2() {
        boolean testPassed = true;

        // creates test user and 10 test posts
        testLibrary.createTestUser(USERNAME, USERNAME + "@gmail.com");
        for (int i = 0; i < 10; i++) {
            postcontroller.createPostController(TestLibrary.getCreatePostString(TestLibrary.TOPIC_NAME, "Post" + i, "info " + i, false, " ", USERNAME));

            User user;
            try {
                user = userHandler.getByName(USERNAME);
            } catch (NoSuchDBEntryException e) {
                TestLibrary.criterionFailPrint(2, "User: " + USERNAME + " not found in DB");
                return false;
            }
            if (user.getProfile().getPosts().size() != i + 1) {
                TestLibrary.criterionFailPrint(2, "newly created posts not being added to Profile.posts: " + user.getProfile());
                return false;
            }
            if (user.getProfile().getNumPosts() != i + 1) {
                TestLibrary.criterionFailPrint(2, "newly created posts not incrementing Profile.numPosts");
                return false;
            }
        }

        // Gets profile info
        String responseString = profileinfoController.getProfileInfo(USERNAME).getBody().toString();
        Request responseObj;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            responseObj = mapper.readValue(responseString, new TypeReference<Request>() { });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(2, "JSON parsing of profileinfoController failed");
            return false;
        }

        int numPosts;
        try {
            numPosts = postHandler.getPostsCreatedByUser(userHandler.getByName(USERNAME)).size();
            System.out.println("found " + numPosts + " posts created by user: " + USERNAME);
        } catch (NoSuchDBEntryException e) {
            TestLibrary.criterionFailPrint(2, "Test User not in DB");
            return false;
        }
        if (responseObj.getNumPosts() != numPosts) {
            TestLibrary.criterionFailPrint(2, "Num posts in returned RequestObj is inaccurate {" + responseObj.getNumPosts() + " != " + numPosts);
            return false;
        }

        TestLibrary.criterionSucceedPrint(2);
        return testPassed;
    }
}
