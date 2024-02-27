package com.User;

import com.Comment.CommentDataHandler;
import com.DbSequencer.IdSequencer;
import com.Exception.*;
import com.Message.DMRepository;
import com.Message.Message;
import com.Post.Post;
import com.Post.PostDataHandler;
import com.Profile.Profile;
//import com.Profile.ProfileRepository;
import com.ProfileContent.Sex;
import com.Reaction.Reaction;
import com.Topic.Topic;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
// This class is used to service incoming requests.
// It uses repositories to get data and sends the result back to UserController
public class UserDataHandler {

    private UserRepository userRepository;
    //private ProfileRepository profileRepository;
    private DMRepository dmRepository;
    IdSequencer idSequencer;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    /*public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }*/

    public User insert(User user) throws DBEntryIDAlreadyExistsException, DBEntryEmailAlreadyExistsException, DBEntryUsernameAlreadyExists {
        if (userRepository.findUserById(user.getId()).isPresent()) {
            System.out.println("FAILED insert user to database.  User already exists.     " + user);
            throw new DBEntryIDAlreadyExistsException("User entry with id: " + user.getId() + "   already exists");
        }

        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            System.out.println("FAILED insert user to database.  Email is taken.     " + user);
            throw new DBEntryEmailAlreadyExistsException("User entry with email: " + user.getEmail() + "   already exists");
        }

        if (userRepository.findUserByUsername(user.getUsername()).isPresent()) {
            System.out.println("FAILED insert user to database.  Username is taken.     " + user.getUsername());
            throw new DBEntryUsernameAlreadyExists("User entry with username: " + user.getUsername() + "   already exists");
        }

        return userRepository.insert(user);
    }

    public User getByEmail(String email) throws NoSuchDBEntryException {
        Optional<User> foundUserOptional = userRepository.findUserByEmail(email);

        if (foundUserOptional.isEmpty())
            throw new NoSuchDBEntryException("No User found with email: " + email); // exception if no User found

        return foundUserOptional.get();
    }

    public User getByName(String name) throws NoSuchDBEntryException {
        Optional<User> foundUserOptional = userRepository.findUserByUsername(name);

        if (foundUserOptional.isEmpty())
            throw new NoSuchDBEntryException("No User found with name: " + name);  // exception if no User found

        return foundUserOptional.get();
    }

    // only removes the User from the UserRepository, should only be called in the deleteUserController
    // where the rest of the remove functionality is implemented
    public void remove_incomplete(String name) throws NoSuchDBEntryException {
        User user = getByName(name);

        userRepository.delete(user);
    }

    public void setProfileUsername(User user, String newUsername){
        user.getProfile().setName(newUsername);
    }

    public void setProfileDMStatus(User user, boolean dmStatus) {
        user.getProfile().setDMStatus(dmStatus);
    }

    public void setProfileSex(User user, Sex newSex){
        Profile foundProfile = user.getProfile();
        foundProfile.getSex().setContent(newSex);
    }

    public void setProfileSexPublic(User user, boolean isPublicInfo){
        Profile foundProfile = user.getProfile();
        foundProfile.getSex().setPublicInfo(isPublicInfo);
    }

    public void setProfileAge(User user, int newAge){
        Profile foundProfile = user.getProfile();
        foundProfile.getAge().setContent(newAge);
    }

    public void setProfileAgePublic(User user, boolean isPublicInfo){
        Profile foundProfile = user.getProfile();
        foundProfile.getAge().setPublicInfo(isPublicInfo);
    }

    public void setProfileBio(User user, String newBio){
        Profile foundProfile = user.getProfile();
        foundProfile.getBio().setContent(newBio);
    }

    public void setProfileBioPublic(User user, boolean isPublicInfo){
        Profile foundProfile = user.getProfile();
        foundProfile.getBio().setPublicInfo(isPublicInfo);
    }

    public boolean doUsernamePasswordMatch(String username, String password) throws NoSuchDBEntryException {
        if (username == null || password == null)
            return false; // invalid username or password

        /*Optional<Profile> foundProfile = profileRepository.findProfileByName(username);

        if (foundProfile.isEmpty())
            return false; // no such username*/

        Optional<User> foundUser = userRepository.findUserByUsername(username);
        if (foundUser.isEmpty())
            throw new NoSuchDBEntryException("No User found with name: " + username);

        return foundUser.get().getPassword().equals(password);
    }

    public void followUser(User currentUser, User userToBeFollowed) {
        currentUser.getProfile().addUserFollowing(userToBeFollowed.getUsername());
        userToBeFollowed.getProfile().addUserFollowers(currentUser.getUsername());
        save(currentUser);
        save(userToBeFollowed);
    }

    public void unfollowUser(User currentUser, User userToBeUnfollowed) {
        Profile currentProfile = currentUser.getProfile();

        if (!currentProfile.getUserFollowing().contains(userToBeUnfollowed))
        {
            System.out.println("SKIPPED unfollowing User: " + userToBeUnfollowed + "   because User: " + currentUser + "   was not already following that User");
            return;
        }

        currentUser.getProfile().removeFollowing(userToBeUnfollowed.getUsername());
        userToBeUnfollowed.getProfile().removeFollower(currentUser.getUsername());
        save(currentUser);
        save(userToBeUnfollowed);
    }

    public void followTopic(User currentUser, Topic topicToBeFollowed) {
        currentUser.getProfile().addFollowingTopic(topicToBeFollowed);
        save(currentUser);
    }

    public void unfollowTopic(User currentUser, Topic topicToBeUnfollowed) {
        Profile currentProfile = currentUser.getProfile();

        if (!currentProfile.getFollowingTopicsList().contains(topicToBeUnfollowed))
        {
            System.out.println("SKIPPED unfollowing Topic: " + topicToBeUnfollowed + "   because User: " + currentUser + "   was not already following that Topic");
            return;
        }

        currentUser.getProfile().removeFollowingTopic(topicToBeUnfollowed);
        save(currentUser);
    }

    /*  Commented out 3/6/22 because it is unnecessary (at least currently)
    public void setFollowerList(User user, ArrayList<User> newListOfFollower) {
        user.getProfile().setUserFollowers(newListOfFollower);
    }

    public void setFollowingList(User user, ArrayList<User> newListOfFollowing) {
        user.getProfile().setUserFollowing(newListOfFollowing);
    }
    */
    public User save(User user){
        //profileRepository.save(user.getProfile());
        return userRepository.save(user);
    }

    public boolean exists(User user){
        return userRepository.findUserById(user.getId()).isPresent();
    }

    public boolean exists(String userId){
        return userRepository.findUserById(userId).isPresent();
    }

    /*public boolean profileExists(Profile profile){
        return profileRepository.findById(profile.getId()).isPresent();
    }*/

    public void savePost(User user, Post post) throws DBEntryAlreadyExistsException {
        if (!user.getProfile().addSavedPostId(post.getId())) {
            throw new DBEntryAlreadyExistsException("FAILED adding postId: " + post.getId() + " to Profile.savedPostIds list because it's already saved by this profile: " + user.getProfile());
        }

        save(user);
    }

    public void unsavePost(User user, Post post) throws NoSuchDBEntryException {
        if (!user.getProfile().savedPostIdsContains(post.getId())) {
            throw new NoSuchDBEntryException("FAILED removing postId: " + post.getId() + " from Profile.savedPostIds list because it hasn't already been saved by this profile: " + user.getProfile());
        }

        user.getProfile().removeSavedPostId(post.getId());
        save(user);
    }

    public ArrayList<Message> getDMsBetweenUsers(String loggedInUserId, String otherUserId) {
        ArrayList<Message> allMessages = new ArrayList<>(); // all DMs between the two users

        // Adds all messages sent from loggedInUser to otherUser
        Optional<ArrayList<Message>> messagesFromUser1Optional = dmRepository.getBySenderIdAndRecipientId(loggedInUserId, otherUserId);
        if (messagesFromUser1Optional.isPresent()) // loggedInUserId has sent at least one DM to otherUserId
            allMessages.addAll(messagesFromUser1Optional.get());

        // Adds all messages sent from otherUserId to loggedInUserId
        Optional<ArrayList<Message>> messagesFromUser2Optional = dmRepository.getBySenderIdAndRecipientId(otherUserId, loggedInUserId);
        if (messagesFromUser2Optional.isPresent()) // otherUserId has sent at least one DM to loggedInUserId
            allMessages.addAll(messagesFromUser2Optional.get());

        Collections.sort(allMessages);

        return allMessages;
    }

    public void sendDM(Message message) throws DBEntryAlreadyExistsException, NoSuchDBEntryException {
        if (message.getId() < IdSequencer.START_ID_VALUE)
            message.setId(idSequencer.getNextId(IdSequencer.Collection.MESSAGE));

        if (dmRepository.getById(message.getId()).isPresent()) {
            System.out.println("FAILED insert message to database.  Message already exists.     " + message);
            throw new DBEntryAlreadyExistsException("Message with id: " + message.getId() + " already exists");
        }

        // Ensures that the sender User exists
        if (!exists(message.getSenderId())) {
            System.out.println("FAILED insert Message to database.  Message's sender User does not exist.");
            throw new NoSuchDBEntryException("No User found with id: " + message.getSenderId()); // No such User
        }

        // Ensures that the recipient User exists
        if (!exists(message.getRecipientId())) {
            System.out.println("FAILED insert Message to database.  Message's recipient User does not exist.");
            throw new NoSuchDBEntryException("No User found with id: " + message.getRecipientId()); // No such User
        }

        dmRepository.insert(message);
    }

    public Message getMessageById(Integer id) throws NoSuchDBEntryException {
        Optional<Message> foundMessageOptional = dmRepository.getById(id);

        if (foundMessageOptional.isEmpty())
            throw new NoSuchDBEntryException("No Message found with id: " + id);  // exception if no Message found

        return foundMessageOptional.get();
    }

    public ArrayList<Post> getNonblockedPosts(ArrayList<Post> postList, User userRequestingPosts) {
        ArrayList<Post> resultingList = new ArrayList<>();
        ArrayList<Topic> blockedTopics = new ArrayList<>();
        ArrayList<String> blockedUsers = new ArrayList<>();

        if (userRequestingPosts == null) {
            System.out.println("getNonblockedPosts() returning unedited postList because user is null");
            return resultingList;
        }
        if (userRequestingPosts.getProfile() == null) {
            System.out.println("getNonblockedPosts() returning unedited postList because user's profile is null");
            return resultingList;
        }

        Profile profile = userRequestingPosts.getProfile();
        blockedTopics.addAll(profile.getBlockedTopic());
        blockedUsers.addAll(profile.getUsersThisUserHasBlocked());
        blockedUsers.addAll(profile.getUsersThatBlockedThisUser());

        for (int i = 0; i < postList.size(); i++) {
            Post p = postList.get(i);
            boolean shouldAdd = true;

            // doesn't add posts with block topics
            for (Topic t : blockedTopics) {
                if (p.getTopicName().equals(t.getName())) {
                    shouldAdd = false;
                    break;
                }
            }
            if (!shouldAdd)
                continue;

            // doesn't add posts with blocked creators
            for (String u : blockedUsers) {
                if (p.getCreatorName().equals(u)) {
                    shouldAdd = false;
                    break;
                }
            }
            if (!shouldAdd)
                continue;

            resultingList.add(p);
        }

        return resultingList;
    }

    public void removeDmsWithUser(User user) {
        Optional<ArrayList<Message>> dmsWithUserOptional = dmRepository.getBySenderIdOrRecipientId(user.getId(), user.getId());
        if (dmsWithUserOptional.isEmpty()) { // user has not sent or received any dms
            System.out.println("no dms to or from user: " + user.getUsername() + " exist to be removed");
            return;
        }

        ArrayList<Message> dmsWithUser = dmsWithUserOptional.get();
        for (Message m : dmsWithUser) {
            dmRepository.delete(m);
        }
    }
}