package easyconnect.example.com.easyconnect;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class UserLocation {

    private static double latitude=0;
    private static double longitude=0;
    private static Location location=null;

    public static void updateUserLocation(Context context)
    {
        LocationManager locationManager=null;
        boolean gps_enabled=false;
        boolean network_enabled=false;
        boolean passive_enabled=false;


        //I use LocationResult callback class to pass location value from MyLocation to user code.
        if(locationManager==null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try{gps_enabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}
        try{passive_enabled=locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);}catch(Exception ex){}

        //don't start listeners if no provider is enabled
        if(!gps_enabled && !network_enabled && !passive_enabled) {
            Log.i("MyLocation", "Location Not Available. gps_disabled network_disabled passive_disabled");
            return;
        }

        // if one of gps_enabled/network_enabled is null set to false
        if(!gps_enabled) gps_enabled = false;
        if(!network_enabled) network_enabled = false;
        if(!passive_enabled) passive_enabled = false;

        // Now get getLastKnownLocation

        /// GPS is the most acurate, so try it first
        if(gps_enabled) {
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i("MyLocation", "gps_enabled. Get location from GPS");
        }

        /// check if other applications or services has the location
        if(passive_enabled && location==null) {
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            Log.i("MyLocation", "passive_enabled. Get location from PASSIVE mode");
        }

        /// last check for network (not very accurate)
        if(network_enabled && location==null) {
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.i("MyLocation", "network_enabled. Get location from NETWORK provider");
        }

        // if location is null there is no LastKnownLocation available
        if (location==null){
            Log.i("MyLocation", "Last Known Location Not Available");
            return;
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.i("MyLocation", "latitude="+latitude+" "+"longitude="+longitude);
        return;
    }

    public static Double getLatitude(){
        if(location==null){
            return null;
        }
        return latitude;
    }

    public static Double getLongitude(){
        if(location==null){
            return null;
        }
        return longitude;
    }
}
