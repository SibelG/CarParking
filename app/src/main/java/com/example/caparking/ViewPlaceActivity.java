package com.example.caparking;

import static com.example.caparking.util.LogUtil.logD;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caparking.Common.Common;
import com.example.caparking.Helper.DBHelper;
import com.example.caparking.Model.PlaceDetail;
import com.example.caparking.Remote.IGoogleAPIService;
import com.example.caparking.databinding.ActivityUpdateParkingBinding;
import com.example.caparking.databinding.ActivityViewPlaceBinding;
import com.example.caparking.util.SessionManager;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPlaceActivity extends AppCompatActivity {


    SessionManager manager;
    IGoogleAPIService mService;
    DBHelper DB;
    PlaceDetail mPlace;
    private ActivityViewPlaceBinding binding;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DB = new DBHelper(this);


        mService = Common.getGoogleAPIService();

        binding.placesName.setText("");
        binding.placesAddress.setText("");
        binding.placesOpenHour.setText("");
        manager=new SessionManager(getApplicationContext());

        binding.btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getUrl()));
                startActivity(mapIntent);
            }
        });

        if (Common.currentResults.getPhotos() != null && Common.currentResults.getPhotos().length > 0)  {

            Picasso.with(this)
                    .load(Common.getPhotoOfPlaces(Common.currentResults.getPhotos()[0].getPhoto_reference(),1000))
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(binding.photo);
        }

        if (Common.currentResults.getRating() != null && !TextUtils.isEmpty(Common.currentResults.getRating()))  {
            binding.ratingBar.setRating(Float.parseFloat(Common.currentResults.getRating()));
        }
        else    {
            binding.ratingBar.setVisibility(View.GONE);
        }

        if (Common.currentResults.getOpening_hours() != null)  {
            binding.placesOpenHour.setText("Open Now : "+Common.currentResults.getOpening_hours().getOpen_now());
        }
        else    {
            binding.placesOpenHour.setVisibility(View.GONE);
        }

        mService.getDetailPlaces(Common.getPlaceDetailUrl(Common.currentResults.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {

                        mPlace = response.body();

                        binding.placesAddress.setText(mPlace.getResult().getFormatted_address());
                        binding.placesName.setText(mPlace.getResult().getName());
                        binding.pAddress.setText(mPlace.getResult().getFormatted_address());
                        binding.pName.setText(mPlace.getResult().getName());

                        Cursor cursor = DB.viewParkingAreas(String.valueOf(DB.GetLocId(binding.placesName.getText().toString())));

                        if (cursor != null && cursor.getCount() == 1) {
                            cursor.moveToFirst();
                            price = String.valueOf(cursor.getDouble(2));

                            binding.perPrice.setText(String.valueOf(cursor.getDouble(2)));
                        }

                        else{
                            Log.d("error","error");
                        }
                        System.out.println("price"+price);
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {

                    }
                });
    }





    public void parkingSlot(View view) {
        int id = DB.GetLocId(binding.placesName.getText().toString());
        manager.createParkingSession(id);
        Intent intent = new Intent(getApplicationContext(), SeatSelection.class);
        intent.putExtra("price",price);
        startActivity(intent);
    }
}
