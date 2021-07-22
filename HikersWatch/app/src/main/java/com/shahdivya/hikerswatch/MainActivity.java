package com.shahdivya.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    TextView latitude;
    TextView longitude;
    TextView altitude;
    TextView Address;
    TextView Accuracy;
    String addres = "";
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
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        altitude = findViewById(R.id.Altitude);
        Address = findViewById(R.id.addd);
        Accuracy = findViewById(R.id.accuracy);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("percy",location.toString());

                    Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                List<Address> addressList = null;
                try {
                    addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList != null && addressList.size()>0)
                {
                    if (addressList.get(0).getThoroughfare()!=null)
                    {
                        addres += addressList.get(0).getThoroughfare()+"\n";
                    }
                    if (addressList.get(0).getSubAdminArea() != null)
                    {
                        addres += addressList.get(0).getSubAdminArea() +"\n";
                    }
                    if (addressList.get(0).getAdminArea() != null)
                    {
                        addres += addressList.get(0).getAdminArea() + "\n";
                    }
                    if (addressList.get(0).getPostalCode() != null)
                    {
                        addres += addressList.get(0).getPostalCode();
                    }
                }
                        Log.i("percy",addressList.get(0).toString());
                Address.setText(addres);
                updateInfo(location);
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
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        }
    }
    @SuppressLint("SetTextI18n")
    public void updateInfo(Location location)
    {
        latitude.setText("Latitude "+Double.toString(location.getLatitude()));
        longitude.setText("Longitude "+Double.toString(location.getLongitude()));
        Accuracy.setText("Accuracy "+Float.toString(location.getAccuracy()));
        altitude.setText("Altitude "+Double.toString(location.getAltitude()));
    }
}
