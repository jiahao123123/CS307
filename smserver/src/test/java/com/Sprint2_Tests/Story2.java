package com.Sprint2_Tests;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Story2 {
    private static final int USER_STORY_NUM = 2;

    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 0;
        int numTestsPassed = 0;
        //numTestsPassed += test2() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 2
    public boolean test2() {
        boolean testPassed = true;



        TestLibrary.criterionSucceedPrint(2);
        return testPassed;
    }
}
