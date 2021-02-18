package com.project.tourme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    //dec veriable
    StorageReference mStorageRef;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    SupportMapFragment mapFrag;
    LatLng latLng;
    EditText inputLocationName;
    ImageView takePic;
    Button btnSave;
    ImageView myPic;
    CardView myPicCard;
    GoogleMap mGoogleMap;
    String key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        //init varibale
        inputLocationName = findViewById(R.id.inputLocationName);
        takePic = findViewById(R.id.takePic);
        btnSave = findViewById(R.id.btnSave);
        myPicCard = findViewById(R.id.myPicCard);
        myPic = findViewById(R.id.myPic);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        //init database veribale
        mStorageRef = FirebaseStorage.getInstance().getReference().child("MemoryImages");
        mRef = FirebaseDatabase.getInstance().getReference("Location");
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        key = getIntent().getStringExtra("key");

    }


    //load user data (all memory )
    private void LoadUserData() {
        mRef.child(mUser.getUid()).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mLocation location = snapshot.getValue(mLocation.class);
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(location.getMarkerName()));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));

                inputLocationName.setText(location.getMarkerName());
                Picasso.get().load(location.getImageUrl()).into(myPic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //when map ready call this method
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LoadUserData();
        mGoogleMap = googleMap;

    }
}