package com.example.parenteye;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URI;
import java.util.Date;

public class custom_posts_returned {
    private String post_owner_name;
    private String post_text;
    private String post_owner_ID;
    private String post_image;
    private Date post_date;

    // private  static  final String noimage="-1";



    public custom_posts_returned(){

    }



    public custom_posts_returned(String post_text, String post_owner_ID, String post_image, Date post_date, String post_owner_name) {
        this.post_text = post_text;
        this.post_owner_ID = post_owner_ID;
        this.post_image = post_image;
        this.post_date = post_date;
        this.post_owner_name=post_owner_name;
    }

    public void setPost_owner_name(String post_owner_name) {
        this.post_owner_name = post_owner_name;
    }

    public String getPost_owner_name() {
        return post_owner_name;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public void setpost_owner_ID(String profile_image) {
        this.post_owner_ID = profile_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }

    public String getPost_text() {
        return post_text;
    }

    public String getpost_owner_ID() {
        return post_owner_ID;
    }

    public String getPost_image() {
        return post_image;
    }

    public Date getPost_date() {
        return post_date;
    }
    public boolean haspost_owner_ID(){
        if(post_owner_ID!=null){
            return true;
        }
        else{
            return false;
        }

    }
    public boolean haspostimage(){
        if(post_image!=null){
            return true;
        }
        else{
            return false;
        }
    }
}
