package com.Comment;

import com.User.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document
public class Comment {
    @Id
    private int id;
    private String text;
    private String creatorName;
    private int postId; // the id of the post object that this comment exists under
    private LocalDateTime creationTime;

    public Comment(String text, String creatorName, int postId) {
        this.text = text;
        this.creatorName = creatorName;
        this.postId = postId;
        creationTime = LocalDateTime.now();
    }
    public Comment(int id, int postId) {
        this.id = id;
        this.postId = postId;
        creationTime = LocalDateTime.now();
    }
}
