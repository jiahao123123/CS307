package com.Sprint2_Tests;

import com.requestHandler.userlineController;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Story11 {
    private static final int USER_STORY_NUM = 11;

    userlineController userlinecontroller;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 1;
        int numTestsPassed = 0;
        numTestsPassed += test2() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 2
    public boolean test2() {
        boolean testPassed = true;

        long timeToBeat = 3000; // view user in 3 seconds
        long startTime = System.currentTimeMillis();

        // Gets Userline
        userlinecontroller.getUserline(TestLibrary.USERNAME);

        long endTime = System.currentTimeMillis();

        long timeDiff = endTime - startTime;

        if (timeDiff > timeToBeat) {
            TestLibrary.criterionFailPrint(3, "postInfo access time (" + timeDiff + " > " + timeToBeat + ")");
            return false;
        }

        TestLibrary.criterionSucceedPrint(2);
        return testPassed;
    }
}
