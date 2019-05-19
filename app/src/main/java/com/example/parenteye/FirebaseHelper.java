package com.example.parenteye;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirebaseHelper {
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private String result ;
    private ArrayList <String> classesType = new ArrayList<String>();
    private ArrayList <Object> DataList = new ArrayList<>();

    
    /*
    Get current user return type  FirebaseUser;
     */




    public FirebaseUser getCurrentUser()
    {
        return mAuth.getCurrentUser();
    }





    public List<Object> DBselectAll(String TableReference  )

    {

       DatabaseReference refe = myRef.child("notifications").child("cR6RdBeU5Lg7CEFLhEniBT16ZxM2");
        refe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()) {
                    DataList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Notifications data = snapshot.getValue(Notifications.class);
                        DataList.add(data);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        return DataList;
    }


    public String LogIn(String Email, String Password) {

        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    result = "1";
                } else {
                    result = task.getException().toString();
                }
            }
        });
        return result;
    }

}
