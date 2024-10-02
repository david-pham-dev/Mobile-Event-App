package com.example.mobiletechassignment;

import androidx.annotation.NonNull;
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

import java.lang.reflect.Array;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng eventLocation;
    private LatLng currentLocation;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    LocationCallback locationCallback;
    private double latitude;
    public double longitude;
    private DatabaseReference mDatabase;
    private String title;
    private String summary;
    private String location;
    private String dates;
    private String contact;
    private String time;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("UC Bruce Map");
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MyDBHelper dbHelper = new MyDBHelper(
                this, "MobileTech", null, 1);
        String eventName = dbHelper.selectAllEvent();
        mDatabase = FirebaseDatabase.getInstance().getReference("O-Week").child(eventName);

        if (requestPermissions()) {
            getLatLngAddress();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        downloadMultipleValuesFromRealtimeDB();

    }

    public void downloadMultipleValuesFromRealtimeDB() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("eventKey")) {
             key = intent.getStringExtra("eventKey");
            mDatabase.child(key);
            // Now you have the key, you can use it to download event details
        }

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                 title = snapshot.child("title").getValue(String.class);
                 location = snapshot.child("location").getValue(String.class);
                 summary = snapshot.child("summary").getValue(String.class);
                 time = snapshot.child("time").getValue(String.class);
                 contact = snapshot.child("contact").getValue(String.class);
                 dates = snapshot.child("dates").getValue(String.class);
                Double latitude = snapshot.child("latLng").child("latitude").getValue(Double.class);
                Double longitude = snapshot.child("latLng").child("longitude").getValue(Double.class);
                if (latitude != null && longitude != null) {
                    eventLocation = new LatLng(latitude, longitude);

                    // Concatenate all relevant information into the marker's snippet
                    String snippet = "Location: " + location + " " +
                            "Summary: " + summary + " " +
                            "Time: " + time + " " +
                            "Contact: " + contact + " " +
                            "Dates: " + dates;

                    Marker eventsMarker = mMap.addMarker(new MarkerOptions()
                            .position(eventLocation)
                            .title(title)
                            .snippet(snippet) // Set the concatenated snippet
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    eventsMarker.setTag("Event");

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = new Intent(MapsActivity.this, EventDetail.class);
        intent.putExtra("currentLatitude",latitude);
        intent.putExtra("currentLongitude",longitude);
        ArrayList<LatLng> uc = new ArrayList<>();
        uc.add(new LatLng(-35.2353691, 149.0914746));
        uc.add(new LatLng(-35.2390062, 149.0901682));
        uc.add(new LatLng(-35.2400065, 149.0899252));
        uc.add(new LatLng(-35.2405206, 149.0898873));
        uc.add(new LatLng(-35.2415457, 149.089981));
        uc.add(new LatLng(-35.2420814, 149.0894008));
        uc.add(new LatLng(-35.2422377, 149.0769777));
        uc.add(new LatLng(-35.2419722, 149.0761428));
        uc.add(new LatLng(-35.2416669, 149.0757536));
        uc.add(new LatLng(-35.2411542, 149.074698));
        uc.add(new LatLng(-35.240974, 149.0750658));
        uc.add(new LatLng(-35.2382806, 149.0771161));
        uc.add(new LatLng(-35.2378117, 149.0774194));
        uc.add(new LatLng(-35.2336506, 149.0793414));
        uc.add(new LatLng(-35.2329108, 149.0798677));
        uc.add(new LatLng(-35.2327255, 149.0799932));
        uc.add(new LatLng(-35.2321494, 149.0803377));
        uc.add(new LatLng(-35.2315465, 149.0806325));
        uc.add(new LatLng(-35.2314683, 149.08069));
        uc.add(new LatLng(-35.2313886, 149.0808109));
        uc.add(new LatLng(-35.2313771, 149.0809849));
        uc.add(new LatLng(-35.231435, 149.0814239));
        uc.add(new LatLng(-35.2315127, 149.0818787));
        uc.add(new LatLng(-35.2316636, 149.082554));
        uc.add(new LatLng(-35.2317299, 149.0828045));
        uc.add(new LatLng(-35.2319656, 149.0835451));
        uc.add(new LatLng(-35.2323638, 149.0845016));
        uc.add(new LatLng(-35.233774, 149.0869296));
        uc.add(new LatLng(-35.2346604, 149.0896269));
        uc.add(new LatLng(-35.2348966, 149.0911233));
        uc.add(new LatLng(-35.2353691, 149.0914746));

        PolygonOptions uchPolygonOptions = new PolygonOptions().geodesic(true);
        for (LatLng latLng : uc) {
            uchPolygonOptions.add(latLng);
        }
        uchPolygonOptions.strokeColor(Color.BLUE);
        uchPolygonOptions.strokeWidth(15.0f);
        mMap.addPolygon(uchPolygonOptions);// Use builder to zoom in the map to the greatest possible zoom level
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : uc) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        int padding = 40;
        mMap.moveCamera(CameraUpdateFactory.
                newLatLngBounds(bounds, padding));

        //pegman
        LatLng thrriwirri_entry = new LatLng(-35.2341, 149.0791);
        LatLng allowoona_entry = new LatLng(-35.2340,
                149.0871);
        LatLng university_entry = new LatLng(-35.2385,
                149.0904);
        LatLng kirinari_entry = new LatLng(-35.2423, 149.0853);



        Marker thrriwirriEntry = mMap.addMarker(new MarkerOptions()
                .position(thrriwirri_entry)

                .icon(BitmapDescriptorFactory.
                        fromResource(R.drawable.ppicture1))
        );
        Marker allowoonaEntry = mMap.addMarker(new MarkerOptions()
                .position(allowoona_entry)

                .icon(BitmapDescriptorFactory.
                        fromResource(R.drawable.ppicture1))
        );
        Marker universityEntry = mMap.addMarker(new MarkerOptions()
                .position(university_entry)

                .icon(BitmapDescriptorFactory.
                        fromResource(R.drawable.ppicture1))
        );
        Marker kirinariEntry = mMap.addMarker(new MarkerOptions()
                .position(kirinari_entry)

                .icon(BitmapDescriptorFactory.
                        fromResource(R.drawable.ppicture1))
        );
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Object tag = marker.getTag();
                if (tag != null && tag.equals("Event")) {
                return false; // Return true to consume the event and prevent default behavior
                }
                if (tag != null && tag.equals("User")) {
                    return false; // Return true to consume the event and prevent default behavior
                }
                // For pegman marker, return false to allow default behavior (info window won't open)
                Intent intent = new Intent(MapsActivity.this, StreetViewActivity.class);
                intent.putExtra("latitude", marker.getPosition().latitude);
                intent.putExtra("longitude", marker.getPosition().longitude);
                startActivity(intent);
                return true;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                Intent intent = new Intent( MapsActivity.this, DisplayDetail.class);
                Object tag = marker.getTag();
                if (tag != null && tag.equals("Event")) {
                    intent.putExtra("eventTitle",title);
                    intent.putExtra("eventSummary",summary);
                    intent.putExtra("eventTime",time);
                    intent.putExtra("eventDates",dates);
                    intent.putExtra("eventContact",contact);
                    intent.putExtra("eventLocation",location);
                    startActivity(intent);
                }

            }
        });

    }

    public boolean requestPermissions() {
        int REQUEST_PERMISSION = 3000;
        String permissions[] = {
                android.Manifest.permission.
                        ACCESS_FINE_LOCATION,
                android.Manifest.permission.
                        ACCESS_COARSE_LOCATION};
        boolean grantFinePermission = ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED;
        boolean grantCoarsePermission = ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED;
        if (!grantFinePermission && !grantCoarsePermission) {
            ActivityCompat.
                    requestPermissions(this, permissions, REQUEST_PERMISSION);
        } else if (!grantFinePermission) {
            ActivityCompat.
                    requestPermissions(this, new String[]{permissions[0]},
                            REQUEST_PERMISSION);
        } else if (!grantCoarsePermission) {
            ActivityCompat.requestPermissions(this, new String[]{permissions[1]},
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
                .addOnSuccessListener(MapsActivity.this,
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
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,
                5000).build();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                latitude = locationResult.getLastLocation().getLatitude();
                longitude = locationResult.getLastLocation().getLongitude();
                currentLocation = new LatLng(latitude, longitude);
                String userSnippet = "Location: " + " " +
                        "Longitude: " + longitude + " " +
                        "Latitude: " + latitude;
                Marker userLocation = mMap.addMarker(new MarkerOptions()
                        .position(currentLocation)
                                .title("YOU ARE HERE")
                                .snippet(userSnippet)
                        .icon(BitmapDescriptorFactory.
                                fromResource(R.drawable.ppicture2))
                );
                userLocation.setTag("User");
                if (eventLocation != null && currentLocation != null) {
                    String apiKey = getMapApiKey(MapsActivity.this);
                    drawRoute(apiKey, currentLocation, eventLocation);

                }
            }
        };
    }
    public void drawRoute(String apiKey, LatLng origin, LatLng destination) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude + "&destination="
                + destination.latitude + "," + destination.longitude
                + "&mode=walking&key=" + apiKey;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.
                        GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
// Parse the JSON response and draw the route on the map
                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.
                                RED);
                        polylineOptions.width(14);
                        JSONArray routes = null;
                        try {
                            routes = response.getJSONArray("routes");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        for (int i = 0; i < routes.length(); i++) {
                            try {
                                JSONObject route = routes.getJSONObject(i);
                                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                                String points = overviewPolyline.getString("points");
                                List<LatLng> path = PolyUtil.
                                        decode(points);
                                polylineOptions.addAll(path);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        mMap.addPolyline(polylineOptions);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
// Handle the error
                    }
                });
        RequestQueue requestQueue = Volley.
                newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
    public String getMapApiKey(Context context) {
        String apiKey = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.
                            GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            if (bundle != null) {
                apiKey = bundle.getString("com.google.android.geo.API_KEY");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return apiKey;
    }


}