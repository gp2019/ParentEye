package com.example.parenteye;

import android.net.Uri;

import java.util.Date;

public class Users {
    private String userId;
    private String Username;
    private String userEmail;
    private String userPassword;
    private Date dateofbirth;
    private String location;
    private boolean gender;
    private String RoleId; //1 is  user and 2  is child
    private String profile_pic_id;
    private String cover_pic_id;
    private boolean CloseAccount;
    private String TimeCloseAccount;
    private String State;

    public void setState(String state) {
        State = state;
    }

    public String getState() {
        return State;
    }

    public Users(String userId, String username, String userEmail, String userPassword, Date dateofbirth, String location, boolean gender, String roleId, String profile_pic_id, String cover_pic_id, boolean closeAccount, String timeCloseAccount) {
        this.userId = userId;
        Username = username;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.dateofbirth = dateofbirth;
        this.location = location;
        this.gender = gender;
        RoleId = roleId;
        this.profile_pic_id = profile_pic_id;
        this.cover_pic_id = cover_pic_id;
        CloseAccount = closeAccount;
        TimeCloseAccount = timeCloseAccount;
    }

    public Users(String userId, String username, String userEmail, String userPassword, Date dateofbirth, String location, boolean gender, String roleId, String profile_pic_id, String cover_pic_id) {
        this.userId = userId;
        Username = username;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.dateofbirth = dateofbirth;
        this.location = location;
        this.gender = gender;
        RoleId = roleId;
        this.profile_pic_id = profile_pic_id;
        this.cover_pic_id = cover_pic_id;
    }

    public Users(){

  }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setCover_pic_id(String cover_pic_id) {
        this.cover_pic_id = cover_pic_id;
    }

    public String getCover_pic_id() {
        return cover_pic_id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /*public void setUsername(String username) {
        Username = username;
    }*/
    public void setUsername(String username){
        Username=username;
    }

  public void setDateofbirth(Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    /*public String getUsername() {
        return Username;
    }*/

    public  String getUsername(){
        return  Username;
    }


    public Date getDateofbirth() {
        return dateofbirth;
    }


    public String getLocation() {
        return location;
    }

    public boolean isGender() {
        return gender;
    }

    public String getRoleId() {
        return RoleId;
    }

    public String getProfile_pic_id() {
        return profile_pic_id;
    }

    public void setProfile_pic_id(String profile_pic_id) {
        this.profile_pic_id = profile_pic_id;
    }

    public boolean isCloseAccount() {
        return CloseAccount;
    }

    public void setCloseAccount(boolean closeAccount) {
        CloseAccount = closeAccount;
    }

    public String getTimeCloseAccount() {
        return TimeCloseAccount;
    }

    public void setTimeCloseAccount(String timeCloseAccount) {
        TimeCloseAccount = timeCloseAccount;
    }
}
