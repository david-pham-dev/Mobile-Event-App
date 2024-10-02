package com.example.mobiletechassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("UC Bruce Campus Map and Events");
        EditText editText = findViewById(R.id.editTextEventName);
        MyDBHelper dbHelper = new MyDBHelper(
                this, "MobileTech", null, 1);
        String eventName = dbHelper.selectAllEvent();
        if(eventName.isEmpty()){
            editText.setText("");
        }
        else{
            editText.setText(eventName);
        }

    }


    public void openEventDetail(View view){
        Intent intent = new Intent(this, EventDetail.class);
        EditText editText = findViewById(R.id.editTextEventName);
        String editText1 = editText.getText().toString();
        MyDBHelper dbHelper = new MyDBHelper(
                this, "MobileTech", null, 1);
        long id1 = dbHelper.insertEvent(editText1);
        String eventName = dbHelper.selectAllEvent();
        intent.putExtra("content", eventName );
        startActivity(intent);
    }
    public void viewMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


}