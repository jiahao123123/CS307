package com.Sprint2_Tests;

import com.ClientUserRequest.Request;
import com.Exception.NoSuchDBEntryException;
import com.Profile.Profile;
import com.ProfileContent.Sex;
import com.Topic.TopicDataHandler;
import com.User.User;
import com.User.UserDataHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.requestHandler.addFollowedTopic;
import com.requestHandler.profileController;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@AllArgsConstructor
public class UserStory1 {
    private static final int USER_STORY_NUM = 1;

    UserDataHandler userHandler;
    profileController profilecontroller;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 2;
        int numTestsPassed = 0;
        numTestsPassed += test1_noUI() ? 1 : 0;
        numTestsPassed += test3_noUI() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 1 (full testing requires UI)
    public boolean test1_noUI() {
        // --- Tests the changing of ProfileInfo's public statuses --- //

        // Creates a new user with all private profile info
        User newUser = testLibrary.createTestUser();
        Profile profile = newUser.getProfile();

        // sets all fields to public
        profile.getSex().setPublicInfo(true);
        profile.getAge().setPublicInfo(true);
        profile.getBio().setPublicInfo(true);

        // sends request to server as JSON
        String requestJSON;
        Request request = new Request(newUser);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            requestJSON = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(1, "mapping request to JSON failed");
            return false;
        }
        profilecontroller.editProfile(requestJSON);




        // Retrieves newly saved database entry
        User foundUser;
        try {
            foundUser = userHandler.getByName(newUser.getUsername());
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            System.out.println("TEST FAILED - User Story 1.1 - No found DB entry");
            return false; // test failed
        }

        // Verifies that the entry in the database has all public ProfileInfo
        boolean testPassed = true;
        profile = foundUser.getProfile();
        String failureString = "";

        testPassed &= profile.getAge().isPublic();
        if (!testPassed)
            failureString += "Age not public   ";

        testPassed &= profile.getSex().isPublic();
        if (!testPassed)
            failureString += "Sex not public   ";

        testPassed &= profile.getBio().isPublic();
        if (!testPassed) {
            failureString += "Bio not public   ";
            TestLibrary.criterionFailPrint(1, failureString);
        }

        TestLibrary.criterionSucceedPrint(1);
        return testPassed;
    }

    // Acceptance Criterion 3
    public boolean test3_noUI() {
        // Creates a new user with all private profile info
        User newUser = testLibrary.createTestUser();
        Profile profile = newUser.getProfile();

        // Sets all sex to public
        profile.getSex().setPublicInfo(true);

        long timeToBeat = 1500; // changing profileInfo status in less than 1.5 sec
        long startTime = System.currentTimeMillis();

        // sends request to server as JSON
        String requestJSON;
        Request request = new Request(newUser);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            requestJSON = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(3, "mapping request to JSON failed");
            return false;
        }
        profilecontroller.editProfile(requestJSON);

        long endTime = System.currentTimeMillis();

        long timeDiff = endTime - startTime;
        boolean passedTest = timeDiff < timeToBeat;

        if (!passedTest)
            TestLibrary.criterionFailPrint(3, "ProfileInfo status change time (" + timeDiff + " > " + timeToBeat + ")");

        TestLibrary.criterionSucceedPrint(3);
        return passedTest;
    }
}
