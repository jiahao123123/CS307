package com.Sprint2_Tests;

import com.Comment.Comment;
import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Topic.Topic;
import com.User.User;
import com.requestHandler.addCommentController;
import com.requestHandler.postController;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserStory4 {
    private static final int USER_STORY_NUM = 4;

    PostDataHandler postHandler;
    postController postcontroller;
    addCommentController addcommentController;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 2;
        int numTestsPassed = 0;
        numTestsPassed += test1_noUI() ? 1 : 0;
        numTestsPassed += test3() ? 1 : 0;

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

        // create comment on test post
        addcommentController.addComment(TestLibrary.getCreateCommentString("thisIsTheCommentText", TestLibrary.USERNAME, newPost.getId()));

        // retrieves newPost from the database after it should have been commented to
        try {
            newPost = postHandler.getById(newPost.getId());
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(1, "Newly created test post not found in database");
            return false;
        }

        int curNumComments = newPost.getComments().size();

        if (curNumComments - startNumComments != 1) {
            TestLibrary.criterionFailPrint(1, "commentController.addNewComment() didn't add comment to post with id: " + newPost.getId());
            return false;
        }

        TestLibrary.criterionSucceedPrint(1);
        return true;
    }

    // Acceptance Criterion 3
    public boolean test3() {
        final int NUM_COMMENTS_TO_ADD = 30;

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

        // create comments on test post
        for (int i = 0; i < NUM_COMMENTS_TO_ADD; i++) {
            addcommentController.addComment(TestLibrary.getCreateCommentString("newComment" + i, TestLibrary.USERNAME, newPost.getId()));
        }

        // retrieves newPost from the database after it should have been commented to
        try {
            newPost = postHandler.getById(newPost.getId());
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(3, "Newly created test post not found in database");
            return false;
        }

        int curNumComments = newPost.getComments().size();

        if (curNumComments - startNumComments != NUM_COMMENTS_TO_ADD) {
            TestLibrary.criterionFailPrint(3, "commentController.addNewComment() didn't add comment to post with id: " + newPost.getId());
            return false;
        }

        // ensures that comments are in chronological order (new comment first)
        Comment prevComment = newPost.getComments().remove(0);
        for (Comment comment : newPost.getComments()) {
            if (comment.getCreationTime().isBefore(prevComment.getCreationTime())) {
                TestLibrary.criterionFailPrint(3, "List of post comments {post id: " + newPost.getId() + "} is not in chronological order");
                return false;
            }

            prevComment = comment;
        }

        TestLibrary.criterionSucceedPrint(3);
        return true;
    }
}