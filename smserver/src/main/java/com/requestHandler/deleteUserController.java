package com.requestHandler;


import com.Comment.Comment;
import com.Comment.CommentDataHandler;
import com.Exception.NoSuchDBEntryException;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Reaction.Reaction;
import com.User.User;
import com.User.UserDataHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@AllArgsConstructor
@RequestMapping("/deleteUser")
public class deleteUserController {

    UserDataHandler userHandler;
    PostDataHandler postHandler;
    CommentDataHandler commentHandler;

    @PostMapping
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity deleteUser(@RequestBody String s) {
        //String s is passed in as: username
        System.out.println("Delete user:" + s);
        User user;
        try {
            user = userHandler.getByName(s);
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Error: Database");
        }

        // removes all comments that the user made
        for (int commentId : user.getProfile().getCommentIds()) {
            Comment curComment;
            try {
                curComment = commentHandler.getById(commentId);
            } catch (NoSuchDBEntryException e) {
                //e.printStackTrace();
                System.out.println("commentId: " + commentId + " in User (" + user.getUsername() + ")'s commentId list is not in database");
                continue;
            }

            try {
                commentHandler.remove(curComment);
            } catch (NoSuchDBEntryException e) {
                e.printStackTrace();
                continue;
            }
        }

        // removes all reactions that the user made
        for (Reaction reaction : user.getProfile().getInteractions()) {
            Post postReactedTo;
            try {
                postReactedTo = postHandler.getById(reaction.getPostId());
                postHandler.removeReaction(postReactedTo, reaction.getCreatorName());
            } catch (NoSuchDBEntryException e) {
                System.out.println("FAILED removing reaction: " + reaction + " while deleting user: " + user.getUsername());
                e.printStackTrace();
                continue;
            }
        }

        // removes all posts that the user made
        for (Post post : user.getProfile().getPosts()) {
            try {
                commentHandler.removePost(post);
            } catch (NoSuchDBEntryException e) {
                System.out.println("FAILED removing post: " + post + " while deleting user: " + user.getUsername());
                e.printStackTrace();
                continue;
            }
        }

        // unfollows all users
        ArrayList<String> followedUsers = user.getProfile().getUserFollowing();
        for (String curUsername : followedUsers) {
            try {
                User curFollowedUser = userHandler.getByName(curUsername);

                user.getProfile().removeFollowing(curFollowedUser.getUsername());
                curFollowedUser.getProfile().removeFollower(user.getUsername());
                userHandler.save(user);
                userHandler.save(curFollowedUser);
            } catch (NoSuchDBEntryException e) {
                System.out.println("FAILED removing followedUser: " + curUsername + " in  deleteUserController because not found in DB");
                e.printStackTrace();
            }
        }
        ArrayList<String> followers = user.getProfile().getUserFollowers();
        for (String curUsername : followers) {
            try {
                User curFollowedUser = userHandler.getByName(curUsername);

                user.getProfile().removeFollower(curFollowedUser.getUsername());
                curFollowedUser.getProfile().removeFollowing(user.getUsername());
                userHandler.save(user);
                userHandler.save(curFollowedUser);
            } catch (NoSuchDBEntryException e) {
                System.out.println("FAILED removing follower: " + curUsername + " in  deleteUserController because not found in DB");
                e.printStackTrace();
            }
        }

        // unblocks all users
        ArrayList<String> blockedUsers = user.getProfile().getUsersThisUserHasBlocked();
        for (String curUsername : blockedUsers) {
            try {
                User curBlockedUser = userHandler.getByName(curUsername);

                user.getProfile().removeUsersThisUserHasBlocked(curBlockedUser.getUsername());
                curBlockedUser.getProfile().removeUsersThatBlockedThisUser(user.getUsername());
                userHandler.save(user);
                userHandler.save(curBlockedUser);
            } catch (NoSuchDBEntryException e) {
                System.out.println("FAILED removing blockedUser: " + curUsername + " in  deleteUserController because not found in DB");
                e.printStackTrace();
            }
        }
        ArrayList<String> usersThatBlockedYou = user.getProfile().getUsersThisUserHasBlocked();
        for (String curUsername : usersThatBlockedYou) {
            try {
                User curBlockedUser = userHandler.getByName(curUsername);

                user.getProfile().removeUsersThatBlockedThisUser(curBlockedUser.getUsername());
                curBlockedUser.getProfile().removeUsersThisUserHasBlocked(user.getUsername());
                userHandler.save(user);
                userHandler.save(curBlockedUser);
            } catch (NoSuchDBEntryException e) {
                System.out.println("FAILED removing userThatBlockedYou: " + curUsername + " in  deleteUserController because not found in DB");
                e.printStackTrace();
            }
        }

        // removes all dms
        userHandler.removeDmsWithUser(user);

        // removes the user
        try {
            userHandler.remove_incomplete(user.getUsername());
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Error: Database");
        }
        System.out.println("deleted: " + s);
        //Client isn't really expecting anything, so just return the OK status
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
