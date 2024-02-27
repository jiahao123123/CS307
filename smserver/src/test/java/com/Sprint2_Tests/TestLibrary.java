package com.Sprint2_Tests;

import com.Comment.Comment;
import com.Comment.CommentDataHandler;
import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.DBEntryIDAlreadyExistsException;
import com.Exception.DBEntryNameAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Profile.Profile;
import com.ProfileContent.Sex;
import com.Reaction.ReactionType;
import com.Topic.Topic;
import com.Topic.TopicDataHandler;
import com.User.User;
import com.User.UserDataHandler;
import com.requestHandler.addFollowedTopic;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TestLibrary {
    public static final String TOPIC_NAME = "TestCaseTopic";
    public static final String USERNAME = "TestCaseUser";

    UserDataHandler userHandler;
    TopicDataHandler topicHandler;
    PostDataHandler postHandler;
    CommentDataHandler commentHandler;
    addFollowedTopic followTopicController;

    public User createTestUser() {
        return createTestUser(USERNAME, "TestCaseUser@gmail.com");
    }

    public User createTestUser(String username, String email) {
        User newUser = new User(username, email, "thisisatestpassword");
        newUser.setProfile(new Profile(username, Sex.MALE, 29, "Test bio"));

        try {
            // remove matching entry from the database if it already exists
            userHandler.remove_incomplete(username);
        } catch (NoSuchDBEntryException e){
            // there is no matching entry to remove
        }

        try { // inserts newUser into database if not already there
            return userHandler.insert(newUser);
        } catch (DBEntryAlreadyExistsException e) {
            // if matching entry found, set newUser to that found database entry
            e.printStackTrace();
            System.out.println("TestLibrary.createTestUser FAILED - multiple new users found in database");
            return null;
        }
    }

    public Topic createTestTopic(String creatorUsername) {
        try {
            return topicHandler.insert(new Topic(TestLibrary.TOPIC_NAME, creatorUsername));
        } catch (DBEntryIDAlreadyExistsException | DBEntryNameAlreadyExistsException e) {
            // topic doesn't need to be inserted because it's already in database
            try {
                return topicHandler.getByName(TestLibrary.TOPIC_NAME);
            } catch (NoSuchDBEntryException ex) {
                ex.printStackTrace();
                System.out.println("TestLibrary.createTestTopic FAILED - no test topic found in database");
                return null;
            }
        }
    }

    // requires that newUser is from TestLibrary.createTestUser()
    public boolean testFollowTestTopic(User newUser, int criterionNum, boolean undoFollowingAfterTest) {
        // creates test topic if not already there
        Topic testTopic;
        try {
            testTopic = topicHandler.insert(new Topic(TestLibrary.TOPIC_NAME, newUser.getUsername()));
        } catch (DBEntryIDAlreadyExistsException | DBEntryNameAlreadyExistsException e) {
            // topic doesn't need to be inserted because it's already in database
            try {
                testTopic = topicHandler.getByName(TestLibrary.TOPIC_NAME);
            } catch (NoSuchDBEntryException ex) {
                ex.printStackTrace();
                TestLibrary.criterionFailPrint(criterionNum, "Test Topic not found in database");
                return false;
            }
        }

        // follows the topic
        followTopicController.addFollowedTopic("\"" + newUser.getUsername() + ", " + testTopic.getName() + ", " + "true\"");

        // determines if topic is now followed
        boolean passedTest;
        try {
            User testUser = userHandler.getByName(newUser.getUsername());

            passedTest = testUser.getProfile().isFollowingTopic(testTopic);
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(criterionNum, "Test User not found in database");
            return false;
        }

        // clears the effect of this test (unfollows the test topic)
        if (undoFollowingAfterTest)
            followTopicController.addFollowedTopic("\"" + newUser.getUsername() + ", " + testTopic.getName() + ", " + "false\"");

        return passedTest;
    }

    public int numPostsInTopic(int criterionNum, String topicName) {
        try {
            return topicHandler.getByName(topicName).getTopicPosts().size();
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(criterionNum, "Test Topic {" + topicName + "} not found in database");
            return -1;
        }
    }

    public static void userStoryTestStartPrint(int userStoryNum) {
        System.out.println("\n\n---------- User Story " + userStoryNum + " ----------");
    }

    public static boolean userStoryTestEndPrint(int userStoryNum, int numTestsPassed, int totalTests) {
        boolean testPassed = numTestsPassed == totalTests;
        System.out.println("--- User Story " + userStoryNum + " [" + (testPassed ? "PASSED" : "FAILED") + "] (" + numTestsPassed + "/" + totalTests + ") ---");

        return testPassed;
    }

    public static void criterionFailPrint(int criterionNum, String failureDescription) {
        System.out.println("-[FAILED] - Criterion " + criterionNum + " - " + failureDescription);
    }

    public static void criterionSucceedPrint(int criterionNum) {
        System.out.println("-[PASSED] - Criterion " + criterionNum);
    }

    public static TestLibrary createNewInstance(UserDataHandler userHandler,
                                          TopicDataHandler topicHandler,
                                          PostDataHandler postHandler,
                                          CommentDataHandler commentHandler,
                                          addFollowedTopic followTopicController) {
        return new TestLibrary(userHandler, topicHandler, postHandler, commentHandler, followTopicController);
    }

    public static String getCreatePostString(String topicName, String postName, String postInfo, boolean anonymous, String imageString, String username) {
        String anonymousString = anonymous ? "true" : "false";
        return String.format("\"%s, %s, %s, %s, %s, %s\"", topicName, postName, postInfo, anonymousString, imageString, username);
    }

    public static String getCreateCommentString(String commentText, String creatorName, int postId) {
        return String.format("%s, %s, %d", commentText, creatorName, postId);
    }

    public static String getRemoveCommentString(int commentId, int postId) {
        return String.format("%d, %d", commentId, postId);
    }

    public static String getCreateReactionString(int postId, String username, ReactionType reaction) {
        return String.format("true, %d, %s, %s", postId, username, reaction.toString());
    }

    public static String getRemoveReactionString(String username, int postId) {
        return String.format("%s, %d", username, postId);
    }

    public static String getFollowUserString(String userToFollow, String userWhoIsFollowing, boolean shouldFollow) {
        return String.format("%s, %s, %s", userToFollow, userWhoIsFollowing, shouldFollow);
    }

    public static String getFollowTopicString(String username, String topicName, boolean shouldFollow) {
        return String.format("\"%s, %s, %s\"", username, topicName, shouldFollow);
    }

    public static String getBlockUserString(String userToBlock, String userWhoIsBlocking, boolean shouldFollow) {
        return String.format("%s, %s, %s", userToBlock, userWhoIsBlocking, shouldFollow);
    }

    public void removeAllUsers() {
        userHandler.deleteAll();
        /*List<User> allUsers = userHandler.getAllUsers();

        for (User u : allUsers) {
            try {
                userHandler.remove_incomplete(u.getUsername());
            } catch (NoSuchDBEntryException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void removeAllTopics() {
        topicHandler.deleteAll();
        /*List<Topic> allTopics = topicHandler.getAll();

        for (Topic t : allTopics) {
            topicHandler.remove(t);
        }*/
    }

    public void removeAllPosts() {
        postHandler.deleteAll();
        /*List<Post> allPosts = postHandler.getAll();

        for (Post p : allPosts) {
            try {
                postHandler.remove_incomplete(p.getId());
            } catch (NoSuchDBEntryException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void removeAllComments() {
        commentHandler.deleteAll();
        /*List<Comment> allComments = commentHandler.getAll();

        for (Comment c : allComments) {
            try {
                commentHandler.remove(c);
            } catch (NoSuchDBEntryException e) {
                e.printStackTrace();
            }
        }*/
    }
}
