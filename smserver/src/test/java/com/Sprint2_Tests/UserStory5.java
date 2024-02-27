package com.Sprint2_Tests;

import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Reaction.Reaction;
import com.Reaction.ReactionType;
import com.Topic.Topic;
import com.User.User;
import com.requestHandler.reactionController;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserStory5 {
    private static final int USER_STORY_NUM = 5;

    PostDataHandler postHandler;
    reactionController reactioncontroller;
    TestLibrary testLibrary;

    public boolean testAll() {
        TestLibrary.userStoryTestStartPrint(USER_STORY_NUM);

        int totalTests = 3;
        int numTestsPassed = 0;
        numTestsPassed += test1_noUI() ? 1 : 0;
        numTestsPassed += test2_noUI() ? 1 : 0;
        numTestsPassed += test4_noUI() ? 1 : 0;

        return TestLibrary.userStoryTestEndPrint(USER_STORY_NUM, numTestsPassed, totalTests);
    }

    // Acceptance Criterion 1 (full testing requires UI)
    public boolean test1_noUI() {
        final ReactionType REACTION_TYPE_TO_ADD = ReactionType.LIKE;

        User testUser = testLibrary.createTestUser();
        Topic testTopic = testLibrary.createTestTopic(testUser.getUsername());

        // create test post
        Post newPost = new Post(testUser.getUsername(), testTopic.getName(), "Test Post Title", false, false, false);

        // inserts post to database
        try {
            postHandler.insert(newPost);
        } catch (DBEntryAlreadyExistsException | NoSuchDBEntryException e) {
            e.printStackTrace();
            return false;
        }

        // create interaction on test post
        reactioncontroller.postReactions(TestLibrary.getCreateReactionString(newPost.getId(), testUser.getUsername(), REACTION_TYPE_TO_ADD));

        // retrieves newPost from the database after it should have been commented to
        try {
            newPost = postHandler.getById(newPost.getId());
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(1, "Newly created test post not found in database");
            return false;
        }

        Reaction currentReaction = newPost.getReactionByCreator(testUser.getUsername());

        if (currentReaction == null || !REACTION_TYPE_TO_ADD.equals(currentReaction.getType())) {
            TestLibrary.criterionFailPrint(1, "reactioncontroller.postReactions() didn't add reaction to post with id: " + newPost.getId());
            return false;
        }

        TestLibrary.criterionSucceedPrint(1);
        return true;
    }

    // Acceptance Criterion 2 (full testing requires UI)
    public boolean test2_noUI() {
        final ReactionType REACTION_TYPE_TO_ADD = ReactionType.LIKE;

        User testUser = testLibrary.createTestUser();
        Topic testTopic = testLibrary.createTestTopic(testUser.getUsername());

        // create test post with reaction
        Post newPost = new Post(testUser.getUsername(), testTopic.getName(), "Test Post Title", false, false, false);
        newPost.addInteraction(new Reaction(REACTION_TYPE_TO_ADD, testUser.getUsername(), newPost.getId()));

        // inserts post to database
        try {
            postHandler.insert(newPost);
        } catch (DBEntryAlreadyExistsException | NoSuchDBEntryException e) {
            e.printStackTrace();
            return false;
        }

        // try to create interaction on test post that already has the same interaction
        reactioncontroller.postReactions(TestLibrary.getCreateReactionString(newPost.getId(), testUser.getUsername(), REACTION_TYPE_TO_ADD));
        // test response to see if it failed because the user already had that reaction

        // retrieves newPost from the database after it should have been commented to
        try {
            newPost = postHandler.getById(newPost.getId());
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            TestLibrary.criterionFailPrint(2, "Newly created test post not found in database");
            return false;
        }

        Reaction currentReaction = newPost.getReactionByCreator(testUser.getUsername());

        if (currentReaction == null || !REACTION_TYPE_TO_ADD.equals(currentReaction.getType())) {
            TestLibrary.criterionFailPrint(2, "reactioncontroller.postReactions() removed reaction from post with id: " + newPost.getId());
            return false;
        }

        TestLibrary.criterionSucceedPrint(2);
        return true;
    }

    // Acceptance Criterion 4 (full testing requires UI)
    public boolean test4_noUI() {
        User testUser = testLibrary.createTestUser();
        Topic testTopic = testLibrary.createTestTopic(testUser.getUsername());

        // create test post
        Post newPost = new Post(testUser.getUsername(), testTopic.getName(), "Test Post Title", false, false, false);

        // inserts post to database
        try {
            postHandler.insert(newPost);
        } catch (DBEntryAlreadyExistsException | NoSuchDBEntryException e) {
            e.printStackTrace();
            return false;
        }

        long timeToBeat = 3000; // creating reaction in less than 3 sec
        long startTime = System.currentTimeMillis();

        // Creates new reaction
        reactioncontroller.postReactions(TestLibrary.getCreateReactionString(newPost.getId(), testUser.getUsername(), ReactionType.LIKE));

        long endTime = System.currentTimeMillis();

        long timeDiff = endTime - startTime;
        boolean passedTest = timeDiff < timeToBeat;

        if (!passedTest)
            TestLibrary.criterionFailPrint(4, "new reaction creation time (" + timeDiff + " > " + timeToBeat + ")");

        TestLibrary.criterionSucceedPrint(4);
        return passedTest;
    }
}