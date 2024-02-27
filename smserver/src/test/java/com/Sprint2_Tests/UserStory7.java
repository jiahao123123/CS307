package com.Sprint2_Tests;

import com.Post.Post;
import com.Topic.TopicDataHandler;
import com.User.User;
import com.User.UserDataHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.requestHandler.addFollowedTopic;
import com.requestHandler.timelineController;
import com.requestHandler.userlineController;
import lombok.AllArgsConstructor;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

@AllArgsConstructor
public class UserStory7 {
    private static final int USER_STORY_NUM = 7;

    timelineController timelinecontroller;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 1;
        int numTestsPassed = 0;
        numTestsPassed += test1_noUI() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 1 (full testing requires UI)
    public boolean test1_noUI() {
        // creates new test user
        User testUser = testLibrary.createTestUser();

        // follows TestTopic, which should have a lot of posts, not unfollowing that topic upon completion
        boolean passedTest = testLibrary.testFollowTestTopic(testUser, 1, false);
        // handles setup test completion
        if (!passedTest) {
            TestLibrary.criterionFailPrint(1, "Test Topic {" + TestLibrary.TOPIC_NAME
                    + "} not followed by Test User {" + TestLibrary.USERNAME + "} after setup");
            return false;
        }

        // controller needs to be implemented so I know how to parse the return for the posts
        String userlineResponseString = timelinecontroller.getTimelineController(testUser.getUsername()).getBody().toString();
        System.out.println("\n\n\n\n\n(pre) userlineResponseString: " + userlineResponseString);

        // fix response string for parsing
        //userlineResponseString = userlineResponseString.replaceAll("\"creationTime\":\\[", "\"creationTime\":\"");
        //userlineResponseString = userlineResponseString.replaceAll("\\],\"hasImage", "\",\"hasImage");

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
        if (timelinePosts.size() < 2){
            TestLibrary.criterionFailPrint(1, "List of timelinePosts is (" + timelinePosts.size()
                                           + "), which is less than 2");
            return false;
        }

        // ensures that posts are in chronological order (new post first)
        Post prevPost = timelinePosts.remove(0);
        for (Post post : timelinePosts) {
            if (post.getCreationTime().isAfter(prevPost.getCreationTime())) {
                TestLibrary.criterionFailPrint(1, "List of timelinePosts is not in chronological order");
                return false;
            }

            prevPost = post;
        }

        TestLibrary.criterionSucceedPrint(1);
        return true;
    }
}
