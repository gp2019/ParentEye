package com.example.parenteye;

import java.sql.Time;

public class Members {

    private String UserId;
    private String typtId;  //1 if group and 2 if page
    private String communityId; // Id of group or page
    private Time AddedTime;



    public Members(){

    }

    public Members(String typtId, String communityId, String userId, Time addedTime) {
        this.typtId = typtId;
        this.communityId = communityId;
        UserId = userId;
        AddedTime = addedTime;
    }


    public void setTyptId(String typtId) {
        this.typtId = typtId;
    }

    public void setCommunityid(String communityid) {
        this.communityId = communityid;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setAddedTime(Time addedTime) {
        AddedTime = addedTime;
    }


    public String getTyptId() {
        return typtId;
    }

    public String getCommunityid() {
        return communityId;
    }

    public String getUserId() {
        return UserId;
    }

    public Time getAddedTime() {
        return AddedTime;
    }
}
