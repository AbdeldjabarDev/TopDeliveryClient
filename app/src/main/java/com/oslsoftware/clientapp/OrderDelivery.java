package com.oslsoftware.clientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.grpc.okhttp.OkHttpChannelBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderDelivery extends AppCompatActivity {
    String UserLocation;
Retrofit retrofit;
    okhttp3.OkHttpClient client;
    okhttp3.OkHttpClient.Builder builder;
MapsFragment fragment;
OrderStateFragment OSFragment;
FragmentManager mFragmentManager;
    String myOrderId;
String restaurant;
Call<Integer> getOrderStateCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO : do net let user return to content Activity when order is dispatched
        setContentView(R.layout.activity_order_delivery);
        /*String[] LatLngArr = UserLocation.split(",");
                            Bundle args = new Bundle();
                            args.putDouble("Lat",Double.valueOf(LatLngArr[0]));
                            args.putDouble("Lng",Double.valueOf(LatLngArr[1]));
                            args.putString("orderId",myOrderId);
                            fragment.setArguments(args);
                            mFragmentManager.beginTransaction().replace(R.id.layout_delivery,fragment);*/

         myOrderId = getIntent().getStringExtra("orderId");
        UserLocation = getIntent().getStringExtra("UserLocation");
        restaurant = getIntent().getStringExtra("restaurant");
        builder = new OkHttpClient.Builder();
        builder.readTimeout(14, TimeUnit.HOURS);
        builder.connectTimeout(10,TimeUnit.SECONDS);
        client = builder.build();
        //moshi = Moshi.Builder().build();
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.247:10000").addConverterFactory(GsonConverterFactory.create()).client(client).build();
        OSFragment = new OrderStateFragment();
        mFragmentManager = getSupportFragmentManager();
        MyCart.DeliverServiceAPI deliveryServiceApi = retrofit.create(MyCart.DeliverServiceAPI.class);
        getOrderStateCall = deliveryServiceApi.getOrderState(restaurant,myOrderId);
        Callback<Integer> getOrderStateCallback = new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                switch (response.body())
                {
                    case 1/*COOKED*/:
                        OSFragment.textView.setText("Your order has been cooked waiting for deliverer ...");
                        call.clone().enqueue(this);
                        break;
                    case 2/*IN_DELIVERY*/:
                          fragment = new MapsFragment();
                          Bundle args = new Bundle();
                          args.putString("UserLocation",UserLocation);



                    break;
                    case 3/*DELIVERED*/:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                        //TODO : notify user when order has been delivered

                        break;
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        };
        }


        //getSupportFragmentManager().beginTransaction().add(getFragmentManager().findFragmentById(R.id.map),"shit")

    @Override
    public void onStart() {
        super.onStart();
        File file = new File(getCacheDir().getAbsolutePath() + "/order.of");
        try {
            if(file.exists())
                file.delete();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(myOrderId != null && UserLocation!=null && restaurant != null)
        {
            try {

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(myOrderId + ":" + UserLocation + ":" + restaurant); // user location and orderId should not be null !!
                fileWriter.flush();
                fileWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mFragmentManager.beginTransaction().replace(R.id.layout_delivery,OSFragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finishAndRemoveTask();
    }
}