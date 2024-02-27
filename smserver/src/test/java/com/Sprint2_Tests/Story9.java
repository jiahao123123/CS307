package com.Sprint2_Tests;

import com.Exception.NoSuchDBEntryException;
import com.User.User;
import com.User.UserDataHandler;
import com.requestHandler.addBlockedUser;
import com.requestHandler.addFollowedUser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Story9 {
    private static final int USER_STORY_NUM = 9;
    private static final String USER1 = "blockUser1";
    private static final String USER2 = "blockUser2";

    addBlockedUser addblockedUser;
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

        // tests unblocking user
        addblockedUser.blockUser(TestLibrary.getBlockUserString(USER1, USER2, false));

        // ensures that the user is followed
        User user1;
        User user2;
        try {
            user1 = userHandler.getByName(USER1);
            user2 = userHandler.getByName(USER2);
        } catch (NoSuchDBEntryException e) {
            TestLibrary.criterionFailPrint(2, "Test User(s) not found in DB");
            return false;
        }

        if (user2.getProfile().getUsersThisUserHasBlocked().contains(USER1)) {
            TestLibrary.criterionFailPrint(2, "UnBlocking a user doesn't remove that user's name from Profile.usersThisUserHasBlocked");
            return false;
        }

        if (user1.getProfile().getUsersThatBlockedThisUser().contains(USER2)) {
            TestLibrary.criterionFailPrint(2, "UnBlocking a user doesn't remove your user's name from the other user's Profile.usersThatHaveBlockedThisUser");
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
        addblockedUser.blockUser(TestLibrary.getBlockUserString(USER1, USER2, true));

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

        if (!user2.getProfile().getUsersThisUserHasBlocked().contains(USER1)) {
            TestLibrary.criterionFailPrint(3, "Following a user doesn't add that user's name to Profile.usersThisUserHadBlocked");
            return false;
        }

        if (!user1.getProfile().getUsersThatBlockedThisUser().contains(USER2)) {
            TestLibrary.criterionFailPrint(3, "Following a user doesn't add your user's name to the other user's Profile.usersThatBlockedThisUser");
            return false;
        }

        TestLibrary.criterionSucceedPrint(3);
        return true;
    }
}
