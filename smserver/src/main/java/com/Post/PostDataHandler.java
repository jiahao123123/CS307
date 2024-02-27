package com.Post;

import com.Comment.Comment;
import com.Comment.CommentRepository;
import com.DbSequencer.IdSequenceObject;
import com.DbSequencer.IdSequencer;
import com.Exception.DBEntryAlreadyExistsException;
import com.Exception.NoSuchDBEntryException;
import com.Profile.Profile;
import com.Reaction.Reaction;
import com.Reaction.ReactionType;
import com.Topic.Topic;
import com.Topic.TopicDataHandler;
import com.Topic.TopicRepository;
import com.User.User;
import com.User.UserDataHandler;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.util.BsonUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
// This class is used to service incoming requests related to Post data.
// It uses repositories to get or set data and sends the result back to the server code
public class PostDataHandler {

    private UserDataHandler userHandler;
    private TopicDataHandler topicHandler;
    private PostRepository postRepository;
    private TopicRepository topicRepository;
    private IdSequencer idSequencer;

    /***
     *
     * Also adds post to the associated topic's posts and updates that topic's database entry.
     * @param post - The post to be inserted
     * @return The newly inserted post (null if error)
     *
     */
    public Post insert(Post post) throws DBEntryAlreadyExistsException, NoSuchDBEntryException {
        if (post.getId() < IdSequencer.START_ID_VALUE) {
            int newID = idSequencer.getNextId(IdSequencer.Collection.POST);
            IdSequencer.lastPostId = newID;
            post.setId(newID);
        }

        if (postRepository.getById(post.getId()).isPresent()) {
            System.out.println("FAILED insert post to database.  Post already exists.     " + post);
            throw new DBEntryAlreadyExistsException("Post with id: " + post.getId() + " already exists");
        }
        Optional<Topic> topicToBeAddedToOptional = topicRepository.getByName(post.getTopicName());
        if (topicToBeAddedToOptional.isEmpty()) {
            System.out.println("FAILED insert post to database.  Post's Topic does not exist.");
            throw new NoSuchDBEntryException("No Topic found with name: " + post.getTopicName()); // No such topic
        }
        Topic topicToBeAddedTo = topicToBeAddedToOptional.get();
        Topic topicToBeAddedTo_oldVersion = topicToBeAddedTo;

        if (topicToBeAddedTo.contains(post)) {
            System.out.println("FAILED insert post to database.  Post already added to topic.     " + post);
            throw new DBEntryAlreadyExistsException("Post with id: " + post.getId() + " already exists in topic: " + post.getTopicName());
        }
        topicToBeAddedTo.addTopicPost(post);
        topicRepository.save(topicToBeAddedTo);

        // updates database entries for all users following this topic
        updateDatabaseForUsersFollowingTopic(topicToBeAddedTo_oldVersion, topicToBeAddedTo);

        User creator = userHandler.getByName((post.getCreatorName()));
        creator.getProfile().addPost(post);
        userHandler.save(creator);

        return postRepository.insert(post);
    }

    // only removes Post from the postRepository, its topic, and from the creator user's posts list,
    // without full cleanup that includes removing all comments on this post
    //
    // must ONLY be called by CommentController.removePost()
    public void remove_incomplete(int postId) throws NoSuchDBEntryException {
        Post post = getById(postId);

        User creator = userHandler.getByName(post.getCreatorName());
        creator.getProfile().removePost(post);
        userHandler.save(creator);

        postRepository.delete(post);

        updateDatabaseForPostAndItsTopic(post, "removing post", true);
    }

    public ArrayList<Post> getPostsCreatedByUser(User user) {
        ArrayList<Post> createdPosts;

        Optional<ArrayList<Post>> createdPostsOptional = postRepository.getPostsByCreatorName(user.getUsername());

        if (createdPostsOptional.isPresent()) // User has created at least one post
            createdPosts = createdPostsOptional.get();
        else
            createdPosts = new ArrayList<>(); // User has created no post

        return createdPosts;
    }

    public boolean exists(Post post){
        return postRepository.getById(post.getId()).isPresent();
    }

    // adds reaction to post.interactions and updates the entry in the database
    // changes User's reaction if they already reacted to that post
    public void addReaction(Post post, ReactionType reactionType, User reactionCreator){
        Reaction currentReaction = post.getReactionByCreator(reactionCreator.getUsername());

        if (currentReaction == null) {
            Reaction newReaction = new Reaction(reactionType, reactionCreator.getUsername(), post.getId());

            User creator;
            try {
                creator = userHandler.getByName(reactionCreator.getUsername());
            } catch (NoSuchDBEntryException e) {
                System.out.println("SKIPPED adding reaction from post: " + post + "   because UserCreator: " + reactionCreator.getUsername() + "   does not exist in database");
                return;
            }
            creator.getProfile().addInteraction(newReaction);
            userHandler.save(creator);

            post.addInteraction(newReaction);
            save(post);
        }
        else {
            currentReaction.setType(reactionType);

            try {
                User user = userHandler.getByName(currentReaction.getCreatorName());
                user.getProfile().changeInteraction(currentReaction);
                userHandler.save(user);
            } catch (NoSuchDBEntryException e) {
                System.out.println("PostDataHandler.addReaction() FAILED to update database because the user " + currentReaction.getCreatorName() + " does not exist");
                e.printStackTrace();
            }

            post.changeReactionInList(currentReaction);
            save(post);
        }
        updateDatabaseForPostAndItsTopic(post, "adding reaction", false);
    }

    // removes reaction from post.interactions and updates the entry in the database
    public void removeReaction(Post post, String creatorName){
        Reaction reaction = post.getReactionByCreator(creatorName);

        if (reaction == null) {
            System.out.println("SKIPPED removing reaction from post: " + post + "   because UserCreator: " + creatorName + "   did not react to that Post");
            return;
        }

        User creator;
        try {
            creator = userHandler.getByName(creatorName);
        } catch (NoSuchDBEntryException e) {
            System.out.println("SKIPPED removing reaction from post: " + post + "   because UserCreator: " + creatorName + "   does not exist in database");
            return;
        }
        creator.getProfile().removeInteraction(reaction);
        userHandler.save(creator);

        post.removeInteraction(reaction);
        save(post);

        updateDatabaseForPostAndItsTopic(post, "removing reaction", false);
    }

    public Post getById(Integer id) throws NoSuchDBEntryException {
        Optional<Post> foundUserOptional = postRepository.getById(id);

        if (foundUserOptional.isEmpty())
            throw new NoSuchDBEntryException("No Post found with id: " + id);  // exception if no Post found

        return foundUserOptional.get();
    }

    public Post save(Post post) {
        Post updatedPost = postRepository.save(post);
        updateDatabaseForPostAndItsTopic(post, "Updating post DB entry", false);
        return updatedPost;
    }

    // returns the updated Topic Object
    private Topic updateDatabaseForPostAndItsTopic(Post post, String operationDescription, boolean removingPost) {
        // updates the post in the database
        if (!removingPost)
            postRepository.save(post);

        // updates the topic in the database
        Topic topicToBeAddedTo = null;
        try {
            topicToBeAddedTo = topicHandler.getByName(post.getTopicName());
            Topic topicToBeAddedTo_oldVersion = topicToBeAddedTo;

            if (!removingPost)
                topicToBeAddedTo.updateTopicPostWithID(post);
            else
                topicToBeAddedTo.removeTopicPost(post);

            topicHandler.save(topicToBeAddedTo);

            // updates database entries for all users following this topic
            updateDatabaseForUsersFollowingTopic(topicToBeAddedTo_oldVersion, topicToBeAddedTo);
        } catch (NoSuchDBEntryException e) {
            e.printStackTrace();
            System.out.println("FAILED update to topic after " + operationDescription +
                                ", no topic found with name: " + post.getTopicName());
        }

        return topicToBeAddedTo;
    }

    private void updateDatabaseForUsersFollowingTopic(Topic oldTopicObject, Topic updatedTopicObject) {
        List<User> allUsers = userHandler.getAllUsers();//.getAllProfiles();

        for (User user : allUsers) {
            Profile profile = user.getProfile();
            Topic foundTopic = profile.getFollowingTopic(oldTopicObject);
            if (foundTopic != null) {
                profile.updateFollowingTopic(updatedTopicObject);
//                userHandler.save(cdprofile);

                /*try {
                    User user = userHandler.getByName(profile.getName());*/
                    user.setProfile(profile);
                    userHandler.save(user);
                /*} catch (NoSuchDBEntryException e) {
                    e.printStackTrace();
                    System.out.println("FAILED updating user obj with update profile: {" + profile + "} after udpating followedTopic: {"
                            + updatedTopicObject + "} because no user found with username: " + profile.getName());
                    return;
                }*/
            }
        }
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public void deleteAll() {
        postRepository.deleteAll();
    }
}