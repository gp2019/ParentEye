package com.example.parenteye;

public class ReactionPosts {

    private String reactionId;
    private String PostORCommentId;
    private String userid;
    private String postorCommentId_userId;

<<<<<<< 83e35f28209d06b1ab914d2d9ebccc3737456930
    public ReactionPosts(String reactionId, String postORCommentId, String userid,String postorCommentId_userId) {
        this.reactionId = reactionId;
        PostORCommentId = postORCommentId;
=======


// private String Reactionid;


    public ReactionPosts(){

    }

    public void setPostORCommentId(String postORCommentId) {
        PostORCommentId = postORCommentId;
    }



    public String getPostORCommentId() {
        return PostORCommentId;
    }



    public ReactionPosts(String PostORCommentId, String userid) {

        this.PostORCommentId = PostORCommentId;
>>>>>>> new commit
        this.userid = userid;
        this.postorCommentId_userId=postorCommentId_userId;
    }

    public ReactionPosts() {

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPostorCommentId_userId() {
        return postorCommentId_userId;
    }

    public void setPostorCommentId_userId(String postorCommentId_userId) {
        this.postorCommentId_userId = postorCommentId_userId;
    }
}