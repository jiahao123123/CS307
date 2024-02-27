package com.Sprint2_Tests;

import com.Exception.NoSuchDBEntryException;
import com.Topic.Topic;
import com.Topic.TopicDataHandler;
import com.User.User;
import com.User.UserDataHandler;
import com.requestHandler.addFollowedTopic;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserStory6 {
    private static final int USER_STORY_NUM = 6;

    addFollowedTopic followTopicController;
    UserDataHandler userHandler;
    TopicDataHandler topicHandler;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 2;
        int numTestsPassed = 0;
        numTestsPassed += test1_noUI() ? 1 : 0;
        numTestsPassed += test2() ? 1 : 0;
        //numTestsPassed += test3() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 1 (full testing requires UI)
    public boolean test1_noUI() {
        User newUser = testLibrary.createTestUser();

        // wants following the topic to succeed
        boolean passedTest = testLibrary.testFollowTestTopic(newUser, 1, true);

        // handles test completion
        if (!passedTest)
            TestLibrary.criterionFailPrint(1, "Test Topic {" + TestLibrary.TOPIC_NAME
                    + "} not followed by Test User {" + TestLibrary.USERNAME + "}");

        TestLibrary.criterionSucceedPrint(1);
        return passedTest;
    }

    // Acceptance Criterion 2
    public boolean test2() {
        // inserts test user into database if not already there
        testLibrary.createTestUser();

        // removes test topic if already there
        User newUser = testLibrary.createTestUser();
        Topic testTopic = new Topic(TestLibrary.TOPIC_NAME, newUser.getUsername());
        topicHandler.remove(testTopic);

        // follows the topic
        followTopicController.addFollowedTopic("\"" + TestLibrary.USERNAME + ", " + TestLibrary.TOPIC_NAME + ", " + "true\"");

        // determines if topic is now followed
        boolean passedTest;
        try {
            User testUser = userHandler.getByName(TestLibrary.USERNAME);

            passedTest = !testUser.getProfile().getFollowingTopicsList().contains(testTopic);
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(1, "Test User not found in database");
            return false;
        }

        // clears the effect of this test (unfollows the test topic)
        followTopicController.addFollowedTopic("\"" + TestLibrary.USERNAME + ", " + TestLibrary.TOPIC_NAME + ", " + "false\"");

        // handles test completion
        if (!passedTest)
            TestLibrary.criterionFailPrint(1, "Test Topic {" + testTopic.getName() + "} is followed by Test User {"
                    + TestLibrary.USERNAME + "}, despite the topic not existing");

        TestLibrary.criterionSucceedPrint(2);
        return passedTest;
    }

    /*// Acceptance Criterion 3    / Client-side test
    public boolean test3() {
        // creates the user that will be following the topics
        User newUser = testLibrary.createTestUser();

        // tests if following the test topic works, not unfollowing that topic upon completion
        boolean passedTest = testLibrary.testFollowTestTopic(newUser, 3, false);
        // handles setup test completion
        if (!passedTest) {
            TestLibrary.criterionFailPrint(3, "Test Topic {" + TestLibrary.TOPIC_NAME
                    + "} not followed by Test User {" + TestLibrary.USERNAME + "} after setup");
            return false;
        }

        // wants following user to fail because that user is already following the topic
        passedTest &= !testLibrary.testFollowTestTopic(newUser, 3, true);

        // handles test completion
        if (!passedTest)
            TestLibrary.criterionFailPrint(3, "Following Test Topic {" + TestLibrary.TOPIC_NAME
                    + "} by Test User {" + TestLibrary.USERNAME + "} succeeded, despite the User already following the topic");

        TestLibrary.criterionSucceedPrint(3);
        return passedTest;
    }*/
}
