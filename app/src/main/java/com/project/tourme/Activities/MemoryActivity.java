package com.project.tourme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.tourme.R;

import java.util.ArrayList;
import java.util.List;

public class MemoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StorageReference mStorageRef;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    List<mLocation> list;
    RecyclerviewAdaper adaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("MemoryImages");
        mRef = FirebaseDatabase.getInstance().getReference("Location");

        mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    mLocation location = snapshot1.getValue(mLocation.class);
                    list.add(location);
                }
                adaper=new RecyclerviewAdaper(list,MemoryActivity.this);
                recyclerView.setAdapter(adaper);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}