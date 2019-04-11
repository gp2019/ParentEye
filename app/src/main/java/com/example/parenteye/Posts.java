package com.example.parenteye;

public class Posts {
    private String postId;
    private String userId;
    private String postdate;
    private String placeTypeId;     //1=his timeline ,2=page,3=group
    private String placeId;          //Id of page or group
    private String postcontent;    //it will be null if post has image only
    private boolean hasimage;       //1 if post contain image and 0 if post not contain image
    private String imageId;        //image id if post has image and it will be null if post do not have image
    private int countComment;
    private int countLike;

    public Posts(String postId ,String userId, String postdate, String placeTypeId, String placeId, String postcontent, boolean hasimage, String imageId,int countComment,int countLike) {
        this.postId = postId;
        this.userId = userId;
        this.postdate = postdate;
        this.placeTypeId = placeTypeId;
        this.placeId = placeId;
        this.postcontent = postcontent;
        this.hasimage = hasimage;
        this.imageId = imageId;
        this.countComment=countComment;
        this.countLike=countLike;
    }

    public Posts(){

    }


    public void setPostId(String postId) { this.postId = postId; }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public void setPlaceTypeId(String placeTypeId) {
        this.placeTypeId = placeTypeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setPostcontent(String postcontent) {
        this.postcontent = postcontent;
    }

    public void setHasimage(boolean hasimage) {
        this.hasimage = hasimage;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public int getCountComment() {
        return countComment;
    }

    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostdate() {
        return postdate;
    }

    public String getPlaceTypeId() {
        return placeTypeId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getPostcontent() {
        return postcontent;
    }

    public boolean isHasimage() {
        return hasimage;
    }

    public String getImageId() {
        return imageId;
    }
    public void setCountComment(int countComment) {
        this.countComment = countComment;
    }
}
