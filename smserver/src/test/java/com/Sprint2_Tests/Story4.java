package com.Sprint2_Tests;

import com.ClientUserRequest.Request;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Reaction.ReactionType;
import com.User.User;
import com.User.UserDataHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.requestHandler.profileInfoController;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class Story4 {
    private static final int USER_STORY_NUM = 4;

    profileInfoController profileinfoController;
    UserDataHandler userHandler;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 2;
        int numTestsPassed = 0;
        numTestsPassed += test1() ? 1 : 0;
        numTestsPassed += test3() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 1
    public boolean test1() {
        boolean testPassed = true;

        // changes profileInfo
        User testUser;
        try {
            testUser = userHandler.getByName(TestLibrary.USERNAME);
        } catch (NoSuchDBEntryException e) {
            TestLibrary.criterionFailPrint(1, "test user not found in database");
            return false;
        }

        boolean newPublicVal = !testUser.getProfile().sexIsPublic();
        userHandler.setProfileAgePublic(testUser, newPublicVal);
        userHandler.save(testUser);

        // Gets profile info
        String responseString = profileinfoController.getProfileInfo(TestLibrary.USERNAME).getBody().toString();
        Request responseObj;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            responseObj = mapper.readValue(responseString, new TypeReference<Request>() { });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(1,
                    "JSON parsing of profileinfoController failed");
            return false;
        }

        if(responseObj.isAgeIsPublic() != newPublicVal) {
            TestLibrary.criterionFailPrint(1, "profileinfoController response wasn't updated with new agePublic");
            return false;
        }

        TestLibrary.criterionSucceedPrint(1);
        return testPassed;
    }

    // Acceptance Criterion 3
    public boolean test3() {
        long timeToBeat = 2000; // view profile info in less than 2 seconds
        long startTime = System.currentTimeMillis();

        // Gets profile info
        profileinfoController.getProfileInfo(TestLibrary.USERNAME);

        long endTime = System.currentTimeMillis();

        long timeDiff = endTime - startTime;

        if (timeDiff > timeToBeat) {
            TestLibrary.criterionFailPrint(3, "postInfo access time (" + timeDiff + " > " + timeToBeat + ")");
            return false;
        }

        TestLibrary.criterionSucceedPrint(3);
        return true;
    }

}
