package com.example.parenteye;

public class ReactionPosts {

    private String PostORCommentId;
    private String userid;


// private String Reactionid;


    public ReactionPosts(){

    }

    public ReactionPosts( String PostORCommentId, String userid) {

        this.PostORCommentId = PostORCommentId;
        this.userid = userid;

       // Reactionid = reactionid;

    }




    public void setpostId(String PostORCommentId) {
        this.PostORCommentId = PostORCommentId;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    /*public void setReactionid(String reactionid) {
        Reactionid = reactionid;
    }
    */

    public String getpostId() {
        return PostORCommentId;
    }

    public String getUserid() {
        return userid;
    }


/*
    public String getReactionid() {
        return Reactionid;
    }
*/

}

