package com.example.parenteye;

import android.provider.ContactsContract;

import java.util.Date;

public class Notifications {
    private String id;
    private String userId;


    private String publisherId;
    private String NotificationMessage;
    private Date date;
    private boolean seen;




    ////////
    private String postId;



    private boolean isPost;

    public Notifications(){

    }

    public Notifications(String id, String userId, String notificationMessage, Date date, boolean seen ,boolean isPost) {
        this.id = id;
        this.userId = userId;
        NotificationMessage = notificationMessage;
        this.date = date;
        this.seen = seen;
        this.isPost=isPost;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNotificationMessage(String notificationMessage) {
        NotificationMessage = notificationMessage;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getNotificationMessage() {
        return NotificationMessage;
    }

    public Date getDate() {
        return date;
    }

    public boolean isSeen() {
        return seen;
    }

    ////////


    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPostId() {
        return postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }



    public boolean getIsPost() {
        return isPost;
    }






}
