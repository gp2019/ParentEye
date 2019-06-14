package com.example.parenteye;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import static com.example.parenteye.NotificationFragment.activityLogAdapter;
import static com.example.parenteye.NotificationFragment.notificationAdapter;

/*
Activity log
clever
 */
public class ActivityLog {
    private String id;
    private String childId;
    private String friendId;
    private String ActivityLogMessage;
    private Date date;
    private boolean seen;

    //*********
    ////////
    private String postId;

    private String ParentId;


    // flags
    private boolean isPost;
    private boolean isComment;
    private boolean isLike;
    private boolean isFriendRequest;

    //  String record;
    //int index;
    /******************* firebase connection  *********************/
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseUser currentUser = mAuth.getCurrentUser();


    public ActivityLog() {

    }

    public ActivityLog(String id, String childId, String activityLogMessage, String friendId, Date date, boolean seen, boolean isPost, boolean isLike, boolean isComment, boolean isFriendRequest) {
        this.id = id;
        this.childId = childId;
        ActivityLogMessage = activityLogMessage;
        this.date = date;
        this.seen = seen;
        this.isLike = isLike;
        this.isComment = isComment;
        this.isPost = isPost;
        this.isFriendRequest = isFriendRequest;
        this.friendId = friendId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String childId) {
        this.childId = childId;
    }

    public void setActivityLogMessage(String activitylogMessage) {
        ActivityLogMessage = activitylogMessage;
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

    public String getChildId() {
        return childId;
    }

    public String getActivityLogMessage() {
        return ActivityLogMessage;
    }

    public Date getDate() {
        return date;
    }

    public boolean isSeen() {
        return seen;
    }

    ////////


    public String getfriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
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


    /****************************** check of current user is child and get his parent id  ******************************/

    public String ParentOfCurrentUserChild(final String ChildId) {
        final DatabaseReference ParentChildren_reference = database.getReference("ParentChildren");

        ParentChildren_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(ChildId)) {

                            ParentId = parentChildren.getParentId();
                        }else {
                            String a="a";
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

        return ParentId;

    }


    //****************************** add Activity log on like********************************
    public void addActivityLogOfLikes(String postid) {
        String parent = ParentOfCurrentUserChild(currentUser.getUid());

        if (parent != null) {
            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parent);


            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("childId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
            hashMap.put("ActivityLogMessage", "kide liked this post");
            hashMap.put("postId", postid);
            hashMap.put("isPost", true);
            hashMap.put("isLike", true);
            hashMap.put("isComment", false);
            hashMap.put("isFriendRequest", false);


            activityLog_reference.push().setValue(hashMap);


        }else {
            String a="s";
        }

        //to call ----->  addNotificationsOfLikes(String postid, String post_publisher_Id)

    }

    //************************  add activity log func on comment***************

    public void addActivityLogOfComments(String postid) {

        String parentId = ParentOfCurrentUserChild(currentUser.getUid());

        if (parentId != null) {
            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentId);


            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("childId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
            hashMap.put("ActivityLogMessage", "kid commented on this post");
            hashMap.put("postId", postid);
            hashMap.put("isPost", true);
            hashMap.put("isLike", false);
            hashMap.put("isComment", true);
            hashMap.put("isFriendRequest", false);


            activityLog_reference.push().setValue(hashMap);


        }

        //+addcomment.getText().toString()
        //to call ----->  addNotificationsOfFollowers(post.getpublisher() //getUserId\\ )
    }


    //************************  add activity log func on Send AddFriend request ***************

    public void addActivityLogOfSendFriendRequest(String FriendWantToRequest_Id) {


        String parentId = ParentOfCurrentUserChild(currentUser.getUid());

        if (parentId != null) {
            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentId);


            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("childId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
            hashMap.put("friendId", FriendWantToRequest_Id);
            hashMap.put("ActivityLogMessage", "kid send friend request to ");
            hashMap.put("postId", " ");
            hashMap.put("isPost", false);
            hashMap.put("isLike", false);
            hashMap.put("isComment", false);
            hashMap.put("isFriendRequest", true);

            activityLog_reference.push().setValue(hashMap);


        }

        //to call ----->  addNotificationsOfFriendRequest(FriendRequesterId)

    }


    //************************  add activity log func on Send AddFriend request ***************

    public void addLogReceiveFriendRequest(String childReceiveRequestFriend_Id) {


        String parentId = ParentOfCurrentUserChild(childReceiveRequestFriend_Id);

        if (parentId != null) {
            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentId);


            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("childId", childReceiveRequestFriend_Id); // or hashMap.put("userid ", firebaseUser.getUid());
            hashMap.put("friendId", currentUser.getUid());
            hashMap.put("ActivityLogMessage", "kide receive friend request from ");
            hashMap.put("postId", " ");
            hashMap.put("isPost", false);
            hashMap.put("isLike", false);
            hashMap.put("isComment", false);
            hashMap.put("isFriendRequest", true);

            activityLog_reference.push().setValue(hashMap);

        }

        //to call ----->  addNotificationsOfFriendRequest(FriendRequesterId)

    }
    //************************************* Delete activity log func of Like ***************************

    public void DeleteLogOfLike(final String postid) {

        String parentId = ParentOfCurrentUserChild(currentUser.getUid());

        if (parentId != null) {
            final DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentId);


            activityLog_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            ActivityLog activityLog = snapshot.getValue(ActivityLog.class);

                            if (activityLog.getIsLike() == true && activityLog.getChildId().equalsIgnoreCase(currentUser.getUid()) && activityLog.getPostId().equalsIgnoreCase(postid)) {

                                activityLog_reference.child(snapshot.getKey()).removeValue();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    //************************************* Delete activity log func of Comment ***************************


    public void DeleteLogOfComment(final String postid, final String post_publisher_Id) {
        String parentId = ParentOfCurrentUserChild(currentUser.getUid());

        if (parentId != null) {
            final DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentId);


            activityLog_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            ActivityLog activityLog = snapshot.getValue(ActivityLog.class);

                            if (activityLog.getIsComment() == true && activityLog.getChildId().equalsIgnoreCase(currentUser.getUid()) && activityLog.getPostId().equalsIgnoreCase(postid)) {

                                activityLog_reference.child(snapshot.getKey()).removeValue();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    //***************************************  Delete activity log  of child sent a Friend Request ******************************

    public void DeleteLogSendFriendRequest(final String FriendWantToRequest_Id) {

        String parentId = ParentOfCurrentUserChild(currentUser.getUid());

        if (parentId != null) {
            final DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentId);

            activityLog_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ActivityLog activityLog = snapshot.getValue(ActivityLog.class);
                            //notificationsList.add(notification);
                            if (activityLog.getisFriendRequest() && activityLog.getfriendId().equalsIgnoreCase(FriendWantToRequest_Id) && activityLog.getChildId().equalsIgnoreCase(currentUser.getUid())) {

                                activityLog_reference.child(snapshot.getKey()).removeValue();
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

    //***************************************  Delete activity log of child receive a Friend Request ******************************


    public void DeleteLogreceiveFriendRequest(final String childReceiveRequestFriend_Id) {

        String parentId = ParentOfCurrentUserChild(childReceiveRequestFriend_Id);

        if (parentId != null) {
            final DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentId);

            activityLog_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ActivityLog activityLog = snapshot.getValue(ActivityLog.class);

                            if (activityLog.getisFriendRequest() && activityLog.getfriendId().equalsIgnoreCase(currentUser.getUid()) && activityLog.getChildId().equalsIgnoreCase(childReceiveRequestFriend_Id)) {

                                activityLog_reference.child(snapshot.getKey()).removeValue();
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


    public List<ActivityLog> logActivityList  = new ArrayList<>();


    public void readLogs() {
        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //FirebaseHelper firebaseHelper = new FirebaseHelper();


        //String userId = "EzH9MI0WJ7N0AwZNUidC45e2wiP2";


        DatabaseReference notification_reference = database.getReference("activityLog").child(user.getUid());



        notification_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    logActivityList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ActivityLog activityLog = snapshot.getValue(ActivityLog.class);
                        logActivityList.add(activityLog);
                    }
                    Collections.reverse(logActivityList);
                    activityLogAdapter.notifyDataSetChanged();
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
    }

}
