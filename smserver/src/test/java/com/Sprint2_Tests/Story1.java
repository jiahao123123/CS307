package com.Sprint2_Tests;

import com.ClientUserRequest.Request;
import com.Comment.CommentDataHandler;
import com.DbSequencer.IdSequencer;
import com.Exception.NoSuchDBEntryException;
import com.Post.PostDataHandler;
import com.Profile.Profile;
import com.ProfileContent.Sex;
import com.Reaction.ReactionType;
import com.Topic.TopicDataHandler;
import com.User.User;
import com.User.UserDataHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.requestHandler.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@AllArgsConstructor
public class Story1 {
    private static final int USER_STORY_NUM = 1;

    topicController topiccontroller;
    postController postcontroller;
    addCommentController addcommentController;
    reactionController reactioncontroller;
    deleteUserController deleteuserController;
    UserDataHandler userHandler;
    PostDataHandler postHandler;
    CommentDataHandler commentHandler;
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
        final String USER_NAME = "DeleteUser";
        final String TOPIC_NAME = "Delete Test Topic";

        // creates the data to be deleted
        testLibrary.createTestUser(USER_NAME, "deleteUser@email.com");
        topiccontroller.createTopicController("TOPIC_NAME, " + USER_NAME);
        postcontroller.createPostController(TestLibrary.getCreatePostString(TOPIC_NAME, "Post1", "info 1", false, " ", USER_NAME));
        addcommentController.addComment(TestLibrary.getCreateCommentString("text1", USER_NAME, IdSequencer.lastPostId));
        reactioncontroller.postReactions(TestLibrary.getCreateReactionString(IdSequencer.lastPostId, USER_NAME, ReactionType.LIKE));

        // deletes the user
        deleteuserController.deleteUser(USER_NAME);

        // ensures that data is deleted
        try {
            userHandler.getByName(USER_NAME);
            TestLibrary.criterionFailPrint(2, "User not deleted");
            return false;
        } catch (NoSuchDBEntryException e) { }
        try {
            postHandler.getById(IdSequencer.lastPostId);
            TestLibrary.criterionFailPrint(2, "Post not deleted");
            return false;
        } catch (NoSuchDBEntryException e) { }
        try {
            commentHandler.getById(IdSequencer.lastCommentId);
            TestLibrary.criterionFailPrint(2, "Comment not deleted");
            return false;
        } catch (NoSuchDBEntryException e) { }

        TestLibrary.criterionSucceedPrint(2);
        return true;
    }
}
