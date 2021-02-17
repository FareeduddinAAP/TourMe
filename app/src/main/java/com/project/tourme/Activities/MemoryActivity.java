package com.project.tourme.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.tourme.R;

public class MemoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StorageReference mStorageRef;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("MemoryImages");
        mRef = FirebaseDatabase.getInstance().getReference("Location");


    }
}