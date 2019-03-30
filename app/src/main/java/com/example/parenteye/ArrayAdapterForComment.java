package com.example.parenteye;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.util.ArrayList;

public class ArrayAdapterForComment extends RecyclerView.Adapter {


    private DatabaseReference dbRef,dbRef2,dbRef3;
    private StorageReference mStorageRef;
    CreateTime calTime ;
    final long ONE_MEGABYTE = 1024 * 1024;
    public ArrayList<PostComments> CommentArrayList;
    public Context contextAdapter;
    Users users = new Users();
    PostComments postComments = new PostComments(  );
    private int currPosition;

    public ArrayAdapterForComment(Context context, ArrayList<PostComments> comments) {
        this.contextAdapter = context;
        this.CommentArrayList = comments;
    }

    public ArrayAdapterForComment() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        ImageView imageViewComment,imageViewUser;
        TextView textViewComment, timeOfComment, countLikeOfComment, replyOfComment,textViewUserName;
        Button btLike;

        public ViewHolder(View itemView) {
            super( itemView );
            imageViewComment = itemView.findViewById( R.id.imageViewComment );
            textViewComment = itemView.findViewById( R.id.textViewComment );
            timeOfComment = itemView.findViewById( R.id.TimeOfComment );
            countLikeOfComment = itemView.findViewById( R.id.countLikeOfComment );
            replyOfComment = itemView.findViewById( R.id.replyOfComment );
            btLike = itemView.findViewById( R.id.btLike );
            imageViewUser=itemView.findViewById( R.id.imageUserComment );
            textViewUserName=itemView.findViewById( R.id.name_of_user );

            itemView.setOnCreateContextMenuListener(this); //REGISTER ONCREATE MENU LISTENER
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem Edit = menu.add( Menu.NONE, 1, 1, "Edit");
            MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Delete");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        private  MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1:

                        break;

                    case 2:
                        DeletComment();
                        break;
                }
                return true;
            }
        };

        private  void DeletComment() {
            dbRef2= FirebaseDatabase.getInstance().getReference().child("CommentsPost");
            dbRef3= FirebaseDatabase.getInstance().getReference().child("ReactionComment");
            String keyCoPost= CommentArrayList.get(currPosition).getCommentID();
            String keyRecId= currPosition +CommentArrayList.get(currPosition ).getPostId()+CommentArrayList.get( currPosition ).getUserId();



            if (CommentArrayList.get( getAdapterPosition() ).isDidLike()==true){
                dbRef3.child( keyRecId ).removeValue();
                dbRef2.child( keyCoPost ).removeValue();
            }
            else {
                dbRef2.child( keyCoPost ).removeValue();
            }

            CommentArrayList.remove( currPosition );
            notifyItemRemoved(currPosition);
            notifyItemRangeChanged(currPosition,CommentArrayList.size());

        }


    }

    @NonNull
    @Override
     public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( contextAdapter ).inflate( R.layout.list_item_comment, parent, false );
        return new ViewHolder( v );
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
         currPosition=position;
        dbRef= FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef2= FirebaseDatabase.getInstance().getReference().child("CommentsPost");
        dbRef3= FirebaseDatabase.getInstance().getReference().child("ReactionComment");
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");

        if (users.getProfile_pic_id()!=null){
            mStorageRef.child(users.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final Bitmap bm = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
                    ((ViewHolder)holder).imageViewUser.setImageBitmap(bm);
                }
            });
        }
        else {

            mStorageRef.child("1ec33d61-8a29-433a-9d97-84cea5da78ba").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final Bitmap bm = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
                    ((ViewHolder)holder).imageViewUser.setImageBitmap(bm);
                }
            });
        }

        dbRef.child(CommentArrayList.get( position ).getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                users = dataSnapshot.getValue(Users.class);
                ((ViewHolder)holder).textViewUserName.setText(users.getUsername());
                //Toast.makeText(Create_Post.this, "the User ID "+users.getUsername()+"the type post is "+typePost,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(contextAdapter, "type error "+databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        if (CommentArrayList.get( position ).getImageId() == null) {
            ((ViewHolder) holder).imageViewComment.setVisibility( View.GONE );
        } else {
            //  ((ViewHolder) holder).imageViewComment.setImageBitmap( CommentArrayList.get( position ).getImageId() );
        }

        if (CommentArrayList.get( position ).getCommentcontent() == null) {
            ((ViewHolder) holder).textViewComment.setVisibility( View.GONE );
        } else {
            ((ViewHolder) holder).textViewComment.setText( CommentArrayList.get( position ).getCommentcontent() );
        }

        //CreateTime CurrTime = new CreateTime(new CreateTime(  ).getDateTime(),)
        String timePublish=CommentArrayList.get( position ).getCommentDate();

        try {
            calTime =new CreateTime(timePublish);
            calTime.sdf();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((ViewHolder) holder).timeOfComment.setText( calTime.calculateTime() );

        ((ViewHolder) holder).countLikeOfComment.setText(  CommentArrayList.get( position ).getCountOfLike()+" Like" );

        if (CommentArrayList.get( position ).isDidLike()){
            ((ViewHolder)holder).btLike.setBackgroundResource( R.drawable.heart_reaction );
        }
        else {
            ((ViewHolder)holder).btLike.setBackgroundResource( R.drawable.heart );
        }

        ((ViewHolder) holder).btLike.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReactionPosts reactionComment= new ReactionPosts(CommentArrayList.get( position ).getPostId(),CommentArrayList.get( position ).getUserId());
                if (CommentArrayList.get( position ).isDidLike()){
                    ((ViewHolder)holder).btLike.setBackgroundResource( R.drawable.heart);
                    CommentArrayList.get( position ).setCountOfLike( CommentArrayList.get( position ).getCountOfLike() - 1 );
                    ((ViewHolder) holder).countLikeOfComment.setText( CommentArrayList.get( position ).getCountOfLike() +" Like" );
                    String keyRecId= position +CommentArrayList.get( position ).getPostId()+CommentArrayList.get( position ).getUserId();
                    dbRef3.child( keyRecId ).removeValue();


                    dbRef2.child( CommentArrayList.get( position ).getCommentID() ).child( "didLike" ).setValue( false );
                    dbRef2.child( CommentArrayList.get( position ).getCommentID() ).child( "countOfLike" ).setValue(  CommentArrayList.get( position ).getCountOfLike()  );
                }
                else {
                    ((ViewHolder)holder).btLike.setBackgroundResource( R.drawable.heart_reaction);
                    CommentArrayList.get( position ).setCountOfLike( CommentArrayList.get( position ).getCountOfLike() + 1 );
                    ((ViewHolder) holder).countLikeOfComment.setText(  CommentArrayList.get( position ).getCountOfLike()+ " Like");
                    String recID = position +CommentArrayList.get( position ).getPostId()+CommentArrayList.get( position ).getUserId();
                    dbRef3.child( recID ).setValue( reactionComment );

                    dbRef2.child( CommentArrayList.get( position ).getCommentID() ).child( "countOfLike" ).setValue(  CommentArrayList.get( position ).getCountOfLike()  );
                    dbRef2.child( CommentArrayList.get( position ).getCommentID() ).child( "didLike" ).setValue( true );
                }



            }
        } );

     /*   ((ViewHolder) holder).itemView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(contextAdapter, holder.itemView);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.edit_delet, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(contextAdapter,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu

                return false;
            }
        } );



        /*holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Overridev
            public void onClick(View v) {

                Intent intent = new Intent( contextAdapter, Details.class );
                ((ViewHolder) holder).image.setDrawingCacheEnabled( true );
                Bitmap b = ((ViewHolder) holder).image.getDrawingCache();
                intent.putExtra( "image", b );
                intent.putExtra( "country", placesArrayList.get( position ).getCountry() );
                intent.putExtra( "description", placesArrayList.get( position ).getDescription() );
                contextAdapter.startActivity( intent );

            }
        } );
*/

    }




    @Override
    public int getItemCount() {

        int a ;

        if(CommentArrayList != null && !CommentArrayList.isEmpty()) {

            a = CommentArrayList.size();
        }
        else {

            a = 0;

        }

        return a;
    }


}