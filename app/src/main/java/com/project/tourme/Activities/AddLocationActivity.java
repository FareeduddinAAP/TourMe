package com.project.tourme.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.tourme.R;

import java.io.IOException;
import java.util.HashMap;

public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1012;
    private static final int IMAGE_PICKER_SELECT = 201;
    SupportMapFragment mapFrag;
    LatLng latLng;
    EditText inputLocationName;
    ImageView takePic;
    Button btnSave;
    ImageView myPic;
    CardView myPicCard;
    Uri imageUri;
    ProgressDialog progressDialog;


    StorageReference mStorageRef;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        inputLocationName = findViewById(R.id.inputLocationName);
        takePic = findViewById(R.id.takePic);
        btnSave = findViewById(R.id.btnSave);
        myPicCard = findViewById(R.id.myPicCard);
        myPic = findViewById(R.id.myPic);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Memory!");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("MemoryImages");
        mRef = FirebaseDatabase.getInstance().getReference("Location");


        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        latLng = new LatLng(getIntent().getDoubleExtra("latitude", 0.0), getIntent().getDoubleExtra("longitude", 0.0));


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveLocationInfo();
            }
        });

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakePicure();
            }
        });
    }

    private void TakePicure() {

        //Android device need permission from user ,can app use camera sensor or not
        if (ContextCompat.checkSelfPermission(AddLocationActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AddLocationActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            //if permission not allowd then request permision
            ActivityCompat.requestPermissions(AddLocationActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {

            //otherwise open camera to take image
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, "New Pic");
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Front Camera Pic");
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, IMAGE_PICKER_SELECT);
        }


    }

    private void SaveLocationInfo() {
        String markerName = inputLocationName.getText().toString();
        if (markerName.isEmpty()) {
            inputLocationName.setError("Enter Location Name");
            inputLocationName.requestFocus();
        } else if (imageUri == null) {
            Toast.makeText(this, "Please Take Memory Picture", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            String key = mRef.push().getKey();
            mStorageRef.child(mUser.getUid()).child(key).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        mStorageRef.child(mUser.getUid()).child(key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                HashMap hashMap = new HashMap();
                                hashMap.put("imageUrl", uri.toString());
                                hashMap.put("key", key);
                                hashMap.put("markerName", markerName);
                                hashMap.put("latitude", latLng.latitude);
                                hashMap.put("longitude", latLng.longitude);
                                mRef.child(mUser.getUid()).child(key).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AddLocationActivity.this, "Saved Your memory", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AddLocationActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        });
                    }


                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title("Current Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.addMarker(markerOptions);

    }

    //check Edither user allowsed permission or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICKER_SELECT) {
            try {
                if (imageUri != null) {
                    myPicCard.setVisibility(View.VISIBLE);
                }
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(AddLocationActivity.this.getContentResolver(), imageUri);
                myPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//If permisison alloed to open camera and take oic and user in URI and store in firebase database
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.Images.Media.TITLE, "New Pic");
                        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Front Camera Pic");
                        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, IMAGE_PICKER_SELECT);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}