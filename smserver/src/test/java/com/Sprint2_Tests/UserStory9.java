package com.Sprint2_Tests;

import com.Comment.Comment;
import com.Comment.CommentDataHandler;
import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Reaction.Reaction;
import com.Reaction.ReactionType;
import com.Topic.Topic;
import com.User.User;
import com.requestHandler.removeComment;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserStory9 {
    private static final int USER_STORY_NUM = 9;

    PostDataHandler postHandler;
    CommentDataHandler commentHandler;
    removeComment removeCommentController;
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
        User testUser = testLibrary.createTestUser();
        Topic testTopic = testLibrary.createTestTopic(testUser.getUsername());

        // create test post
        Post newPost = new Post(testUser.getUsername(), testTopic.getName(), "Test Post Title", false, false, false);

        // gets number of comments on this post
        int startNumComments = newPost.getComments().size();

        // inserts post to database
        try {
            postHandler.insert(newPost);
        } catch (DBEntryAlreadyExistsException | NoSuchDBEntryException e) {
            e.printStackTrace();
            return false;
        }

        // add comment to test post
        Comment testComment;
        try {
            testComment = commentHandler.insert(new Comment("testText", testUser.getUsername(), newPost.getId()));
        } catch (DBEntryAlreadyExistsException | NoSuchDBEntryException e) {
            e.printStackTrace();
            return false;
        }

        // remove comment from test post
        removeCommentController.removeComment(TestLibrary.getRemoveCommentString(testComment.getId(), testComment.getPostId()));

        // retrieves newPost from the database after it should have been commented to
        try {
            newPost = postHandler.getById(newPost.getId());
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(1, "Newly created test post not found in database");
            return false;
        }

        int curNumComments = newPost.getComments().size();

        if (startNumComments != curNumComments) {
            TestLibrary.criterionFailPrint(1, "removeCommentController.removeComment() didn't remove comment from post with id: " + newPost.getId());
            return false;
        }

        TestLibrary.criterionSucceedPrint(1);
        return true;
    }
}