package com.Post;

import com.User.*;
import com.Comment.*;
import com.Reaction.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.time.*;

@Document(collection = "post")
public class Post implements Comparable<Post> {
    @Id
    private int id; // initialized to highestId+1 when inserted to database by IdSequencer
    private String creatorName;
    private String topicName;
    private String image;
    private String title;
    private String caption;
    private ArrayList<Comment> comments;
    private ArrayList<Reaction> interactions;
    private LocalDateTime creationTime;
    private boolean hasImage;
    private boolean hasCaption;
    private boolean isAnonymous;

    public Post() {
        this.creationTime = LocalDateTime.now();
    }

    public Post(String creatorName, String topicName, String postTitle, boolean hasImage, boolean hasCaption, boolean isAnonymous) {
        this.creatorName = creatorName;
        this.topicName = topicName;
        this.title = postTitle;
        this.creationTime = LocalDateTime.now();
        this.hasImage = hasImage;
        this.hasCaption = hasCaption;
        this.isAnonymous = isAnonymous;
        this.comments = new ArrayList<>();
        this.interactions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopic(String topicName) {
        this.topicName = topicName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comment) {
        this.comments = comment;
    }

    public void addComment(Comment c) {
        this.comments.add(c);
    }

    public void removeComment(int commentID) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getId() == commentID) {
                comments.remove(i);
                return;
            }
        }
    }

    public ArrayList<Reaction> getInteractions() {
        return interactions;
    }

    public void setInteractions(ArrayList<Reaction> interactions) {
        this.interactions = interactions;
    }

    public void addInteraction(Reaction i) {
        this.interactions.add(i);
    }

    public void removeInteraction(Reaction i) {
        this.interactions.remove(i);
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public boolean isHasCaption() {
        return hasCaption;
    }

    public void setHasCaption(boolean hasCaption) {
        this.hasCaption = hasCaption;
    }

    public boolean getIsAnonymous(){
        return isAnonymous;
    }

    public void setIsAnonymous(boolean isAnonymous){
        this.isAnonymous = isAnonymous;
    }

    public boolean contains(Comment c) { return comments.contains(c); }

    public Reaction getReactionByCreator(String creatorName) {
        if (creatorName == null)
            return null;

        for (Reaction currentReaction : interactions) {
            if (creatorName.equals(currentReaction.getCreatorName()))
                return currentReaction;
        }

        return null;
    }

    public void changeReactionInList(Reaction newReaction) {
        for (Reaction r : interactions) {
            if (r.getCreatorName().equals(newReaction.getCreatorName())) {
                r.setType(newReaction.getType());
                return;
            }
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "creatorName='" + creatorName + '\'' +
                ", topicName='" + topicName + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", caption='" + caption + '\'' +
                ", comments=" + comments +
                ", interactions=" + interactions +
                ", creationTime=" + creationTime +
                ", hasImage=" + hasImage +
                ", hasCaption=" + hasCaption +
                ", isAnonymous=" + isAnonymous +
                '}';
    }

    @Override
    public int compareTo(Post comparePost) {
        return comparePost.getCreationTime().compareTo(getCreationTime());
    }
}
