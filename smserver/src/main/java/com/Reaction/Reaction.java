package com.Reaction;

import com.User.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Reaction {
    private ReactionType type;
    private String creatorName;
    private int postId;

    public Reaction(ReactionType r, String c, int id) {
        this.type = r;
        this.creatorName = c;
        this.postId = id;
    }
}
