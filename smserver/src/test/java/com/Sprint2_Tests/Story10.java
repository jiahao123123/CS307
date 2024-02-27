package com.Sprint2_Tests;

import com.ClientUserRequest.Request;
import com.Exception.NoSuchDBEntryException;
import com.User.User;
import com.User.UserDataHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.requestHandler.searchUserController;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Story10 {
    private static final int USER_STORY_NUM = 10;

    searchUserController searchuserController;
    UserDataHandler userHandler;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 1;
        int numTestsPassed = 0;
        //numTestsPassed += test1() ? 1 : 0;
        numTestsPassed += test3() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 1
    public boolean test1() {
        boolean testPassed = true;

        // Gets User object
        String responseString = searchuserController.searchUser(TestLibrary.USERNAME).getBody().toString();
        User responseObj;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            responseObj = mapper.readValue(responseString, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(1,
                    "JSON parsing of profileinfoController failed");
            return false;
        }

        User DBuser;
        try {
            DBuser = userHandler.getByName(TestLibrary.USERNAME);
        } catch (NoSuchDBEntryException e) {
            TestLibrary.criterionFailPrint(1,
                    "Test User not found in DB");
            return false;
        }

        if (!responseObj.equals(DBuser)) {
            TestLibrary.criterionFailPrint(1, "User: " + DBuser + " not returned by searchUserController: " + responseObj);
            return false;
        }

        TestLibrary.criterionSucceedPrint(1);
        return testPassed;
    }

    // Acceptance Criterion 3
    public boolean test3() {
        boolean testPassed = true;

        long timeToBeat = 3000; // view user in 3 seconds
        long startTime = System.currentTimeMillis();

        // Gets User
        searchuserController.searchUser(TestLibrary.USERNAME);

        long endTime = System.currentTimeMillis();

        long timeDiff = endTime - startTime;

        if (timeDiff > timeToBeat) {
            TestLibrary.criterionFailPrint(3, "postInfo access time (" + timeDiff + " > " + timeToBeat + ")");
            return false;
        }

        TestLibrary.criterionSucceedPrint(3);
        return testPassed;
    }
}
