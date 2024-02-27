package com.Profile;

import java.util.ArrayList;
import java.util.Objects;

import com.User.*;
import com.ProfileContent.*;
import com.Topic.*;
import com.DirectMessage.*;
import com.Post.*;
import com.Reaction.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Profile {
    //@Id
    private int id; //unused now     // initialized to highestId+1 when inserted to database by IdSequencer*/
    private String name;
    private ProfileContent<Sex> sex;
    private ProfileContent<Integer> age;
    private ProfileContent<String> bio;
    private boolean dmStatus;
    private ArrayList<String> userFollowers;
    private ArrayList<String> userFollowing;
    private ArrayList<String> usersThisUserHasBlocked;
    private ArrayList<String> usersThatBlockedThisUser;
    private ArrayList<Topic> followingTopic;
    private ArrayList<Topic> blockedTopic;
    private ArrayList<Integer> directMessageIds;
    private ArrayList<Post> posts;
    private ArrayList<Reaction> interactions;
    private ArrayList<ProfileContent<Object>> publicInfo;
    private ArrayList<Integer> savedPostIds;
    private int numPosts;
    private ArrayList<Integer> commentIds;

    public Profile() {
        this.name = "";
        this.sex = new ProfileContent(Sex.MALE, true);
        this.age = new ProfileContent(0, true);
        this.bio = new ProfileContent("", true);
        this.userFollowers = new ArrayList<>();
        this.userFollowing = new ArrayList<>();
        this.dmStatus = false;
        this.usersThisUserHasBlocked = new ArrayList<>();
        this.usersThatBlockedThisUser = new ArrayList<>();
        this.followingTopic = new ArrayList<>();
        this.blockedTopic = new ArrayList<>();
        this.directMessageIds = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.interactions = new ArrayList<>();
        this.publicInfo = new ArrayList<>();
        this.savedPostIds = new ArrayList<>();
        this.commentIds = new ArrayList<>();
    }

    public Profile(String name, Sex sex, int age, String bio){
        this(name,
                new ProfileContent<>(sex, false),
                new ProfileContent<>(age, false),
                new ProfileContent<>(bio, false)
        );
    }

    public Profile(String name, ProfileContent<Sex> sex, ProfileContent<Integer> age, ProfileContent<String> bio) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.bio = bio;
        this.userFollowers = new ArrayList<>();
        this.userFollowing = new ArrayList<>();
        this.usersThisUserHasBlocked = new ArrayList<>();
        this.usersThatBlockedThisUser = new ArrayList<>();
        this.followingTopic = new ArrayList<>();
        this.blockedTopic = new ArrayList<>();
        this.directMessageIds = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.interactions = new ArrayList<>();
        this.publicInfo = new ArrayList<>();
        this.numPosts = 0;
        this.savedPostIds = new ArrayList<>();
        this.commentIds = new ArrayList<>();
    }

    public int getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(int numPosts) {
        this.numPosts = numPosts;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfileContent<Sex> getSex() {
        return sex;
    }

    public String getSexString() { return sex.toString(); }

    public boolean sexIsPublic() { return this.sex.isPublic(); }

    public ProfileContent<Integer> getAge() {
        return age;
    }

    public int getAgeInt() { return this.age.toInt(); }

    public boolean ageIsPublic() { return this.age.isPublic(); }

    public ProfileContent<String> getBio() {
        return bio;
    }

    public String getBioString() { return this.bio.toString(); }

    public boolean bioIsPublic() { return this.bio.isPublic(); }

    public ArrayList<String> getUserFollowers() {
        return userFollowers;
    }

    public void setUserFollowers(ArrayList<String> userFollowers) {
        this.userFollowers = userFollowers;
    }

    public void addUserFollowers(String u) {
        this.userFollowers.add(u);
    }

    public ArrayList<String> getUserFollowing() {
        return userFollowing;
    }

    public void setUserFollowing(ArrayList<String> userFollowing) {
        this.userFollowing = userFollowing;
    }

    public void addUserFollowing(String u) {
        this.userFollowing.add(u);
    }

    public void removeFollowing(String followPerson) {
        this.userFollowing.remove(followPerson);
    }

    public void removeFollower(String followPerson) {
        this.userFollowers.remove(followPerson);
    }

    public void removeFollowingTopic(Topic topic) {
        int index = 0;
        for (int i = 0; i < this.followingTopic.size(); i++) {
            if (Objects.equals(this.followingTopic.get(i).getName(), topic.getName())) {
                index = i;
                break;
            }
        }
        this.followingTopic.remove(index); }

    public ArrayList<String> getUsersThisUserHasBlocked() {
        return usersThisUserHasBlocked;
    }

    public void setUsersThisUserHasBlocked(ArrayList<String> usersThisUserHasBlocked) {
        this.usersThisUserHasBlocked = usersThisUserHasBlocked;
    }

    public void addUsersThisUserHasBlocked(String u) {
        usersThisUserHasBlocked.add(u);
    }

    public void removeUsersThisUserHasBlocked(String u) { usersThisUserHasBlocked.remove(u); }

    public ArrayList<String> getUsersThatBlockedThisUser() {
        return usersThatBlockedThisUser;
    }

    public void setUsersThatBlockedThisUser(ArrayList<String> usersThatBlockedThisUser) {
        this.usersThatBlockedThisUser = usersThatBlockedThisUser;
    }

    public void addUsersThatBlockedThisUser(String u) {
        usersThatBlockedThisUser.add(u);
    }

    public void removeUsersThatBlockedThisUser(String u) { usersThatBlockedThisUser.remove(u); }

    public ArrayList<Topic> getFollowingTopicsList() {
        return followingTopic;
    }

    public void setFollowingTopic(ArrayList<Topic> followingTopic) {
        this.followingTopic = followingTopic;
    }

    public void addFollowingTopic(Topic t) {
        if (isFollowingTopic(t)) {
            System.out.println("FAILED following topic: " + t + " because it is already being follow by user: " + name);
            return;
        }

        this.followingTopic.add(t);
    }
    public boolean getDMStatus() {
        return dmStatus;
    }
    public void setDMStatus(boolean DMStatus) {
        this.dmStatus = DMStatus;
    }
    public boolean isFollowingTopic(Topic topic) {
        int id = topic.getId();

        for (Topic t : followingTopic) {
            if (t.getId() == id)
                return true;
        }

        return false;
    }
    public boolean isFollowingTopic(String topicName) {
        for (Topic t : followingTopic) {
            if (t.getName().equals(topicName))
                return true;
        }

        return false;
    }

    public Topic getFollowingTopic(Topic topic) {
        int id = topic.getId();

        for (Topic t : followingTopic) {
            if (t.getId() == id)
                return t;
        }

        return null;
    }

    public void updateFollowingTopic(Topic topic) {
        int id = topic.getId();

        for (int i = 0; i < followingTopic.size(); i++) {
            Topic t = followingTopic.get(i);
            if (t.getId() == id) {
                followingTopic.set(i, topic);
                return;
            }
        }
    }

    public ArrayList<Topic> getBlockedTopic() {
        return blockedTopic;
    }

    public void setBlockedTopic(ArrayList<Topic> blockedTopic) {
        this.blockedTopic = blockedTopic;
    }

    public void addBlockedTopic(Topic t) {
        this.blockedTopic.add(t);
    }

    public ArrayList<Integer> getDirectMessages() {
        return directMessageIds;
    }

    /*public void addDirectMessageId(DirectMessage dm) {
        this.directMessageIds.add(dm.getId());
    }*/

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
        numPosts = posts.size();
    }

    public void addPost(Post p) {
        this.posts.add(p);
        numPosts = posts.size();
    }

    public boolean removePost(Post p) {
        for (int i = 0; i < posts.size(); i++) {
            Post curPost = posts.get(i);
            if (curPost.getId() == p.getId()) {
                posts.remove(i);
                numPosts = posts.size();
                return true;
            }
        }

        return false;
    }

    public ArrayList<Reaction> getInteractions() {
        return interactions;
    }

    public void setInteractions(ArrayList<Reaction> interactions) {
        this.interactions = interactions;
    }

    // returns true if the add succeeded
    public boolean addInteraction(Reaction reaction) {
        if (interactionsContains(reaction)) {
            return false;
        }

        interactions.add(reaction);
        return true;
    }

    // returns true if the add succeeded
    public void changeInteraction(Reaction newReaction) {
        for (Reaction curReaction : interactions) {
            if (curReaction.getCreatorName().equals(newReaction.getCreatorName()) && curReaction.getPostId() == newReaction.getPostId())
                curReaction.setType(newReaction.getType());
        }
    }

    public boolean interactionsContains(Reaction r) {
        for (Reaction curReaction : interactions) {
            if (curReaction.getCreatorName().equals(r.getCreatorName()) && curReaction.getPostId() == r.getPostId())
                return true;
        }

        return false;
    }

    // returns true if the removal succeeded
    public boolean removeInteraction(Reaction r){
        for (Reaction curReaction : interactions) {
            if (curReaction.getCreatorName().equals(r.getCreatorName()) && curReaction.getPostId() == r.getPostId()) {
                interactions.remove(curReaction);
                return true;
            }
        }

        return false;
    }

    public ArrayList<ProfileContent<Object>> getPublicInfo() {
        return publicInfo;
    }

    public void setPublicInfo(ArrayList<ProfileContent<Object>> publicInfo) {
        this.publicInfo = publicInfo;
    }

    public void addPublicInfo(ProfileContent<Object> pc) {
        this.publicInfo.add(pc);
    }

    public ArrayList<Integer> getSavedPostIds() {
        return savedPostIds;
    }

    // returns true if the add succeeded
    public boolean addSavedPostId(int postId) {
        if (savedPostIdsContains(postId)) {
            // moved to UserDataHandler.save()
            //System.out.println("FAILED adding postId: " + postId + " to Profile.savedPostIds list because it's already saved by this profile: " + this);
            return false;
        }

        savedPostIds.add(postId);
        return true;
    }

    // returns true if the removal succeeded
    public boolean removeSavedPostId(int postId){
        for (int i = 0; i < savedPostIds.size(); i++) {
            int curId = savedPostIds.get(i);
            if (curId == postId) {
                getSavedPostIds().remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean savedPostIdsContains(int postId) {
        for (int curId : savedPostIds) {
            if (curId == postId)
                return true;
        }

        return false;
    }

    public void setSavedPostIds(ArrayList<Integer> savedPostIds) {
        this.savedPostIds = savedPostIds;
    }


    public ArrayList<Integer> getCommentIds() {
        return commentIds;
    }

    // returns true if the add succeeded
    public boolean addCommentId(int commentId) {
        if (commentIdsContains(commentId)) {
            return false;
        }

        commentIds.add(commentId);
        return true;
    }

    // returns true if the removal succeeded
    public boolean removeCommentId(int commentId){
        for (int i = 0; i < commentIds.size(); i++) {
            int curId = commentIds.get(i);
            if (curId == commentId) {
                commentIds.remove(i);
                System.out.println("removing commentId: " + commentId);
                return true;
            }
        }

        return false;
    }

    public boolean commentIdsContains(int commentId) {
        for (int curId : commentIds) {
            if (curId == commentId)
                return true;
        }

        return false;
    }

    public void setCommentIds(ArrayList<Integer> commentIds) {
        this.commentIds = commentIds;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", bio=" + bio +
                ", userFollowers=" + userFollowers +
                ", userFollowing=" + userFollowing +
                ", usersThisUserHasBlocked=" + usersThisUserHasBlocked +
                ", usersThatBlockedThisUser=" + usersThatBlockedThisUser +
                ", followingTopic=" + followingTopic +
                ", blockedTopic=" + blockedTopic +
                ", directMessageIds=" + directMessageIds +
                ", posts=" + posts +
                ", interactions=" + interactions +
                ", publicInfo=" + publicInfo +
                ", savedPostIds=" + savedPostIds +
                ", numPosts=" + numPosts +
                ", commentIds=" + commentIds +
                '}';
    }
}
