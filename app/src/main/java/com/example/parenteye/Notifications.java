package com.example.parenteye;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class Notifications {
    private String id;
    private String userId;


    private String publisherId;

    private String NotificationMessage;
    private Date date;
    private boolean seen;

//*********
    private boolean isFriendRequest;





    ////////
    private String postId;



    private boolean isPost;
    String record;



    ////////////////////////////
    private FirebaseAuth mAuth =FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseUser currentUser = mAuth.getCurrentUser();



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

    public boolean isFriendRequest() {
        return isFriendRequest;
    }

    public void setFriendRequest(boolean friendRequest) {
        isFriendRequest = friendRequest;
    }





    //****************************** add notification on like********************************
    public void addNotificationsOfLikes(String postid, String post_publisher_Id) {

        DatabaseReference notification_reference = database.getReference("notifications").child(post_publisher_Id);


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
        hashMap.put("NotificationMessage", "liked your post");
        hashMap.put("postId", postid);
        hashMap.put("isPost", true);
        hashMap.put("isFriendRequest",false);


        notification_reference.push().setValue(hashMap);

        //to call ----->  addNotificationsOfLikes(String postid, String post_publisher_Id)

    }

    //************************  add notification func on comment***************

    public void addNotificationsOfComments(String postid, String post_publisher_Id) {

        DatabaseReference notification_reference = database.getReference("notifications").child(post_publisher_Id);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
        hashMap.put("NotificationMessage", "commented: welcome clever");
        hashMap.put("postId", postid);
        hashMap.put("isPost", true);

        notification_reference.push().setValue(hashMap);

        //+addcomment.getText().toString()
        //to call ----->  addNotificationsOfFollowers(post.getpublisher() //getUserId\\ )
    }


    //************************  add notification func on Friend request ***************

    public void addNotificationsOfFriendRequest( String FriendWantToRequest_Id) {
        DatabaseReference notification_reference = database.getReference("notifications").child(FriendWantToRequest_Id);


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
        hashMap.put("NotificationMessage", "send to you a Friend Request ");
        hashMap.put("postId", " ");
        hashMap.put("isPost", false);
        hashMap.put("isFriendRequest",true);
        notification_reference.push().setValue(hashMap);

        //to call ----->  addNotificationsOfFriendRequest(FriendRequesterId)

    }



    public void DeleteNotificationOfFriendRequest(final String FriendWantToRequest_Id)
    {
        final DatabaseReference notification_reference = database.getReference("notifications").child(currentUser.getUid());






        notification_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Notifications notification = snapshot.getValue(Notifications.class);

                        if(notification.getUserId()==FriendWantToRequest_Id) {

                            //notification_reference.child("-LbdvUQb4wAHEtWjsqoN").removeValue();
                            //notification_reference.child("-LbdvUQb4wAHEtWjsqoN").removeValue();
                            snapshot.getRef().removeValue();
                        }

                        if(notification.getUserId() ==FriendWantToRequest_Id)
                        {
                             //record = snapshot.getKey();
                             //snapshot.getRef().removeValue();




                            //notification_reference.orderByChild("userId").equalTo(FriendWantToRequest_Id).removeEventListener();

                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//            notification_reference.child(record).removeValue();



    }






}
