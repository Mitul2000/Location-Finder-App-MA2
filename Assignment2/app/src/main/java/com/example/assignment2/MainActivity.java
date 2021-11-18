package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    ListView listView;
    ArrayList<String> cityList = new ArrayList<>();
    ArrayList<LocationModel> places = new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = (SearchView) findViewById(R.id.search_bar);
        listView = findViewById(R.id.list_item);
//        get_json();

        DBHelper databaseHelper = new DBHelper(MainActivity.this);
        ArrayList<String> readtable = databaseHelper.getAllCity();


        //modify the adapter for viewing different lists
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                readtable );

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String coordinates = adapterView.getItemAtPosition(i).toString();
                startGeocoding(coordinates);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MainActivity.this.arrayAdapter.getFilter().filter(query);

                Boolean shouldbeentered = databaseHelper.findAddress(query);
                if(shouldbeentered){
                    Boolean newcity = databaseHelper.addCity(query,0+"",0+"");

                }

                startGeocoding(query);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                MainActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });


    }


    public void startGeocoding(String query){
        Intent i = new Intent(MainActivity.this, Geocoding.class);
        i.putExtra("query", query);
        startActivity(i);
    }

    public void get_json(){
        String json;
        try{
            InputStream is = getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i =0; i< jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                if(places.size()<= 50){
                    String lat = obj.get("latitude").toString();
                    String lon = obj.get("longitude").toString();
                    LocationModel locationobj = new LocationModel(obj.get("latitude").toString(),obj.get("longitude").toString());
                    places.add(locationobj);
                    String cityToAdd = reverseGeocoding(lat, lon);
                    cityList.add(cityToAdd);

                    DBHelper databaseHelper = new DBHelper(MainActivity.this);
                    Boolean shouldbeentered = databaseHelper.findrow(lat,lon);
                    if(shouldbeentered){
                        Boolean newcity = databaseHelper.addCity(cityToAdd,lat,lon);
                    }

                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    public String reverseGeocoding(String latittue, String longitude){

        double lat = Double.parseDouble(latittue);
        double lon = Double.parseDouble(longitude);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

//            Toast.makeText(Geocoding.this, "city : "+city, Toast.LENGTH_SHORT).show();
            return city + ", " + state + ", " + country + ", " + postalCode;

        } catch (IOException e) {
            e.printStackTrace();
        }
       return "";
    }


}