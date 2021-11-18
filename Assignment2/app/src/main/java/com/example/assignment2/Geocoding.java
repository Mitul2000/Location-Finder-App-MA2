package com.example.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Console;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Geocoding extends AppCompatActivity {

    TextView outputmessage;
    String address;
    EditText updateaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoding);

        outputmessage = (TextView) findViewById(R.id.tv_outputmessage);
        updateaddress = (EditText)findViewById(R.id.etv_updateaddress);

        Intent intent = getIntent();
        String result = getIntent().getStringExtra("query");
        address = result;

        GeoLocation locationAddress = new GeoLocation();
        locationAddress.getAddressFromLocation(result,
                getApplicationContext(), new GeocoderHandler());

    }
    public void deleteEntry(View view) {
        DBHelper databaseHelper = new DBHelper(Geocoding.this);
        databaseHelper.deleteRow(address);
        Intent intent = new Intent(Geocoding.this, MainActivity.class);
        startActivity(intent);
    }

    public void manualupdate(View view) {
        String newaddress = updateaddress.getText().toString();
        DBHelper databaseHelper = new DBHelper(Geocoding.this);
        databaseHelper.updateAddress(address, newaddress);
        Intent intent = new Intent(Geocoding.this, MainActivity.class);
        startActivity(intent);

    }

    public void mainpage(View view) {
        Intent intent = new Intent(Geocoding.this, MainActivity.class);
        startActivity(intent);
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            outputmessage.setText(locationAddress);
        }
    }


}