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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.parenteye.NotificationFragment.notificationAdapter;

/*
notification class
clever

 */
public class Notifications {
    private String id;
    private String userId;


    private String publisherId;

    private String NotificationMessage;
    private Date date;
    private boolean seen;

//*******

    private String postId;
// flags
    private boolean isPost;
    private boolean isComment;
    private boolean isLike;
    private boolean isFriendRequest;

    public List<Notifications> notificationsList  = new ArrayList<>();

    //  String record;
    //int index;








    /******************* firebase connection  *********************/
    private FirebaseAuth mAuth =FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseUser currentUser = mAuth.getCurrentUser();


    public Notifications(){

    }

    public Notifications(String id, String userId, String notificationMessage, Date date, boolean seen ,boolean isPost,boolean isLike ,boolean isComment,boolean isFriendRequest) {
        this.id = id;
        this.userId = userId;
        NotificationMessage = notificationMessage;
        this.date = date;
        this.seen = seen;
        this.isLike=isLike;
        this.isComment=isComment;
        this.isPost=isPost;
        this.isFriendRequest=isFriendRequest;
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

    public boolean getisFriendRequest() {
        return isFriendRequest;
    }

    public void setFriendRequest(boolean friendRequest) {
        isFriendRequest = friendRequest;
    }

    public boolean getIsComment() {
        return isComment;
    }
    public boolean getIsLike() {
        return isLike;
    }





    //****************************** add notification on like********************************
    public void addNotificationsOfLikes(String postid, String post_publisher_Id) {

        DatabaseReference notification_reference = database.getReference("notifications").child(post_publisher_Id);


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
        hashMap.put("NotificationMessage", "liked your post");
        hashMap.put("postId", postid);
        hashMap.put("isPost",true);
        hashMap.put("isLike", true);
        hashMap.put("isComment", false);
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
        hashMap.put("isLike", false);
        hashMap.put("isComment", true);

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
    //************************************* Delete notification func of Like ***************************

    public void DeleteNotificationOfLike(final String postid, final String post_publisher_Id)
    {
        final DatabaseReference notification_reference = database.getReference("notifications").child(post_publisher_Id);

        notification_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Notifications notification = snapshot.getValue(Notifications.class);

                        if (notification.getIsLike() == true && notification.getUserId().equalsIgnoreCase(currentUser.getUid()) && notification.getPostId().equalsIgnoreCase(postid) ) {

                            notification_reference.child(snapshot.getKey()).removeValue();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //************************************* Delete notification func of Comment ***************************


    public void DeleteNotificationOfComment(final String postid, final String post_publisher_Id)
    {
        final DatabaseReference notification_reference = database.getReference("notifications").child(post_publisher_Id);

        notification_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Notifications notification = snapshot.getValue(Notifications.class);
                        if (notification.getIsComment() && notification.getUserId().equalsIgnoreCase(currentUser.getUid()) && notification.getPostId().equalsIgnoreCase(postid) ) {

                            notification_reference.child(snapshot.getKey()).removeValue();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //***************************************  Delete notification  cancel of Friend Request ******************************

    public void DeleteNotificationOfCancelFriendRequest(final String FriendWantToRequest_Id)
    {
        final DatabaseReference notification_reference = database.getReference("notifications").child(FriendWantToRequest_Id);
         //final List<Notifications> notificationsList = new ArrayList<>();
         //String name="ahmed";
        notification_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Notifications notification = snapshot.getValue(Notifications.class);
                        //notificationsList.add(notification);
                        if ( notification.getisFriendRequest() && notification.getUserId().equalsIgnoreCase(currentUser.getUid())  ) {

                            notification_reference.child(snapshot.getKey()).removeValue();
                        }

                       // notification_reference.child(notificationsList.get(0).).removeValue();
                        /*if(notification.getUserId() ==FriendWantToRequest_Id)
                        {
                             //record = snapshot.getKey();
                             //snapshot.getRef().removeValue();
                            //notification_reference.orderByChild("userId").equalTo(FriendWantToRequest_Id).removeEventListener();

                        }*/
                    }
                }
                /*Notifications notifi = notificationsList.get(0);
                if(notificationsList.get(0).isFriendRequest()) {
                    notification_reference.child(notificationsList.get(0).id).removeValue();
                    //notification_reference.child("-LbdvUQb4wAHEtWjsqoN").removeValue();
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//            notification_reference.child(record).removeValue();

    }


    //***************************************  Delete notification  cancel of Friend Request ******************************

    public void DeleteNotificationOfRejectFriendRequest(final String FriendWantToRejectRequest_Id)
    {
        final DatabaseReference notification_reference = database.getReference("notifications").child(currentUser.getUid());
        //final List<Notifications> notificationsList = new ArrayList<>();
        //String name="ahmed";
        notification_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Notifications notification = snapshot.getValue(Notifications.class);
                        //notificationsList.add(notification);
                        if ( notification.getisFriendRequest() && notification.getUserId().equalsIgnoreCase(FriendWantToRejectRequest_Id)  ) {

                            notification_reference.child(snapshot.getKey()).removeValue();
                        }

                        // notification_reference.child(notificationsList.get(0).).removeValue();
                        /*if(notification.getUserId() ==FriendWantToRequest_Id)
                        {
                             //record = snapshot.getKey();
                             //snapshot.getRef().removeValue();
                            //notification_reference.orderByChild("userId").equalTo(FriendWantToRequest_Id).removeEventListener();

                        }*/
                    }
                }
                /*Notifications notifi = notificationsList.get(0);
                if(notificationsList.get(0).isFriendRequest()) {
                    notification_reference.child(notificationsList.get(0).id).removeValue();
                    //notification_reference.child("-LbdvUQb4wAHEtWjsqoN").removeValue();
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//            notification_reference.child(record).removeValue();

    }

    //********************* read notifications current user from fire base and but in notification list
    //********************* put it in adapter

    public List<Notifications> readNotifications() {
        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //FirebaseHelper firebaseHelper = new FirebaseHelper();


        //String userId = "EzH9MI0WJ7N0AwZNUidC45e2wiP2";


        DatabaseReference notification_reference = database.getReference("notifications").child(user.getUid());


        notification_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    notificationsList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Notifications notifications = snapshot.getValue(Notifications.class);
                        notificationsList.add(notifications);
                    }
                    Collections.reverse(notificationsList);
                   notificationAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









        /*
           String userId= firebaseHelper.getCurrentUser().getUid();

           String notifireferce ="notifications";

           String   totalReference =notifireferce+"/"+userId;


        objectList = firebaseHelper.DBselectAll(totalReference);
        for (int i=0;i<objectList.size();i++){

            notificationsList.add((Notifications) objectList.get(i));

        }



        Collections.reverse(notificationsList);
        notificationAdapter.notifyDataSetChanged();
        */




        /*




        //if (notification_reference.getDatabase()!=null) {
        ChildEventListener nchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    HAVE_NOTIFICATIONS = true;


                    Notifications notification = dataSnapshot.getValue(Notifications.class);
                    notificationsList.add(notification);
                    Collections.reverse(notificationsList);
                    notificationAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        notification_reference.addChildEventListener(nchildEventListener);






        // }else{
        //HAVE_NOTIFICATIONS=false;

        // }*/
        return notificationsList;
    }
}










  /*                              /// function to push notification

    //user id <who commented >  , publisher Id < who publish the post>

    // add notification on likes
    private  void addNotificationsOfLikes(String userid,String postid)
    {
       FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference notification_reference = database.getReference("notifications/likes").child(userid);

        HashMap<String, Object> hashMap =new HashMap<>();
        hashMap.put("userId ", userid); // or hashMap.put("userid ", firebaseUser.getUid());
        hashMap.put("NotificationMessage","liked your post");
        hashMap.put("postId ", postid);
        hashMap.put("isPost ", true);


        notification_reference.push().setValue(hashMap);

        //to call ----->  addNotificationsOfLikes(post.getpublisher() //getUserId\\ , post.getPostId())

    }


    //*************************************

    // add notification on Comments
        // needs to comment content and <who commented > and < who publish the post>
    // parameter can be comment text , user id <who commented >  , publisher Id < who publish the post>

    private  void addNotificationsOfComments()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference notification_reference = database.getReference("notifications/comments").child(PublisherId);

        HashMap<String, Object> hashMap =new HashMap<>();
        hashMap.put("userId ", userid); // or hashMap.put("userid ", firebaseUser.getUid());
        hashMap.put("NotificationMessage","commented:"+addcomment.getText().toString());
        hashMap.put("postId ", "");

        hashMap.put("isPost ", false);

        notification_reference.push().setValue(hashMap);


        //to call ----->  addNotificationsOfFollowers(post.getpublisher() //getUserId\\ )
    }


    //*************************************

    // add notification on followers putton on post
    private  void addNotificationsOfFollowers(String userid)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference notification_reference = database.getReference("notifications/follow").child(publisherid);

        HashMap<String, Object> hashMap =new HashMap<>();
        hashMap.put("userId ", userid); // or hashMap.put("userid ", firebaseUser.getUid());
        hashMap.put("NotificationMessage","started follows you ");
        hashMap.put("postId ", "");

        hashMap.put("isPost ", false);

        notification_reference.push().setValue(hashMap);


        //to call ----->  addNotificationsOfFollowers(post.getpublisher() //getUserId\\ )
    }

// add notification on followers putton on profile
    private  void addNotificationsOfFollowers(String userid)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference notification_reference = database.getReference("notifications").child(profile id );

        HashMap<String, Object> hashMap =new HashMap<>();
        hashMap.put("userId ", userid); // or hashMap.put("userid ", firebaseUser.getUid());
        hashMap.put("NotificationMessage","started follows you ");
        hashMap.put("postId ", "");

        hashMap.put("isPost ", false);

        notification_reference.push().setValue(hashMap);


        //to call ----->  addNotificationsOfFollowers(post.getpublisher() //getUserId\\ )
    }

*/




/************************************************** notification icon click listener*/










