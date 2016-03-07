package easyconnect.example.com.easyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    TextView TotalSharesCount;
    TextView TotalValidLocationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        TotalSharesCount = (TextView) findViewById(R.id.total_shares_count);
        TotalValidLocationCount = (TextView) findViewById(R.id.total_locations_count);

        Intent intent = getIntent();
        String objectID = intent.getStringExtra("Object_ID");
        Log.i("MapActivity", "Parse Object ID = " + objectID);

        // Make parse object
        final ParseObject locationDB = new ParseObject("tap_location");

        // Retrieving Objects from Parse
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("tap_location");
        query.whereEqualTo("AdId", objectID);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (googleMap == null){
                    Toast.makeText(MapActivity.this, "something went wrong with Google Maps",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (e == null) {
                    // No errors; We can read data from the parse object
                    int totalValidLocations = 0; // ignore the null information. i.e user location was now available
                    int length = objects.size();
                    for (int i = 0; i < length; i++) {

                        String latitude = objects.get(i).getString("latitude");
                        String longitude = objects.get(i).getString("longitude");

                        // If latitude or longitude is null or contains the string "null",
                        // continue to the next iteration
                        // Cannot show null values on the map
                        if (latitude.isEmpty()|| latitude==null || latitude.equals("null")||
                                longitude.isEmpty() || longitude==null || longitude.equals("null"))
                            continue;

                        totalValidLocations++;

                        double latitude_double = Double.parseDouble(latitude);
                        double longitude_double = Double.parseDouble(longitude);

                        // Center the map to the first location available
                        if (i == 0) {
                            // Default center point and zoom
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_double, longitude_double), 13.5f));
                        }

                        // set markers on google map
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude_double, longitude_double)));

                    }

                    // Set the total number an ad was shared
                    // This number is equal to the number of rows we query from parse db
                    TotalSharesCount.setText(""+length);

                    // Set the total number of valid locations available
                    TotalValidLocationCount.setText(""+totalValidLocations);

                } else {
                    // something went wrong
                    Log.i("MapActivity", "error");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setUpMap();
    }

    public void setUpMap(){
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

}
