package com.example.parenteye;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

import static com.example.parenteye.ActivityLogFragment.activityLogAdapter;

/*
Activity log
clever
 */
public class ActivityLog {
    private String id;
    private String childId;
    private String userId;
    private String ActivityLogMessage;
    private Date date;
    private boolean seen;

    //*********
    ////////
    private String postId;
    private String groupId;
    private String ParentId;


    // flags
    private boolean isPost;
    private boolean isComment;
    private boolean isLike;
    private boolean isFriendRequest;


    private boolean isGroupRequest ;
    private boolean isPage;

    public boolean getisPage() {
        return isPage;
    }

    public void setisPage(boolean iPage) {
        isPage = iPage;
    }
    public boolean getisGroupRequest() {
        return isGroupRequest;
    }

    public void setGroupRequest(boolean groupRequest) {
        isGroupRequest = groupRequest;
    }


    //  String record;
    //int index;
    /******************* firebase connection  *********************/
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseUser currentUser = mAuth.getCurrentUser();

    public List<ActivityLog> logActivityList  = new ArrayList<>();


    public ActivityLog() {

    }

    public ActivityLog(String id, String childId, String activityLogMessage, String userId, Date date,
                       boolean seen, boolean isPost, boolean isLike, boolean isComment, boolean isFriendRequest,boolean isGroupRequest,boolean isPage) {
        this.id = id;
        this.childId = childId;
        ActivityLogMessage = activityLogMessage;
        this.date = date;
        this.seen = seen;
        this.isLike = isLike;
        this.isComment = isComment;
        this.isPost = isPost;
        this.isFriendRequest = isFriendRequest;
        this.userId = userId;
        this.isGroupRequest = isGroupRequest;
        this.isPage = isPage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setchildId(String childId) {
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


    public String getuserId() {
        return userId;
    }

    public void setuserId(String friendId) {
        this.userId = friendId;
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



    DatabaseReference ParentChildren_reference = database.getReference("ParentChildren");




    /****************************** check of current user is child and get his parent id  ******************************/

    public String ParentOfCurrentUserChild(final String ChildId) {

         DatabaseReference ParentChildren_reference = database.getReference("ParentChildren");

        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
        });


//
//
//        ChildEventListener nchildEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if (dataSnapshot.exists()) {
//                    ParentChildren parentChildren = dataSnapshot.getValue(ParentChildren.class);
//
//                    if (parentChildren.getChildId().equalsIgnoreCase(ChildId)) {
//
//                        ParentId = parentChildren.getParentId();
//                    }else {
//                        String a="a";
//                    }
//
//
//
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//
//
//        ParentChildren_reference.addChildEventListener(nchildEventListener);


        return ChildId;
    }



    //****************************** add Activity log on like********************************
    public void addActivityLogOfLikes(final String postid) {


        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {

                            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());


                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("childId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
                            hashMap.put("ActivityLogMessage", "kide liked this post");
                            hashMap.put("postId", postid);
                            hashMap.put("isPost", true);
                            hashMap.put("isLike", true);
                            hashMap.put("isComment", false);
                            hashMap.put("isFriendRequest", false);
                            hashMap.put("isGroupRequest",false);


                            activityLog_reference.push().setValue(hashMap);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //to call ----->  addNotificationsOfLikes(String postid, String post_publisher_Id)

    }

    //************************  add activity log func on comment***************

    public void addActivityLogOfComments(final String postid , final String comment) {

        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {

                            ParentId = parentChildren.getParentId();
                            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());


                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("childId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
                            hashMap.put("ActivityLogMessage", "kid commented "+comment+"on this post");
                            hashMap.put("postId", postid);
                            hashMap.put("isPost", true);
                            hashMap.put("isLike", false);
                            hashMap.put("isComment", true);
                            hashMap.put("isFriendRequest", false);
                            hashMap.put("isGroupRequest",false);


                            activityLog_reference.push().setValue(hashMap);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //+addcomment.getText().toString()
        //to call ----->  addNotificationsOfFollowers(post.getpublisher() //getUserId\\ )
    }


    //************************  add activity log func on Send AddFriend request ***************

    public void addActivityLogOfSonSendFriendRequest(final String childReceiveRequestFriend_Id) {



        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {

                            ParentId = parentChildren.getParentId();
                            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());



                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("childId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
                            hashMap.put("ActivityLogMessage", "your kid sends a friend request to ");
                            hashMap.put("userId", childReceiveRequestFriend_Id);
                            hashMap.put("postId", " ");
                            hashMap.put("isPost", false);
                            hashMap.put("isLike", false);
                            hashMap.put("isComment", false);
                            hashMap.put("isFriendRequest", true);
                            hashMap.put("isGroupRequest",false);


                            activityLog_reference.push().setValue(hashMap);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //to call ----->  addNotificationsOfFriendRequest(FriendRequesterId)

    }


    //************************  add activity log func on recieve AddFriend request ***************

    public void addLogActivityReceiveFriendRequest(final String childReceiverRequestFriend_Id) {


        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(childReceiverRequestFriend_Id)) {

                            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());


                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("childId", childReceiverRequestFriend_Id); // or hashMap.put("userid ", firebaseUser.getUid());
                            hashMap.put("ActivityLogMessage", "kide receive friend request from ");
                            hashMap.put("userId", currentUser.getUid());
                            hashMap.put("postId", " ");
                            hashMap.put("isPost", false);
                            hashMap.put("isLike", false);
                            hashMap.put("isComment", false);
                            hashMap.put("isFriendRequest", true);
                            hashMap.put("isGroupRequest",false);


                            activityLog_reference.push().setValue(hashMap);


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        //to call ----->  addNotificationsOfFriendRequest(FriendRequesterId)

    }

    public void addLogActivityChildReceiveJoinGroupRequest(final String ChildGroupAdmin,final String groupId) {


        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(ChildGroupAdmin)) {

                            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());


                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("childId", ChildGroupAdmin); // or hashMap.put("userid ", firebaseUser.getUid());
                            hashMap.put("ActivityLogMessage", "kide receive join group request from ");
                            hashMap.put("userId", currentUser.getUid());
                            hashMap.put("postId", groupId);
                            hashMap.put("isPost", false);
                            hashMap.put("isLike", false);
                            hashMap.put("isComment", false);
                            hashMap.put("isFriendRequest", false);
                            hashMap.put("isGroupRequest",true);


                            activityLog_reference.push().setValue(hashMap);


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        //to call ----->  addNotificationsOfFriendRequest(FriendRequesterId)

    }
    public void addLogActivityChildSendGroupRequest(final String ChildGroupAdmin,final String groupId) {


        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {

                            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());


                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("childId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
                            hashMap.put("ActivityLogMessage", "kide receive join group request from ");
                            hashMap.put("userId", ChildGroupAdmin);
                            hashMap.put("postId", groupId);
                            hashMap.put("isPost", false);
                            hashMap.put("isLike", false);
                            hashMap.put("isComment", false);
                            hashMap.put("isFriendRequest", false);
                            hashMap.put("isGroupRequest", true);


                            activityLog_reference.push().setValue(hashMap);


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

        public void addLogActivityAcceptGroupRequest(final String userid,final String groupId) {


            ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                            if (parentChildren.getChildId().equalsIgnoreCase(userid)) {

                                DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());


                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("childId", userid); // or hashMap.put("userid ", firebaseUser.getUid());
                                hashMap.put("ActivityLogMessage", "kide joined group ");
                                hashMap.put("userId", currentUser.getUid());
                                hashMap.put("postId", groupId);
                                hashMap.put("isPost", false);
                                hashMap.put("isLike", false);
                                hashMap.put("isComment", false);
                                hashMap.put("isFriendRequest", false);
                                hashMap.put("isGroupRequest",true);


                                activityLog_reference.push().setValue(hashMap);


                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




            //to call ----->  addNotificationsOfFriendRequest(FriendRequesterId)

    }


    public void addLogActivityChildCreateGroup(final String groupId) {


        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {

                            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());


                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("childId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
                            hashMap.put("ActivityLogMessage", "kide Create group ");
                            hashMap.put("postId", groupId);
                            hashMap.put("isPost", false);
                            hashMap.put("isLike", false);
                            hashMap.put("isComment", false);
                            hashMap.put("isFriendRequest", false);
                            hashMap.put("isGroupRequest",true);


                            activityLog_reference.push().setValue(hashMap);


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        //to call ----->  addNotificationsOfFriendRequest(FriendRequesterId)

    }


    public void addLogActivityChildCreatePage(final String PageId) {


        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {

                            DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());


                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("childId", currentUser.getUid()); // or hashMap.put("userid ", firebaseUser.getUid());
                            hashMap.put("ActivityLogMessage", "kide Create page ");
                            hashMap.put("postId", PageId);
                            hashMap.put("isPost", false);
                            hashMap.put("isLike", false);
                            hashMap.put("isComment", false);
                            hashMap.put("isFriendRequest", false);
                            hashMap.put("isGroupRequest",true);
                            hashMap.put("isPage",true);

                            activityLog_reference.push().setValue(hashMap);


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        //to call ----->  addNotificationsOfFriendRequest(FriendRequesterId)

    }




    //************************************* Delete activity log func of Like ***************************

    public void DeleteLogOfLike(final String postid) {


        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {



                            final DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());

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
                }
            }





            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    //************************************* Delete activity log func of Comment ***************************


    public void DeleteLogOfComment(final String postid, final String comment) {
        String parentId = ParentOfCurrentUserChild(currentUser.getUid());




        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {



                            final DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());

                            activityLog_reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                            ActivityLog activityLog = snapshot.getValue(ActivityLog.class);

                                            if (activityLog.getIsComment() == true && activityLog.getChildId().equalsIgnoreCase(currentUser.getUid()) && activityLog.getPostId().equalsIgnoreCase(postid)&&activityLog.ActivityLogMessage.contains(comment)) {

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
                }
            }





            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });













    }
    //***************************************  Delete activity log  of child sent a Friend Request ******************************

    public void DeleteLogSonSendFriendRequest(final String childReceiveRequestFriend_Id) {


        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {



                            final DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());
                            activityLog_reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            ActivityLog activityLog = snapshot.getValue(ActivityLog.class);
                                            //notificationsList.add(notification);
                                            if (activityLog.getisFriendRequest() && activityLog.getuserId().equalsIgnoreCase(childReceiveRequestFriend_Id) && activityLog.getChildId().equalsIgnoreCase(currentUser.getUid())) {

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
                    }
                }






            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });























    }

    //***************************************  Delete activity log of child receive a Friend Request ******************************


    public void DeleteLogReceiverFriendRequest(final String childReceiveRequestFriend_Id) {

        String parentId = ParentOfCurrentUserChild(childReceiveRequestFriend_Id);

        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {



                            final DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());
                            activityLog_reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            ActivityLog activityLog = snapshot.getValue(ActivityLog.class);

                                            if (activityLog.getisFriendRequest() && activityLog.getuserId().equalsIgnoreCase(currentUser.getUid()) && activityLog.getChildId().equalsIgnoreCase(childReceiveRequestFriend_Id)) {

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
                }
            }






            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





//            notification_reference.child(record).removeValue();


    }

    public void DeleteLogSonCancelGroupRequest(final String AdminGroup , final String groupId) {


        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {


                            final DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());
                            activityLog_reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            ActivityLog activityLog = snapshot.getValue(ActivityLog.class);
                                            //notificationsList.add(notification);
                                            if (activityLog.getisGroupRequest() && activityLog.getuserId().equalsIgnoreCase(AdminGroup) && activityLog.getChildId().equalsIgnoreCase(currentUser.getUid()) && activityLog.getPostId().equalsIgnoreCase(groupId)) {

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
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    public void DeleteLogUserCancelGroupRequestToYourSon(final String userid , final String groupId) {


        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(currentUser.getUid())) {


                            final DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());
                            activityLog_reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            ActivityLog activityLog = snapshot.getValue(ActivityLog.class);
                                            //notificationsList.add(notification);
                                            if (activityLog.getisGroupRequest() && activityLog.getuserId().equalsIgnoreCase(currentUser.getUid()) && activityLog.getChildId().equalsIgnoreCase(userid) && activityLog.getPostId().equalsIgnoreCase(groupId)) {

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
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void DeleteLogRejectGroupRequest(final String userid , final String groupId) {


        ParentChildren_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ParentChildren parentChildren = snapshot.getValue(ParentChildren.class);

                        if (parentChildren.getChildId().equalsIgnoreCase(userid)) {


                            final DatabaseReference activityLog_reference = database.getReference("activityLog").child(parentChildren.getParentId());
                            activityLog_reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            ActivityLog activityLog = snapshot.getValue(ActivityLog.class);
                                            //notificationsList.add(notification);
                                            if (activityLog.getisGroupRequest() && activityLog.getuserId().equalsIgnoreCase(userid) && activityLog.getChildId().equalsIgnoreCase(currentUser.getUid()) && activityLog.getPostId().equalsIgnoreCase(groupId)) {

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
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }





    public List<ActivityLog> readLogs() {
        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //FirebaseHelper firebaseHelper = new FirebaseHelper();


        //String userId = "EzH9MI0WJ7N0AwZNUidC45e2wiP2";


        DatabaseReference Activity_reference = database.getReference("activityLog").child(user.getUid());



        Activity_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logActivityList.clear();
                activityLogAdapter.notifyDataSetChanged();

                if(dataSnapshot.exists())
                {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
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
        return logActivityList;

    }

}
