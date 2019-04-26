package com.example.parenteye;

public class ActivityLog {
    private String ActivitylogiId;
    private String ActivityContent;
    private String postId;

    public ActivityLog(String activitylogiId, String activityContent, String postId) {
        ActivitylogiId = activitylogiId;
        ActivityContent = activityContent;
        this.postId = postId;
    }
    public ActivityLog(){

    }

    public void setActivitylogiId(String activitylogiId) {
        ActivitylogiId = activitylogiId;
    }

    public void setActivityContent(String activityContent) {
        ActivityContent = activityContent;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getActivitylogiId() {
        return ActivitylogiId;
    }

    public String getActivityContent() {
        return ActivityContent;
    }

    public String getPostId() {
        return postId;
    }
}
