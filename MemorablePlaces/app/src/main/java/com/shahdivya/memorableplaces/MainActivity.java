package com.shahdivya.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView places;
    static ArrayList<String> memorables;
    static ArrayList<LatLng> latLngs ;
    static ArrayAdapter<String> arrayAdapter;
    static int n = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.shahdivya.memorableplaces", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        places = findViewById(R.id.places);
        memorables = new ArrayList<String>();
        latLngs = new ArrayList<LatLng>();
        memorables.clear();
        latLngs.clear();
        places.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("I was caled","i ewas");
                memorables.remove(position);
                latLngs.remove(position);
                return false;
            }
        });
        places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                n = 1;
                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("title",memorables.get(position));
                startActivity(intent);
            }
        });
        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();
        latitudes.clear();
        longitudes.clear();
        try {
            memorables = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("memorable",ObjectSerializer.serialize(new ArrayList<>())));
            latitudes =  (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("latitudes",ObjectSerializer.serialize(new ArrayList<>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longitudes",ObjectSerializer.serialize(new ArrayList<>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("latitudes",Integer.toString(latitudes.size()));
        Log.i("longitudes",Integer.toString(longitudes.size()));
        Log.i("places",Integer.toString(memorables.size()));
        if (memorables.size()==latitudes.size()&&latitudes.size()==longitudes.size())
        {
            for (int i =0;i<latitudes.size();i++)
            {
                latLngs.add(new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i))));
            }
        }
        arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,memorables);
        places.setAdapter(arrayAdapter);
    }
    public void addPlace(View view)
    {
        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
        startActivity(intent);
    }
}
