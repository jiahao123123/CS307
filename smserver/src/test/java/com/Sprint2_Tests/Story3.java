package com.Sprint2_Tests;

import com.Exception.NoSuchDBEntryException;
import com.User.User;
import com.User.UserDataHandler;
import com.requestHandler.addFollowedUser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Story3 {
    private static final int USER_STORY_NUM = 3;
    private static final String USER1 = "followUser1";
    private static final String USER2 = "followUser2";

    addFollowedUser addfollowedUser;
    UserDataHandler userHandler;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 2;
        int numTestsPassed = 0;
        numTestsPassed += test3() ? 1 : 0;
        numTestsPassed += test2() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 2
    public boolean test2() {
        boolean testPassed = true;

        // tests unfollowing user
        addfollowedUser.followUser(TestLibrary.getFollowUserString(USER1, USER2, false));

        // ensures that the user is followed
        User user1;
        User user2;
        try {
            user1 = userHandler.getByName(USER1);
            user2 = userHandler.getByName(USER2);
        } catch (NoSuchDBEntryException e) {
            TestLibrary.criterionFailPrint(3, "Test User(s) not found in DB");
            return false;
        }

        if (user2.getProfile().getUserFollowing().contains(USER1)) {
            TestLibrary.criterionFailPrint(3, "UnFollowing a user doesn't remove that user's name from Profile.userFollowing");
            return false;
        }

        if (user1.getProfile().getUserFollowers().contains(USER2)) {
            TestLibrary.criterionFailPrint(3, "UnFollowing a user doesn't remove your user's name from the other user's Profile.userFollowers");
            return false;
        }

        TestLibrary.criterionSucceedPrint(2);
        return testPassed;
    }

    // Acceptance Criterion 3
    public boolean test3() {
        // creates testUsers
        testLibrary.createTestUser(USER1, USER1 + "@gmail.com");
        testLibrary.createTestUser(USER2, USER2 + "@gmail.com");

        // user2 follows user1
        addfollowedUser.followUser(TestLibrary.getFollowUserString(USER1, USER2, true));

        // ensures that the user is followed
        User user1;
        User user2;
        try {
            user1 = userHandler.getByName(USER1);
            user2 = userHandler.getByName(USER2);
        } catch (NoSuchDBEntryException e) {
            TestLibrary.criterionFailPrint(3, "Test User(s) not found in DB");
            return false;
        }

        if (!user2.getProfile().getUserFollowing().contains(USER1)) {
            TestLibrary.criterionFailPrint(3, "Following a user doesn't add that user's name to Profile.userFollowing");
            return false;
        }

        if (!user1.getProfile().getUserFollowers().contains(USER2)) {
            TestLibrary.criterionFailPrint(3, "Following a user doesn't add your user's name to the other user's Profile.userFollowers");
            return false;
        }

        TestLibrary.criterionSucceedPrint(3);
        return true;
    }
}
