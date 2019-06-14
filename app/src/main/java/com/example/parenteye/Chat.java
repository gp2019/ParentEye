package com.example.parenteye;

import java.sql.Time;
import java.util.Date;

public class Chat {

    private String messageText;
    private String messageUser;
   // private long messageTime;
    private String time;
    private String messageType;


    public Chat(String messageText, String messageUser,String time,String messageType) {
        this.messageText = messageText;
        this.messageUser = messageUser;
       // messageTime= new Date().getTime();
        this.time=time;
        this.messageType=messageType;
    }

    public  Chat()
    {

    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    /*public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
    */
}
