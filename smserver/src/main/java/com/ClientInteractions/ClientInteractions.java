package com.ClientInteractions;

import com.Post.Post;

public class ClientInteractions {
    private Post post;
    private String type;

    public ClientInteractions(Post p, String t) {
        this.post = p;
        this.type = t;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post p) {
        this.post = p;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String s) {
        this.type = type;
    }
}
