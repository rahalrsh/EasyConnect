package easyconnect.example.com.easyconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        // Default center point and zoom
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.6617, -79.3951), 14.0f));

        // set markers
        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.7273, -79.2768))
                .title("Home"));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.6617, -79.3951))
                .title("UofT"));
    }

    public void setUpMap(){
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

}
