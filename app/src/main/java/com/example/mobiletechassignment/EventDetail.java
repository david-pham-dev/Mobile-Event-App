package com.example.mobiletechassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mobiletechassignment.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EventDetail extends AppCompatActivity {
    private double currentLatitude;
    private double currentLongitude;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Bundle extra = getIntent().getExtras();
        String ms = extra.getString("content");

        TextView textView = findViewById(R.id.eventDetailHeader);
        textView.setText(ms);




        if (requestPermissions()) {
            getLatLngAddress();
        }
        setTitle("Add a new Activity and Edit it");
        
        
        Spinner spinner = (Spinner) findViewById(R.id.detail_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.location_array,
                android.R.layout.simple_spinner_item
        );
// Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinner.setAdapter(adapter);

    }
    public boolean requestPermissions() {
        int REQUEST_PERMISSION = 3000;
        String permissions[] = {
                android.Manifest.permission.
                        ACCESS_FINE_LOCATION,
                android.Manifest.permission.
                        ACCESS_COARSE_LOCATION};
        boolean grantFinePermission =
                ContextCompat.
                        checkSelfPermission(this, permissions[0]) ==
                        PackageManager.
                                PERMISSION_GRANTED;
        boolean grantCoarsePermission =
                ContextCompat.
                        checkSelfPermission(this, permissions[1]) ==
                        PackageManager.
                                PERMISSION_GRANTED;
        if (!grantFinePermission && !grantCoarsePermission) {
            ActivityCompat.
                    requestPermissions(this, permissions, REQUEST_PERMISSION);
        } else if (!grantFinePermission) {
            ActivityCompat.
                    requestPermissions(this, new String[]{permissions[0]},
                            REQUEST_PERMISSION);
        } else if (!grantCoarsePermission) {
            ActivityCompat.
                    requestPermissions(this, new String[]{permissions[1]},
                            REQUEST_PERMISSION);
        }
        return grantFinePermission && grantCoarsePermission;
    }
    public synchronized void getLatLngAddress() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.
                checkSelfPermission(this,
                        android.Manifest.permission.
                                ACCESS_FINE_LOCATION) !=
                PackageManager.
                        PERMISSION_GRANTED &&
                ActivityCompat.
                        checkSelfPermission(this,
                                android.Manifest.permission.
                                        ACCESS_COARSE_LOCATION) !=
                        PackageManager.
                                PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(EventDetail.this,
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                createLocationRequestLocationCallback();
                                startLocationUpdates();
                            }
                        });
    }
    public void startLocationUpdates() {
        if (ActivityCompat.
                checkSelfPermission(this,
                        android.Manifest.permission.
                                ACCESS_FINE_LOCATION) !=
                PackageManager.
                        PERMISSION_GRANTED &&
                ActivityCompat.
                        checkSelfPermission(this,
                                android.Manifest.permission.
                                        ACCESS_COARSE_LOCATION) !=
                        PackageManager.
                                PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
    public void createLocationRequestLocationCallback() {
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000
        ).build();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLatitude = locationResult.getLastLocation().getLatitude();
                currentLongitude = locationResult.getLastLocation().getLongitude();

            }
        };
    }


    public void saveActivity(View view){
        Intent intent = new Intent(this, EventEdit.class);

        // header
        TextView eventDetailHeader = findViewById(R.id.eventDetailHeader);
        String eventDetailHeader1 = eventDetailHeader.getText().toString();
        intent.putExtra("eventDetailHeader", eventDetailHeader1 );

        //title
        EditText eventDetailTitle = findViewById(R.id.eventDetailTitle);
        String eventDetailTitle1 = eventDetailTitle.getText().toString();
        intent.putExtra("eventDetailTitle", eventDetailTitle1 );
        if (eventDetailTitle1.isEmpty()) {
            return; // Stop further execution
        }

        //summary
        EditText eventDetailSummary = findViewById(R.id.eventDetailSummary);
        String eventDetailSummary1 = eventDetailSummary.getText().toString();
        intent.putExtra("eventDetailSummary", eventDetailSummary1 );
        if (eventDetailSummary1.isEmpty()) {
            return; // Stop further execution
        }

        //time
        EditText eventDetailTime = findViewById(R.id.eventDetailTime);
        String eventDetailTime1 = eventDetailTime.getText().toString();
        intent.putExtra("eventDetailTime", eventDetailTime1 );
        if (eventDetailTime1.isEmpty()) {
            return; // Stop further execution
        }

        //dates
        EditText eventDetailDates = findViewById(R.id.eventDetailDates);
        String eventDetailDates1 = eventDetailDates.getText().toString();
        intent.putExtra("eventDetailDates", eventDetailDates1 );
        if (eventDetailDates1.isEmpty()) {
            return; // Stop further execution
        }

        //contact
        EditText eventDetailContact = findViewById(R.id.eventDetailContact);
        String eventDetailContact1 = eventDetailContact.getText().toString();
        intent.putExtra("eventDetailContact", eventDetailContact1 );
        if (eventDetailContact1.isEmpty()) {
            return; // Stop further execution
        }

        //spinner
        Spinner spinner = findViewById(R.id.detail_spinner);
        String selectedItem = spinner.getSelectedItem().toString();
        intent.putExtra("selectedItem", selectedItem );
        LatLng locationLatLng = getLocationLatLng(selectedItem);
        if (locationLatLng != null) {
            intent.putExtra("latitude", locationLatLng.latitude);
            intent.putExtra("longitude", locationLatLng.longitude);
        }
        if (selectedItem.equals("Select a location")) {
            return; // Stop further execution
        }
        startActivity(intent);
    }
    private LatLng getLocationLatLng(String locationName) {

        switch (locationName) {
            case "Refectory":
                return new LatLng(-35.23849600894474, 149.0844213224768);
            case "Building 6":
                return new LatLng(-35.236240246167554, 149.08397494549197);
            case "UC Hub":
                return new LatLng(-35.23809441627641, 149.0845431173291);
            case "UC Lodge":
                return new LatLng(-35.23816810315772, 149.0827951359983);
            case "Library":
                return new LatLng(-35.237928620561014, 149.08360484608554);
            case "Current Location":
                return new LatLng(currentLatitude, currentLongitude);
            default:
                return null;
        }
    }

}