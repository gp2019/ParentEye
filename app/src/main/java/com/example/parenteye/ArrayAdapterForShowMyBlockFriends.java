package com.example.parenteye;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ArrayAdapterForShowMyBlockFriends extends RecyclerView.Adapter{
    final long ONE_MEGABYTE = 1024 * 1024;
    public ArrayList<String> friendsArrayList;
    public Context contextAdapter;
    Users users = new Users();
    private int position;
    private DatabaseReference dbRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private Button remove,unblock;

    public ArrayAdapterForShowMyBlockFriends(Context context, ArrayList<String> friends) {
        this.contextAdapter = context;
        this.friendsArrayList = friends;
    }



    public class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageViewUser;
        TextView textViewUserName;

        public ViewHolder(View itemView) {
            super( itemView );
            imageViewUser=itemView.findViewById( R.id.childphoto );
            textViewUserName=itemView.findViewById( R.id.childname );
            remove = itemView.findViewById(R.id.remove_friend);
            unblock  = itemView.findViewById(R.id.block_friend);

        }

    }

    private void add(final String IdFreiends, final String idcurrentuser){
        dbRef = FirebaseDatabase.getInstance().getReference().child( "Friends" );
        dbRef.child(idcurrentuser).child("userFriends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class) != null&&!dataSnapshot.getValue(String.class).isEmpty()) {
                    String myfriends = dataSnapshot.getValue(String.class);
                    String[] myFriendsID = myfriends.split(",");
                    List<String> list = new ArrayList<String>(Arrays.asList(myFriendsID));
                    list.add(IdFreiends);
                    myFriendsID = list.toArray(new String[0]);
                    StringBuilder csvBuilder = new StringBuilder();

                    for (String friends : myFriendsID) {
                        csvBuilder.append(friends);
                        csvBuilder.append(",");
                    }
                    dbRef.child(idcurrentuser).child("userFriends").setValue(csvBuilder.toString());
                }
                else {
                    dbRef.child(idcurrentuser).child("userFriends").setValue(IdFreiends);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void unblock(final String IdFreiends, final String idcurrentuser){
        dbRef = FirebaseDatabase.getInstance().getReference().child( "Friends" );
        dbRef.child(idcurrentuser).child("blockFriends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class) != null) {
                    String myfriends = dataSnapshot.getValue(String.class);
                    String[] myFriendsID = myfriends.split(",");
                    List<String> list = new ArrayList<String>(Arrays.asList(myFriendsID));
                    list.remove(IdFreiends);
                    myFriendsID = list.toArray(new String[0]);
                    StringBuilder csvBuilder = new StringBuilder();

                    for (String friends : myFriendsID) {
                        csvBuilder.append(friends);
                        csvBuilder.append(",");
                    }
                        dbRef.child(idcurrentuser).child("blockFriends").setValue(csvBuilder.toString());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( contextAdapter ).inflate( R.layout.who_did_like, parent, false );
        return new ArrayAdapterForShowMyBlockFriends.ViewHolder( v );
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        mAuth = FirebaseAuth.getInstance();

        remove.setVisibility(View.GONE);
        unblock.setText("un block");
        unblock.setVisibility(View.VISIBLE);

        unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unblock(friendsArrayList.get(position),mAuth.getCurrentUser().getUid());
                add(friendsArrayList.get(position),mAuth.getCurrentUser().getUid());
            }
        });

        dbRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");

        dbRef.child(friendsArrayList.get( position )).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(Users.class);

                if (users.getProfile_pic_id()!=null){
                    mStorageRef.child(users.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            final Bitmap bm = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
                            ((ArrayAdapterForShowMyBlockFriends.ViewHolder)holder).imageViewUser.setImageBitmap(bm);
                        }
                    });
                }

                ((ArrayAdapterForShowMyBlockFriends.ViewHolder)holder).textViewUserName.setText(users.getUsername());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(contextAdapter, "type error "+databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public int getItemCount() {

        int a ;

        if(friendsArrayList != null && !friendsArrayList.isEmpty()) {

            a = friendsArrayList.size();
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


}
