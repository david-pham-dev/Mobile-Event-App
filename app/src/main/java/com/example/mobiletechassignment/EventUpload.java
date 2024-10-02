package com.example.mobiletechassignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.UUID;


public class EventUpload extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 1000;
    private static final int REQUEST_PERMISSION = 3000;
    private Uri imageFileUri;
    private String imageKey;
    private ImageView imageView;
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_upload);
        setTitle("Upload an Image for new Activity");
        imageView = findViewById(R.id.imageEventUpload);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("eventKey")) {
            key = intent.getStringExtra("eventKey");

        }
    }


    public void Done(View view){
        MyDBHelper dbHelper = new MyDBHelper(
                this, "MobileTech", null, 1);
        String eventName = dbHelper.selectAllEvent();
        DatabaseReference dbRef = FirebaseDatabase.
                getInstance().getReference("O-Week").child(eventName);
        imageKey = dbRef.push().getKey().toString();
        dbRef.child(key).child("imageFileName").setValue(imageKey);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(imageKey).putFile(imageFileUri);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }

    public void capture(View view) {
        if (checkPermissions() == false)
            return;
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFileUri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        activityResultLauncher.launch(takePhotoIntent);
    }
    public void load(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(galleryIntent);
    }
    private boolean checkPermissions() {
        String cameraPermission = android.Manifest.permission.CAMERA;
        boolean grantCamera =
                ContextCompat.checkSelfPermission(this,
                        cameraPermission) == PackageManager.PERMISSION_GRANTED;
        if (!grantCamera) {
            ActivityCompat.requestPermissions(this,
                    new String[]{cameraPermission}, REQUEST_PERMISSION);
        }
        return grantCamera;
    }
     ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null && result.getData().getData() != null)
                            imageFileUri = result.getData().getData();
                        imageView.setImageURI(imageFileUri);

                    }}

            });



}