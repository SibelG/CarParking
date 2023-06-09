package com.example.caparking.Remote;

import com.example.caparking.Model.ModelResultNearby;
import com.example.caparking.Model.MyPlaces;
import com.example.caparking.Model.PlaceDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IGoogleAPIService {
    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);

    @GET
    Call<PlaceDetail> getDetailPlaces(@Url String url);

    @GET("maps/api/directions/json")
    Call<String> getDirection(@Query("origin") String origin, @Query("destination") String destination);
}
