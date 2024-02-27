package com.Sprint2_Tests;

import com.Post.Post;
import com.User.User;
import com.requestHandler.postController;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class UserStory2 {
    private static final int USER_STORY_NUM = 2;

    postController postcontroller;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 3;
        int numTestsPassed = 0;
        numTestsPassed += test1_noUI() ? 1 : 0;
        numTestsPassed += test4_noUI() ? 1 : 0;
        numTestsPassed += test5() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 1 (full testing requires UI)
    public boolean test1_noUI() {
        final int NUM_POSTS_TO_ADD = 3;

        User testUser = testLibrary.createTestUser();

        int numTopicPostsPreAdd = testLibrary.numPostsInTopic(1, TestLibrary.TOPIC_NAME);
        if (numTopicPostsPreAdd < 0)
            return false;

        for (int i = 0; i < NUM_POSTS_TO_ADD; i++) {
            postcontroller.createPostController(TestLibrary.getCreatePostString(TestLibrary.TOPIC_NAME, "post" + i, "thisIsPostInfo", false, " ", TestLibrary.USERNAME));
        }

        int numTopicPostsPostAdd = testLibrary.numPostsInTopic(1, TestLibrary.TOPIC_NAME);
        if (numTopicPostsPostAdd < 0)
            return false;

        if (numTopicPostsPostAdd - numTopicPostsPreAdd != NUM_POSTS_TO_ADD) {
            TestLibrary.criterionFailPrint(1, "Topic (" + TestLibrary.TOPIC_NAME + ")'s posts, "
                                           + " returned by postController.getController(), didn't include this test's new posts, "
                                           + " added by postController.createPostController()");
            return false;
        }

        TestLibrary.criterionSucceedPrint(1);
        return true;
    }

    // Acceptance Criterion 4 (full testing requires UI)
    public boolean test4_noUI() {
        long timeToBeat = 3000; // creating post in less than 3 sec
        long startTime = System.currentTimeMillis();

        // Creates new post
        postcontroller.createPostController(TestLibrary.getCreatePostString(TestLibrary.TOPIC_NAME, "speedpost", "thisIsPostInfo", false, " ", TestLibrary.USERNAME));

        long endTime = System.currentTimeMillis();

        long timeDiff = endTime - startTime;
        boolean passedTest = timeDiff < timeToBeat;

        if (!passedTest)
            TestLibrary.criterionFailPrint(4, "new post creation time (" + timeDiff + " > " + timeToBeat + ")");

        TestLibrary.criterionSucceedPrint(4);
        return passedTest;
    }

    // Acceptance Criterion 5
    public boolean test5() {
        final int CAPTION_SIZE_LIMIT = 600;

        int numTopicPostsPreAdd = testLibrary.numPostsInTopic(5, TestLibrary.TOPIC_NAME);

        String captionString_tooLong = "abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789";
        String captionString_atLimit = "abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz01";

        while (captionString_tooLong.length() < CAPTION_SIZE_LIMIT) { captionString_tooLong += captionString_tooLong; }
        while (captionString_atLimit.length() < CAPTION_SIZE_LIMIT) { captionString_atLimit += captionString_atLimit; }

        byte[] imageStringBytes_tooLong = new byte[500001];
        byte[] imageStringBytes_atLimit = new byte[500000];
        String imageString_tooLong = new String(imageStringBytes_tooLong);
        String imageString_atLimit = new String(imageStringBytes_atLimit);

        // creates post with oversized caption
        postcontroller.createPostController(TestLibrary.getCreatePostString(TestLibrary.TOPIC_NAME, "oversized caption", captionString_tooLong, false, imageString_atLimit, TestLibrary.USERNAME));

        int numTopicPostsCurrent = testLibrary.numPostsInTopic(5, TestLibrary.TOPIC_NAME);
        if (numTopicPostsCurrent > numTopicPostsPreAdd) {
            TestLibrary.criterionFailPrint(5, "Post was created with oversized caption");
            return false;
        }

        // IMAGE removed from this sprint
        /*// creates post with oversized image
        postcontroller.createPostController(TestLibrary.getCreatePostString(TestLibrary.TOPIC_NAME, "oversized image", captionString_atLimit, false, imageString_tooLong, TestLibrary.USERNAME));

        numTopicPostsCurrent = testLibrary.numPostsInTopic(5, TestLibrary.TOPIC_NAME);
        if (numTopicPostsCurrent > numTopicPostsPreAdd) {
            TestLibrary.criterionFailPrint(5, "Post was created with oversized image");
            return false;
        }

        // creates post with oversized caption and image
        postcontroller.createPostController(TestLibrary.getCreatePostString(TestLibrary.TOPIC_NAME, "oversized caption and image", captionString_tooLong, false, captionString_tooLong, TestLibrary.USERNAME));

        numTopicPostsCurrent = testLibrary.numPostsInTopic(5, TestLibrary.TOPIC_NAME);
        if (numTopicPostsCurrent > numTopicPostsPreAdd) {
            TestLibrary.criterionFailPrint(5, "Post was created with oversized caption and image");
            return false;
        }*/

        TestLibrary.criterionSucceedPrint(5);
        return true;
    }
}
