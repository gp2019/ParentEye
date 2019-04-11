package com.example.parenteye;

public class PostComments {
    private String commentID;
    private String commentcontent;
    private String userId;
    private String PostId;
    private boolean hasreply; // 1 if this comment has reply
    private boolean hasimage; // 1 if comment has image and 0 if comment do nit have image
    private String imageId;
    private String commentDate;
    private int CountOfLike;


    public PostComments(String commentID, String commentcontent, String userId, String postId, boolean hasreply, boolean hasimage, String imageId, String commentDate, int countOfLike) {
        this.commentID = commentID;
        this.commentcontent = commentcontent;
        this.userId = userId;
        this.PostId = postId;
        this.hasreply = hasreply;
        this.hasimage = hasimage;
        this.imageId = imageId;
        this.commentDate = commentDate;
        this.CountOfLike = countOfLike;
    }

    public PostComments() {

    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getCommentcontent() {
        return commentcontent;
    }

    public void setCommentcontent(String commentcontent) {
        this.commentcontent = commentcontent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public boolean isHasreply() {
        return hasreply;
    }

    public void setHasreply(boolean hasreply) {
        this.hasreply = hasreply;
    }

    public boolean isHasimage() {
        return hasimage;
    }

    public void setHasimage(boolean hasimage) {
        this.hasimage = hasimage;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public int getCountOfLike() {
        return CountOfLike;
    }

    public void setCountOfLike(int countOfLike) {
        CountOfLike = countOfLike;
    }


}
