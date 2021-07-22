package com.shahdivya.memorableplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //SharedPreferences sharedPreferences;
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1)
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try
                {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    String name = "";
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if (addressList!=null && addressList.size()>0)
                    {
                        if (addressList.get(0).getLocality()!=null)
                        {
                            name = name + addressList.get(0).getLocality()+",";
                        }
                        if (addressList.get(0).getThoroughfare()!=null)
                        {
                            name = name + addressList.get(0).getThoroughfare();
                        }
                    }
                    LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(myPos).title(name));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos,10));
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                try{
                    String name ="";
                    Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    if (addressList!=null && addressList.size()>0)
                    {
                        if (addressList.get(0).getThoroughfare() != null)
                        {
                            if (addressList.get(0).getSubThoroughfare() != null)
                            {
                                name = name + addressList.get(0).getSubThoroughfare()+",";
                            }
                            name = name + addressList.get(0).getThoroughfare();
                        }
                        else {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                            Date date = new Date();
                            name = sdf.format(date);
                        }
                    }
                    locate(name,latLng);
                    MainActivity.memorables.add(name);
                    MainActivity.latLngs.add(latLng);
                    MainActivity.arrayAdapter.notifyDataSetChanged();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.shahdivya.memorableplaces",Context.MODE_PRIVATE);
        try {
            ArrayList<String> latitudes = new ArrayList<>();
            ArrayList<String> longitudes = new ArrayList<>();

            for (LatLng cord : MainActivity.latLngs)
            {
                latitudes.add(Double.toString(cord.latitude));
                longitudes.add(Double.toString(cord.longitude));
            }
            sharedPreferences.edit().putString("memorable",ObjectSerializer.serialize(MainActivity.memorables)).apply();
            sharedPreferences.edit().putString("latitudes",ObjectSerializer.serialize(latitudes)).apply();
            sharedPreferences.edit().putString("longitudes",ObjectSerializer.serialize(longitudes)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (MainActivity.n == 1)
        {
            MainActivity.n = 0;
            memorable();
        }
    }
    public void locate(String title1, LatLng position1)
    {
        mMap.addMarker(new MarkerOptions().position(position1).title(title1));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position1,10));
    }
    public void memorable()
    {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        int pos = intent.getIntExtra("position",0);
        LatLng position1 = MainActivity.latLngs.get(pos);
        locate(title,position1);
    }
}
