package com.Topic;

import com.Post.*;
import com.User.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;

@Data
@NoArgsConstructor
@Document(collection = "topic")
public class Topic {
    @Id
    private int id; // initialized to highestId+1 when inserted to database by IdSequencer
    private String name;
    private String creatorName;
    private ArrayList<Post> topicPosts;

    public Topic(String name, String creatorName) {
        this.name = name;
        this.creatorName = creatorName;
        this.topicPosts = new ArrayList<>();
    }

    public void addTopicPost(Post p) {
        this.topicPosts.add(p);
    }

    public void removeTopicPost(Post p) {
        for (int i = 0; i < topicPosts.size(); i++) {
            Post curPost = topicPosts.get(i);

            if (curPost.getId() == p.getId()) {
                this.topicPosts.remove(i);
                return;
            }
        }
    }

    public boolean contains(Post p) { return topicPosts.contains(p); }

    public Post getTopicPostWithID(int id){
        for (Post currentPost : topicPosts){
            if (currentPost == null)
                continue;

            if  (currentPost.getId() == id)
                return currentPost;
        }

        return null;
    }

    public int getTopicPostIndexWithID(int id) {
        for (int i = 0; i < topicPosts.size(); i++) {
            Post currentPost = topicPosts.get(i);
            if (currentPost == null)
                continue;

            if  (currentPost.getId() == id)
                return i;
        }

        return -1;
    }

    public Post updateTopicPostWithID(Post newPost){
        for (int i = 0; i < topicPosts.size(); i++) {
            Post currentPost = topicPosts.get(i);
            if (currentPost == null)
                continue;

            if  (currentPost.getId() == newPost.getId())
                return (topicPosts.set(i, newPost));
        }

        return null;
    }

    public ArrayList<Post> getSortedPosts() {
        Collections.sort(getTopicPosts());

        /*for (int i = 0; i < getTopicPosts().size(); i++) {
            Post post = getTopicPosts().get(i);
            System.out.println("{" + i + "} PostTime - " + post.getCreationTime().toString());
        }*/

        return getTopicPosts();
    }
}