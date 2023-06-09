package com.example.caparking.Common;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.caparking.Model.MyPlaces;
import com.example.caparking.Model.Results;
import com.example.caparking.Remote.IGoogleAPIService;
import com.example.caparking.Remote.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class Common {
    private static double latitude;
    private static double longitude;
    private static Location mLastLocation;
    private static Marker mMarker;
    private static LocationRequest mLocationRequest;
    IGoogleAPIService mService;
    private static final int MY_PERMISSION_CODE = 1000;
    private static GoogleMap mMap;
    MyPlaces currentPlaces;

    FusedLocationProviderClient fusedLocationProviderClient;
    static LocationCallback locationCallback;
    public static Results currentResults;

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";

    public static IGoogleAPIService getGoogleAPIService()   {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }


    public static String getUrl(double latitude, double longitude, String placeType) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword=parking");
        googlePlacesUrl.append("&location="+latitude+","+longitude);
        googlePlacesUrl.append("&radius="+10000);
        googlePlacesUrl.append("&type="+placeType);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=AIzaSyAgjvl_cLGKpKOJ85WC3BpzVT_dv_tLOwI");

        Log.d("getUrl",googlePlacesUrl.toString());

        return googlePlacesUrl.toString();
    }
    public static String getPlaceDetailUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid="+place_id);
        url.append("&key=AIzaSyAgjvl_cLGKpKOJ85WC3BpzVT_dv_tLOwI");

        return url.toString();
    }

    public static String getPhotoOfPlaces(String photo_reference, int maxWidth) {

        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth="+maxWidth);
        url.append("&photoreference="+photo_reference);
        url.append("&key=AIzaSyAgjvl_cLGKpKOJ85WC3BpzVT_dv_tLOwI");

        return url.toString();
    }


    public static boolean checkLocationPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions((Activity) context,new String[]{

                        android.Manifest.permission.ACCESS_FINE_LOCATION

                },MY_PERMISSION_CODE);
            }
            else    {
                ActivityCompat.requestPermissions((Activity) context,new String[]{

                        Manifest.permission.ACCESS_FINE_LOCATION

                },MY_PERMISSION_CODE);
            }
            return false;
        }
        else {
            return true;
        }
    }


}
