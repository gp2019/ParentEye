package com.example.parenteye;


public class Friends {
    private String userId;
    private String userFriends;
    private String blockFriends;

    public String getBlockFriends() {
        return blockFriends;
    }

    public void setBlockFriends(String blockFriends) {
        this.blockFriends = blockFriends;
    }


    public Friends(){

    }

    public Friends(String userId, String userFriends) {
        this.userId = userId;
        this.userFriends = userFriends;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserFriends(String userFriends) {
        this.userFriends = userFriends;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserFriends() {
        return userFriends;
    }
}
