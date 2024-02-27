package com.Sprint2_Tests;

import com.Post.Post;
import com.User.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.requestHandler.addFollowedTopic;
import com.requestHandler.addFollowedUser;
import com.requestHandler.timelineController;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class Story7 {
    private static final int USER_STORY_NUM = 7;
    private static String TESTUSERNAME_DOINGTHEFOLLOWING = "timelineUser";
    private static String TESTUSERNAME = Story6.USERNAME;

    timelineController timelinecontroller;
    addFollowedTopic addfollowedTopic;
    addFollowedUser addfollowedUser;
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

        // creates new test user
        User testUser = testLibrary.createTestUser(TESTUSERNAME_DOINGTHEFOLLOWING, TESTUSERNAME_DOINGTHEFOLLOWING +"@gmail.com");

        // follows a test topic and user
        addfollowedTopic.addFollowedTopic(TestLibrary.getFollowTopicString(TESTUSERNAME_DOINGTHEFOLLOWING, TestLibrary.TOPIC_NAME, true));
        addfollowedUser.followUser(TestLibrary.getFollowUserString(TESTUSERNAME, TESTUSERNAME_DOINGTHEFOLLOWING, true));

        // follows TestTopic, which should have a lot of posts, not unfollowing that topic upon completion
        boolean passedTest = testLibrary.testFollowTestTopic(testUser, 1, false);
        // handles setup test completion
        if (!passedTest) {
            TestLibrary.criterionFailPrint(1, "Test Topic {" + TestLibrary.TOPIC_NAME
                    + "} not followed by Test User {" + TestLibrary.USERNAME + "} after setup");
            return false;
        }

        // gets timeline response string
        String userlineResponseString = timelinecontroller.getTimelineController(testUser.getUsername()).getBody().toString();

        // parses the JSON string into an ArrayList<Post>
        ArrayList<Post> timelinePosts;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            timelinePosts = mapper.readValue(userlineResponseString, new TypeReference<ArrayList<Post>>() { });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(1,
                    "JSON parsing of timelinecontroller.getTimelineController() failed");
            return false;
        }

        // fails if there are less than 2 posts in the timeline
        if (timelinePosts == null){
            TestLibrary.criterionFailPrint(1, "userlinecontroller.getUserline() response not specified");
            return false;
        }
        if (timelinePosts.size() < 1){
            TestLibrary.criterionFailPrint(1, "List of timelinePosts is (" + timelinePosts.size()
                    + "), which is less than 1");
            return false;
        }

        // ensures that posts are in chronological order (new post first)
        for (Post post : timelinePosts) {
            if (!post.getCreatorName().equals(TESTUSERNAME) && !post.getTopicName().equals(TestLibrary.TOPIC_NAME)) {
                TestLibrary.criterionFailPrint(1, "List of timelinePosts includes incorrect post: " + post);
                testPassed = false;
            }
        }

        if (testPassed)
            TestLibrary.criterionSucceedPrint(1);

        return testPassed;
    }

    // Acceptance Criterion 2
    public boolean test2() {
        boolean testPassed = true;

        // gets timeline response string
        String userlineResponseString = timelinecontroller.getTimelineController(TESTUSERNAME_DOINGTHEFOLLOWING).getBody().toString();

        // parses the JSON string into an ArrayList<Post>
        ArrayList<Post> timelinePosts;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            timelinePosts = mapper.readValue(userlineResponseString, new TypeReference<ArrayList<Post>>() { });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(2,
                    "JSON parsing of timelinecontroller.getTimelineController() failed");
            return false;
        }

        // fails if there are less than 2 posts in the timeline
        if (timelinePosts == null){
            TestLibrary.criterionFailPrint(2, "userlinecontroller.getUserline() response not specified");
            return false;
        }
        if (timelinePosts.size() < 2){
            TestLibrary.criterionFailPrint(2, "List of timelinePosts is (" + timelinePosts.size()
                    + "), which is less than 2");
            return false;
        }

        // ensures that posts are in chronological order (new post first)
        Post prevPost = timelinePosts.remove(0);
        for (Post post : timelinePosts) {
            if (post.getCreationTime().isAfter(prevPost.getCreationTime())) {
                TestLibrary.criterionFailPrint(2, "List of timelinePosts is not in chronological order");
                return false;
            }

            prevPost = post;
        }

        TestLibrary.criterionSucceedPrint(2);
        return testPassed;
    }

    // Acceptance Criterion 3
    public boolean test3() {
        boolean testPassed = true;

        long timeToBeat = 3000; // view profile info in less than 2 seconds
        long startTime = System.currentTimeMillis();

        // Gets profile info
        timelinecontroller.getTimelineController(TestLibrary.USERNAME).getBody().toString();

        long endTime = System.currentTimeMillis();

        long timeDiff = endTime - startTime;

        if (timeDiff > timeToBeat) {
            TestLibrary.criterionFailPrint(3, "timeline access time (" + timeDiff + " > " + timeToBeat + ")");
            return false;
        }

        TestLibrary.criterionSucceedPrint(3);
        return testPassed;
    }
}
