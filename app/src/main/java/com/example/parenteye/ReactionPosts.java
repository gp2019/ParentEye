package com.example.parenteye;

public class ReactionPosts {

    private String reactionId;
    private String PostORCommentId;
    private String userid;
    private String postorCommentId_userId;


    public ReactionPosts(String reactionId, String postORCommentId, String userid, String postorCommentId_userId) {
        this.reactionId = reactionId;
        PostORCommentId = postORCommentId;


// private String Reactionid;


    }


    public ReactionPosts() {

    }

    public void setReactionId(String reactionId) {
        this.reactionId = reactionId;
    }

    public void setPostORCommentId(String postORCommentId) {
        PostORCommentId = postORCommentId;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPostorCommentId_userId(String postorCommentId_userId) {
        this.postorCommentId_userId = postorCommentId_userId;
    }

    public String getReactionId() {
        return reactionId;
    }

    public String getPostORCommentId() {
        return PostORCommentId;
    }

    public String getUserid() {
        return userid;
    }

    public String getPostorCommentId_userId() {
        return postorCommentId_userId;
    }
}