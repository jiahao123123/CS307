package com.Sprint2_Tests;

import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.DBEntryIDAlreadyExistsException;
import com.Exception.DBEntryNameAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Topic.Topic;
import com.Topic.TopicDataHandler;
import com.User.User;
import com.User.UserDataHandler;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class UserStory3 {
    private static final int USER_STORY_NUM = 3;

    TopicDataHandler topicHandler;
    PostDataHandler postHandler;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 1;
        int numTestsPassed = 0;
        numTestsPassed += test3() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 3
    public boolean test3() {
        // creates test topic
        User newUser = testLibrary.createTestUser();
        testLibrary.createTestTopic(newUser.getUsername());

        // inserts 20 posts into a topic
        for (int i = 0; i < 20; i++){
            Post newPost = new Post(newUser.getUsername(), TestLibrary.TOPIC_NAME, "Test Post Title", false, false, false);
            try {
                postHandler.insert(newPost);
            } catch (DBEntryAlreadyExistsException | NoSuchDBEntryException e) {
                e.printStackTrace();
                return false;
            }
        }

        // gets topic from database
        try {
            Topic topic = topicHandler.getByName(TestLibrary.TOPIC_NAME);

            LocalDateTime highestTime = LocalDateTime.now();
            for (Post p : topic.getSortedPosts()){
                if (p.getCreationTime().isAfter(highestTime)) // posts aren't in chronological order
                {
                    TestLibrary.criterionFailPrint(3, "Topic {" + TestLibrary.TOPIC_NAME + "}'s posts aren't in descending chronological order.");
                    return false;
                }
                highestTime = p.getCreationTime();
            }

            TestLibrary.criterionSucceedPrint(3);
            return true;
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            return false;
        }
    }
}
