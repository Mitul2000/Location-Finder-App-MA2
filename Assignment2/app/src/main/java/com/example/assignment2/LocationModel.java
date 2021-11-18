package com.example.assignment2;

public class LocationModel {
    String Address;
    String Latitude;
    String Longitude;


    public LocationModel(String latitude, String longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getAddress() {
        return Address;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    @Override
    public String toString() {
        return "Lat= " + Latitude + ", Long= " + Longitude;
    }
}
