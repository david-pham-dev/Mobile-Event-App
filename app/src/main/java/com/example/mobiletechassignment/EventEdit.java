package com.example.mobiletechassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventEdit extends AppCompatActivity {
    private double latitude;
    private double longitude;
    private String eventDetailHeader;
    public String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        Bundle extra = getIntent().getExtras();
        eventDetailHeader = extra.getString("eventDetailHeader");
        String eventDetailTitle = extra.getString("eventDetailTitle");
        String eventDetailSummary = extra.getString("eventDetailSummary");
        String eventDetailTime = extra.getString("eventDetailTime");
        String eventDetailDates = extra.getString("eventDetailDates");
        String eventDetailContact = extra.getString("eventDetailContact");
        String spinnerData = extra.getString("selectedItem");
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);
        //header
        TextView eventEditHeader = findViewById(R.id.eventEditHeader);
        eventEditHeader.setText(eventDetailHeader);

        EditText eventEditLocation = findViewById(R.id.eventEditLocation);
        EditText eventEditTitle = findViewById(R.id.eventEditTitle);
        EditText eventEditSummary = findViewById(R.id.eventEditSummary);
        EditText eventEditContact = findViewById(R.id.eventEditContact);
        EditText eventEditDates = findViewById(R.id.eventEditDates);
        EditText eventEditTime = findViewById(R.id.eventEditTime);

        //title
//        EditText eventEditTitle = findViewById(R.id.eventEditTitle);
        eventEditTitle.setText(eventDetailTitle);
//        String title = eventEditTitle.getText().toString();

        //summary
//        EditText eventEditSummary = findViewById(R.id.eventEditSummary);
        eventEditSummary.setText(eventDetailSummary);
//        String summary = eventEditSummary.getText().toString();

        //time
//        EditText eventEditTime = findViewById(R.id.eventEditTime);
        eventEditTime.setText(eventDetailTime);
//        String time = eventEditTime.getText().toString();

        //dates
//        EditText eventEditDates = findViewById(R.id.eventEditDates);
        eventEditDates.setText(eventDetailDates);
//        String dates = eventEditDates.getText().toString();

        //contact
//        EditText eventEditContact = findViewById(R.id.eventEditContact);
        eventEditContact.setText(eventDetailContact);
//        String contact = eventEditContact.getText().toString();

        //location spinner
//        EditText eventEditLocation = findViewById(R.id.eventEditLocation);
        eventEditLocation.setText(spinnerData);
//        String location = eventEditLocation.getText().toString();


        setTitle("Edit an Existing Event");
//        uploadMultipleValuesToRealtimeDB(title,location,summary,time,contact,dates);

    }

    public void uploadMultipleValuesToRealtimeDB(String title, String location, String summary, String time, String contact, String dates, double latitude, double longitude)
    {
        Bundle extra = getIntent().getExtras();
        String eventDetailHeader = extra.getString("eventDetailHeader");
        DatabaseReference dbRef = FirebaseDatabase.
                getInstance().getReference("O-Week").child(eventDetailHeader);
        key = dbRef.push().getKey();

// to generate a random key
        dbRef.child(key).child("title").setValue(title);
        dbRef.child(key).child("title").setValue(title);
        dbRef.child(key).child("location").setValue(location);
        dbRef.child(key).child("summary").setValue(summary);
        dbRef.child(key).child("time").setValue(time);
        dbRef.child(key).child("contact").setValue(contact);
        dbRef.child(key).child("dates").setValue(dates);
        dbRef.child(key).child("latLng").child("latitude").setValue(latitude);
        dbRef.child(key).child("latLng").child("longitude").setValue(longitude);
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("eventKey",key);
        intent.putExtra("eventDetailHeader",eventDetailHeader);

    }
    public void updateActivity(View view) {
          Intent intent = new Intent(this, EventUpload.class);
        EditText eventEditLocation = findViewById(R.id.eventEditLocation);
        EditText eventEditTitle = findViewById(R.id.eventEditTitle);
        EditText eventEditSummary = findViewById(R.id.eventEditSummary);
        EditText eventEditContact = findViewById(R.id.eventEditContact);
        EditText eventEditDates = findViewById(R.id.eventEditDates);
        EditText eventEditTime = findViewById(R.id.eventEditTime);
        String dates = eventEditDates.getText().toString();
        String summary = eventEditSummary.getText().toString();
        String title = eventEditTitle.getText().toString();
        String time = eventEditTime.getText().toString();
        String contact = eventEditContact.getText().toString();
        String location = eventEditLocation.getText().toString();
        uploadMultipleValuesToRealtimeDB(title,location,summary,time,contact,dates,latitude,longitude);
        intent.putExtra("eventKey",key);
        startActivity(intent);


    }
}