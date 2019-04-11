package com.example.parenteye;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ArrayAdapterForShowDidLike  extends RecyclerView.Adapter  {
    final long ONE_MEGABYTE = 1024 * 1024;
    public ArrayList<ReactionPosts> reactionPostsArrayList;
    public Context contextAdapter;
    Users users = new Users();
    private int position;
    private DatabaseReference dbRef;
    private StorageReference mStorageRef;

    public ArrayAdapterForShowDidLike(Context context, ArrayList<ReactionPosts> reactionPosts) {
        this.contextAdapter = context;
        this.reactionPostsArrayList = reactionPosts;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewUser;
        TextView textViewUserName;

        public ViewHolder(View itemView) {
            super( itemView );
            imageViewUser=itemView.findViewById( R.id.childphoto );
            textViewUserName=itemView.findViewById( R.id.childname );
        }


    }
    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( contextAdapter ).inflate( R.layout.child_list, parent, false );
        return new ViewHolder( v );
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        dbRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");


        dbRef.child(reactionPostsArrayList.get( position ).getUserid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(Users.class);

                if (users.getProfile_pic_id()!=null){
                    mStorageRef.child(users.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            final Bitmap bm = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
                            ((ArrayAdapterForShowDidLike.ViewHolder)holder).imageViewUser.setImageBitmap(bm);
                        }
                    });
                }

                ((ArrayAdapterForShowDidLike.ViewHolder)holder).textViewUserName.setText(users.getUsername());


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

        if(reactionPostsArrayList != null && !reactionPostsArrayList.isEmpty()) {

            a = reactionPostsArrayList.size();
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
