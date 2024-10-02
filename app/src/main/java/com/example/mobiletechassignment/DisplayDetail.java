package com.example.mobiletechassignment;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class DisplayDetail extends AppCompatActivity {
    private String eventTitle;
    private String eventSummary;
    private String eventTime;
    private String eventDates;
    private String eventContact;
    private String eventLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail);
        setTitle("Event Detail");
        displayData();




    }
    public void displayData(){
        MyDBHelper dbHelper = new MyDBHelper(
                this, "MobileTech", null, 1);
        String eventName = dbHelper.selectAllEvent();
        TextView header = findViewById(R.id.DisplayHeader);
        header.setText(eventName);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("eventTitle")) {
            eventTitle = intent.getStringExtra("eventTitle");
            TextView title = findViewById(R.id.displayTitle);
            title.setText("Title: "+ eventTitle);

        }
        if(intent != null && intent.hasExtra("eventSummary")) {
            eventSummary = intent.getStringExtra("eventSummary");
            TextView summary = findViewById(R.id.displaySummary);
            summary.setText("Summary: " + eventSummary);

        }
        if(intent != null && intent.hasExtra("eventTime")) {
            eventTime = intent.getStringExtra("eventTime");
            TextView time = findViewById(R.id.displayTime);
            time.setText("Time: "+ eventTime);

        }
        if(intent != null && intent.hasExtra("eventDates")) {
            eventDates = intent.getStringExtra("eventDates");
            TextView dates = findViewById(R.id.displayDates);
            dates.setText("Dates: " + eventDates);

        }
        if(intent != null && intent.hasExtra("eventContact")) {
            eventContact = intent.getStringExtra("eventContact");
            TextView contact = findViewById(R.id.displayContact);
            contact.setText("Contact:" + eventContact);

        }
        if(intent != null && intent.hasExtra("eventLocation")) {
            eventLocation = intent.getStringExtra("eventLocation");
            TextView location = findViewById(R.id.displayLocation);
            location.setText("Location: " + eventLocation);

        }
    }
    public void map(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}