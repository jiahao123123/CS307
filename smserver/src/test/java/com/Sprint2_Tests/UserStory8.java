package com.Sprint2_Tests;

import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Reaction.Reaction;
import com.Reaction.ReactionType;
import com.Topic.Topic;
import com.User.User;
import com.requestHandler.removeReaction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserStory8 {
    private static final int USER_STORY_NUM = 8;

    PostDataHandler postHandler;
    removeReaction removeReactionController;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 2;
        int numTestsPassed = 0;
        numTestsPassed += test1() ? 1 : 0;
        numTestsPassed += test3_noUI() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 1
    public boolean test1() {
        final ReactionType REACTION_TYPE_TO_ADD = ReactionType.LIKE;

        User testUser = testLibrary.createTestUser();
        Topic testTopic = testLibrary.createTestTopic(testUser.getUsername());

        // create test post
        Post newPost = new Post(testUser.getUsername(), testTopic.getName(), "Test Post Title", false, false, false);
        newPost.addInteraction(new Reaction(REACTION_TYPE_TO_ADD, testUser.getUsername(), newPost.getId()));

        // inserts post to database
        try {
            postHandler.insert(newPost);
        } catch (DBEntryAlreadyExistsException | NoSuchDBEntryException e) {
            e.printStackTrace();
            return false;
        }

        // remove interaction from test post
        removeReactionController.removeReaction(TestLibrary.getRemoveReactionString(TestLibrary.USERNAME, newPost.getId()));

        // retrieves newPost from the database after it should have been commented to
        try {
            newPost = postHandler.getById(newPost.getId());
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(1, "Newly created test post not found in database");
            return false;
        }

        Reaction currentReaction = newPost.getReactionByCreator(testUser.getUsername());

        if (currentReaction != null) {
            TestLibrary.criterionFailPrint(1, "removeReactionController.removeReaction() didn't remove reaction from post with id: " + newPost.getId());
            return false;
        }

        TestLibrary.criterionSucceedPrint(1);
        return true;
    }

    // Acceptance Criterion 3 (full testing requires UI)
    public boolean test3_noUI() {
        final ReactionType REACTION_TYPE_TO_ADD = ReactionType.LIKE;

        User testUser = testLibrary.createTestUser();
        Topic testTopic = testLibrary.createTestTopic(testUser.getUsername());

        // create test post
        Post newPost = new Post(testUser.getUsername(), testTopic.getName(), "Test Post Title", false, false, false);
        newPost.addInteraction(new Reaction(REACTION_TYPE_TO_ADD, testUser.getUsername(), newPost.getId()));

        // inserts post to database
        try {
            postHandler.insert(newPost);
        } catch (DBEntryAlreadyExistsException | NoSuchDBEntryException e) {
            e.printStackTrace();
            return false;
        }

        long timeToBeat = 3000; // deleting reaction in less than 3 sec
        long startTime = System.currentTimeMillis();

        // Creates new post
        removeReactionController.removeReaction(TestLibrary.getRemoveReactionString(TestLibrary.USERNAME, newPost.getId()));

        long endTime = System.currentTimeMillis();

        long timeDiff = endTime - startTime;
        boolean passedTest = timeDiff < timeToBeat;

        if (!passedTest)
            TestLibrary.criterionFailPrint(4, "new reaction removal time (" + timeDiff + " > " + timeToBeat + ")");

        TestLibrary.criterionSucceedPrint(3);
        return passedTest;
    }
}
