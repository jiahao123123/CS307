package com.example.cs307_purduecircle;

import com.Comment.CommentDataHandler;
import com.Post.PostDataHandler;
import com.Sprint2_Tests.*;
import com.Topic.TopicDataHandler;
import com.User.UserDataHandler;
import com.User.UserTest;
import com.requestHandler.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest//(classes = {UserTest.class})
public class Cs307PurdueCircleApplicationTests {
    @Autowired
    UserDataHandler userHandler;
    @Autowired
    TopicDataHandler topicHandler;
    @Autowired
    PostDataHandler postHandler;
    @Autowired
    CommentDataHandler commentHandler;
    @Autowired
    postController postcontroller;
    @Autowired
    addCommentController addcommentController;
    @Autowired
    addFollowedTopic followTopicController;
    @Autowired
    timelineController timelinecontroller;
    @Autowired
    removeReaction removeReactionController;
    @Autowired
    removeComment removeCommentController;
    @Autowired
    reactionController reactioncontroller;
    @Autowired
    profileController profilecontroller;
    @Autowired
    signupController signupcontroller;
    @Autowired
    loginController logincontroller;
    @Autowired
    topicController topiccontroller;
    @Autowired
    deleteUserController deleteuserController;
    @Autowired
    addFollowedUser addfollowedUser;
    @Autowired
    profileInfoController profileinfoController;
    @Autowired
    addFollowedTopic addfollowedTopic;
    @Autowired
    addBlockedUser addblockedUser;
    @Autowired
    searchUserController searchuserController;
    @Autowired
    userlineController userlinecontroller;

    boolean isInitialized;
    TestLibrary testLibrary;

    public void initializeTests() {
        if (isInitialized)
            return;

        testLibrary = TestLibrary.createNewInstance(userHandler, topicHandler, postHandler, commentHandler, followTopicController);
        isInitialized = true;
    }

    // ---------- User Story 1 ---------- //
    @Test
    public void Story1Test() {
        initializeTests();
        assertTrue((new Story1(topiccontroller, postcontroller, addcommentController, reactioncontroller,
                                deleteuserController, userHandler, postHandler, commentHandler, testLibrary)).testAll());
    }

    /*// ---------- User Story 2 ---------- //
    @Test
    public void Story2Test() {
        initializeTests();
        assertTrue((new Story2()).testAll());
    }*/

    // ---------- User Story 3 ---------- //
    @Test
    public void Story3Test() {
        initializeTests();
        assertTrue((new Story3(addfollowedUser, userHandler, testLibrary)).testAll());
    }

    // ---------- User Story 4 ---------- //
    @Test
    public void Story4Test() {
        initializeTests();
        assertTrue((new Story4(profileinfoController, userHandler, testLibrary)).testAll());
    }

    // ---------- User Story 5 ---------- //
    @Test
    public void Story5Test() {
        initializeTests();
        assertTrue((new Story5(addfollowedUser, addfollowedTopic, userHandler, testLibrary)).testAll());
    }

    // ---------- User Story 6 ---------- //
    @Test
    public void Story6Test() {
        initializeTests();
        assertTrue((new Story6(profileinfoController, postcontroller, userHandler, postHandler, testLibrary)).testAll());
    }

    // ---------- User Story 7 ---------- //
    @Test
    public void Story7Test() {
        initializeTests();
        assertTrue((new Story7(timelinecontroller, addfollowedTopic, addfollowedUser, testLibrary)).testAll());
    }

    // ---------- User Story 8 ---------- //
    @Test
    public void Story8Test() {
        initializeTests();
        assertTrue((new Story8(postcontroller, reactioncontroller, postHandler, testLibrary)).testAll());
    }

    // ---------- User Story 9 ---------- //
    @Test
    public void Story9Test() {
        initializeTests();
        assertTrue((new Story9(addblockedUser, userHandler, testLibrary)).testAll());
    }

    // ---------- User Story 10 ---------- //
    @Test
    public void Story10Test() {
        initializeTests();
        assertTrue((new Story10(searchuserController, userHandler, testLibrary)).testAll());
    }

    // ---------- User Story 11 ---------- //
    @Test
    public void Story11Test() {
        initializeTests();
        assertTrue((new Story11(userlinecontroller, testLibrary)).testAll());
    }






    // SPRINT 2

    /*// ---------- User Story 1 ---------- //
    @Test
    public void userStory1Test() {
        initializeTests();

        //signupcontroller.signupController("{\"username\":\"Hello Test User\",\"email\":\"helloTestUser@gmail.com\",\"hash\":\"823948298as\"}");
        //logincontroller.loginController("{\"username\":\"Hello Test User\", \"hash\":\"823948298as\"}");

        //testLibrary.removeAllComments();
        //testLibrary.removeAllPosts();
        //testLibrary.removeAllTopics();
        //testLibrary.removeAllUsers();


        assertTrue((new UserStory1(userHandler, profilecontroller, testLibrary)).testAll());
    }

    // ---------- User Story 2 ---------- //
    @Test
    public void userStory2Test() {
        initializeTests();
        assertTrue((new UserStory2(postcontroller, testLibrary)).testAll());
    }

    // ---------- User Story 3 ---------- //
    @Test
    public void userStory3Test() {
        initializeTests();
        assertTrue((new UserStory3(topicHandler, postHandler, testLibrary)).testAll());
    }

    // ---------- User Story 4 ---------- //
    @Test
    public void userStory4Test() {
        initializeTests();
        assertTrue((new UserStory4(postHandler, postcontroller, addcommentController, testLibrary)).testAll());
    }

    // ---------- User Story 5 ---------- //
    @Test
    public void userStory5Test() {
        initializeTests();
        assertTrue((new UserStory5(postHandler, reactioncontroller, testLibrary)).testAll());
    }

    // ---------- User Story 6 ---------- //
    @Test
    public void userStory6Test() {
        initializeTests();
        assertTrue((new UserStory6(followTopicController, userHandler, topicHandler, testLibrary)).testAll());
    }

    // ---------- User Story 7 ---------- //
    @Test
    public void userStory7Test() {
        initializeTests();
        assertTrue((new UserStory7(timelinecontroller, testLibrary)).testAll());
    }

    // ---------- User Story 8 ---------- //
    @Test
    public void userStory8Test() {
        initializeTests();
        assertTrue((new UserStory8(postHandler, removeReactionController, testLibrary)).testAll());
    }


    // ---------- User Story 9 ---------- //
    @Test
    public void userStory9Test() {
        initializeTests();
        assertTrue((new UserStory9(postHandler, commentHandler, removeCommentController, testLibrary)).testAll());
    }*/
}