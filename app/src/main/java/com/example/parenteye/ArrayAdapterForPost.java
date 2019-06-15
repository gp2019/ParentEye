package com.example.parenteye;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ArrayAdapterForPost extends ArrayAdapter<custom_posts_returned> {

    int count = 0;
    ArrayList<PostComments> comments_of_post = new ArrayList<>();
    private FirebaseAuth mAuth;
    private Clipboard clipboard = new Clipboard();
    private DatabaseReference dbRef, dbRef2, dbRef3;
    private Activity contextAdapter;
    final long ONE_MEGABYTE = 1024 * 1024;
    private StorageReference userStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");
    private StorageReference pageStorageRef = FirebaseStorage.getInstance().getReference("PageImages/");
    private ActivityLog activityLog = new ActivityLog();
    private StorageReference postStorageRef = FirebaseStorage.getInstance().getReference("PostImages/");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");
    DatabaseReference communityRef = database.getReference("Community");

    private Notifications notifications = new Notifications();
    ArrayList<custom_posts_returned> post_returnedd;
    private int position;

    public ArrayAdapterForPost(Activity context, ArrayList<custom_posts_returned> post_returned) {

        super(context, 0, post_returned);
        post_returnedd = post_returned;
        this.contextAdapter = context;
    }


    @NonNull
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {

        View postlist = convertView;

        postlist = LayoutInflater.from(getContext()).inflate(
                R.layout.list_posts, parent, false);


        final custom_posts_returned post = getItem(position);
        count = count + 1;
        final TextView postownername = (TextView) postlist.findViewById(R.id.postowner);
        // postownername.setText(post.getPost_owner_name());


        if (post_returnedd.get(position).getPlaceTypeId().equals("1")){
            userRef.child(post_returnedd.get(position).getpost_owner_ID()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        postownername.setText(dataSnapshot.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (post_returnedd.get(position).getPlaceTypeId().equals("2")){
            communityRef.child(post_returnedd.get(position).getCommunityId()).child("communityname").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        postownername.setText(dataSnapshot.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (post_returnedd.get(position).getPlaceTypeId().equals("3")){
            communityRef.child(post_returnedd.get(position).getCommunityId()).child("communityname").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        final String nameGroup = dataSnapshot.getValue(String.class);
                        userRef.child(post_returnedd.get(position).getpost_owner_ID()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){
                                    postownername.setText(dataSnapshot.getValue(String.class)+" Posted "+nameGroup+" Group");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        final TextView postDescription = postlist.findViewById(R.id.postDescription);
        if (post.getPost_text() != null && !(post.getPost_text().isEmpty())) {

            postDescription.setVisibility(View.VISIBLE);


            postDescription.setText(post.getPost_text());

        } else {
            postDescription.setVisibility(View.GONE);

        }

        TextView postdate = (TextView) postlist.findViewById(R.id.postdate);
        postdate.setText(post.getPost_date());


        final ImageView profileimage = (ImageView) postlist.findViewById(R.id.profile_post);
        final ImageView postimage = (ImageView) postlist.findViewById(R.id.post_image);

        if (post_returnedd.get(position).getPlaceTypeId().equals("1")||post_returnedd.get(position).getPlaceTypeId().equals("3")) {
            userRef.child(post_returnedd.get(position).getpost_owner_ID()).child("profile_pic_id").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        userStorageRef.child(dataSnapshot.getValue(String.class)).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                profileimage.setImageBitmap(bm);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if(post_returnedd.get(position).getPlaceTypeId().equals("2")){
            communityRef.child(post_returnedd.get(position).getpost_owner_ID()).child("photoId").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        pageStorageRef.child(dataSnapshot.getValue(String.class)).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                profileimage.setImageBitmap(bm);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (post.haspostimage() == true) {
            postimage.setVisibility(View.VISIBLE);
            postStorageRef.child(post.getPost_image()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    postimage.setImageBitmap(bm);

                }
            });


        } else {
            postimage.setVisibility(View.GONE);
        }

        final TextView countComment = postlist.findViewById(R.id.textComment);

        if (post_returnedd.get(position).getCountComment() == 0) {
            countComment.setVisibility(View.GONE);
        } else {

            countComment.setVisibility(View.VISIBLE);
            countComment.setText(post_returnedd.get(position).getCountComment() + " Comment");

        }

        final TextView countLike = postlist.findViewById(R.id.textLike);

        if (post_returnedd.get(position).getCountLike() == 0) {
            countLike.setVisibility(View.GONE);
        } else {

            countLike.setVisibility(View.VISIBLE);
            countLike.setText(post_returnedd.get(position).getCountLike() + " Like");

        }

        final RelativeLayout like_comment = postlist.findViewById(R.id.like_comment);

        if (post_returnedd.get(position).getCountLike() == 0 && post_returnedd.get(position).getCountComment() == 0) {
            like_comment.setVisibility(View.GONE);
        } else {
            like_comment.setVisibility(View.VISIBLE);
        }
        like_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_create_comment(position);
            }
        });

        final RelativeLayout btLike = postlist.findViewById(R.id.btLike);
        dbRef3 = FirebaseDatabase.getInstance().getReference().child("ReactionComment_Post");
        dbRef2 = FirebaseDatabase.getInstance().getReference().child("Posts");
        mAuth = FirebaseAuth.getInstance();

        final  ImageView imLike = postlist.findViewById(R.id.imLike);

        dbRef3.child(post_returnedd.get(position).getPost_Id() + mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    imLike.setImageResource(R.drawable.heart);
                } else {
                    imLike.setImageResource(R.drawable.heart_reaction);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String rectionId=post_returnedd.get(position).getPost_Id() + mAuth.getCurrentUser().getUid();
                Query queryToGetData = dbRef3.orderByChild("reactionId").equalTo(rectionId);
                queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            ReactionPosts reactionComment = new ReactionPosts(rectionId, post_returnedd.get(position).getPost_Id(), post_returnedd.get(position).getpost_owner_ID());
                            imLike.setImageResource(R.drawable.heart_reaction);
                            post_returnedd.get(position).setCountLike(post_returnedd.get(position).getCountLike() + 1);
                            countLike.setText(post_returnedd.get(position).getCountLike() + " Like");
                            dbRef3.child(rectionId).setValue(reactionComment);
                            dbRef2.child(post_returnedd.get(position).getPost_Id()).child("countLike").setValue(post_returnedd.get(position).getCountLike());
                            if (!post_returnedd.get(position).getpost_owner_ID().equals(mAuth.getCurrentUser().getUid())){
                                notifications.addNotificationsOfLikes(post_returnedd.get(position).getPost_Id(),post_returnedd.get(position).getpost_owner_ID());
                                activityLog.addActivityLogOfLikes(post_returnedd.get(position).getPost_Id());
                            }
                        } else {
                            imLike.setImageResource(R.drawable.heart);
                            post_returnedd.get(position).setCountLike(post_returnedd.get(position).getCountLike() - 1);
                            countLike.setText(post_returnedd.get(position).getCountLike() + " Like");
                            dbRef2.child(post_returnedd.get(position).getPost_Id()).child("countLike").setValue(post_returnedd.get(position).getCountLike());
                            dbRef3.child(rectionId).removeValue();
                            if (!post_returnedd.get(position).getpost_owner_ID().equals(mAuth.getCurrentUser().getUid())){
                            notifications.DeleteNotificationOfLike(post_returnedd.get(position).getPost_Id(),post_returnedd.get(position).getpost_owner_ID());
                                activityLog.DeleteLogOfLike(post_returnedd.get(position).getPost_Id());
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        final RelativeLayout btComment = postlist.findViewById(R.id.RelativebtComment);
        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_create_comment(position);
            }
        });

        final ImageView three_dots = postlist.findViewById(R.id.three_dots);

        three_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (post_returnedd.get(position).getpost_owner_ID().equals(currentUser.getUid())) {
                    PopupMenu popup = new PopupMenu(contextAdapter, three_dots);
                    popup.inflate(R.menu.edit_delet);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.Edit:
                                    UpdatePost(post_returnedd.get(position).getPost_Id(), post_returnedd.get(position).getPost_text(), post_returnedd.get(position).getPost_image(), post_returnedd.get(position).getpost_owner_ID(), post_returnedd.get(position).haspostimage());
                                    break;
                                case R.id.Delete:
                                    DeletPost(position, post_returnedd.get(position).getPost_Id());
                                    break;
                                case R.id.Copy:
                                    clipboard.setClipboard(contextAdapter, postDescription.getText().toString());
                                    Toast.makeText(contextAdapter, "Copied to clipboard", Toast.LENGTH_LONG).show();
                                    break;
                                case R.id.cancel:
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                } else {
                    PopupMenu popup = new PopupMenu(contextAdapter, three_dots);
                    popup.inflate(R.menu.copy_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.Copy:
                                    clipboard.setClipboard(contextAdapter, postDescription.getText().toString());
                                    Toast.makeText(contextAdapter, "Copied to clipboard", Toast.LENGTH_LONG).show();
                                    break;
                                case R.id.cancel:
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            }
        });

        return postlist;


    }

    private void DeletPost(final int position, final String postId) {
        dbRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        dbRef2 = FirebaseDatabase.getInstance().getReference().child("CommentsPost");
        dbRef3 = FirebaseDatabase.getInstance().getReference().child("ReactionComment_Post");
        dbRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments_of_post.clear();
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    PostComments postComments = commentSnapshot.getValue(PostComments.class);

                    if (postComments.getPostId() != null) {
                        if (postComments.getPostId().equals(postId)) {
                            dbRef2.child(postComments.getCommentID()).removeValue();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String keyRecId = post_returnedd.get(position).getPost_Id() + post_returnedd.get(position).getpost_owner_ID();

        dbRef3.child(keyRecId).removeValue();
        dbRef.child(post_returnedd.get(position).getPost_Id()).removeValue();

        comments_of_post.removeAll(comments_of_post);
        //notifyItemRangeChanged(position,comments_of_post.size());
        //mAdapter.notifyItemRemoved(position);
        //mAdapter.notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        int a ;

        if(post_returnedd != null && !post_returnedd.isEmpty()) {

            a = post_returnedd.size();
        }
        else {

            a = 0;

        }

        return a;

    }

    @Override
    public long getItemId(int position) {

        return position;

    }

    @Override

    public int getItemViewType(int position) {

        return position;

    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private  void  UpdatePost(String postId, String postcontent, String imageId,String userId,boolean hasImage){
        Intent intent = new Intent( contextAdapter, Create_Post.class );
        intent.putExtra( "postId",postId );
        intent.putExtra( "postcontent",postcontent );
        intent.putExtra( "imageId",imageId );
        intent.putExtra("userId",userId);
        intent.putExtra("hasImage",hasImage);
        contextAdapter.startActivity( intent );
    }

    private  void  go_create_comment(int position)
    {
        Intent intent = new Intent( contextAdapter, Create_Comment.class );
        intent.putExtra("postId",post_returnedd.get(position).getPost_Id());
        intent.putExtra("countOfLike",post_returnedd.get(position).getCountLike());
        intent.putExtra("position",position);
        contextAdapter.startActivity(intent);
    }
}
