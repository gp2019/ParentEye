package com.example.parenteye;

public class ReactionPosts {

    private String reactionId; //PostORCommentId+userid
    private String PostORCommentId;
    private String userId;

    public ReactionPosts() {
    }

    public ReactionPosts(String reactionId, String postORCommentId, String userId) {
        this.reactionId = reactionId;
        PostORCommentId = postORCommentId;
        this.userId = userId;
    }

    public String getReactionId() {
        return reactionId;
    }

    public void setReactionId(String reactionId) {
        this.reactionId = reactionId;
    }

    public String getPostORCommentId() {
        return PostORCommentId;
    }

    public void setPostORCommentId(String postORCommentId) {
        PostORCommentId = postORCommentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}