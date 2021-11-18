package com.oslsoftware.clientapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MapsFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
LatLng userPos;
    Retrofit retrofit;
    Marker DelivererPos;
    okhttp3.OkHttpClient client;
    //Moshi moshi;
    okhttp3.OkHttpClient.Builder builder;
    public interface DeliveryServiceApi
    {
        @GET("get-deliverer/{orderId}")
        Call<String> getDelivererLocation(@Path("orderId") String orderId);
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng sydney = new LatLng(-34, 151);
            Callback<String> mCallback = new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(DelivererPos != null)
                        DelivererPos.remove();
                String location = response.body();
                String[] LatLngArr = location.split(",");
                LatLng delivererPos = new LatLng(Double.valueOf(LatLngArr[0]),Double.valueOf(LatLngArr[1]));
                DelivererPos = googleMap.addMarker(new MarkerOptions().position(delivererPos));
                    try {
                        Thread.sleep(30000);
                        call.clone().enqueue(this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            };

                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            Bundle res = getArguments();
            if(res != null)
            {
                userPos = new LatLng(res.getDouble("Lat"),res.getDouble("Lng"));
                String orderId = res.getString("orderId");
                DeliveryServiceApi mDeliveryServiceApi= retrofit.create(DeliveryServiceApi.class);
                Call<String> getLocation = mDeliveryServiceApi.getDelivererLocation(orderId);
                getLocation.enqueue(mCallback);
                //googleMap.addMarker(new MarkerOptions().position(sydney));
                Toast.makeText(getContext(), "Lat : " + userPos.latitude + "Lng: " + userPos.longitude, Toast.LENGTH_SHORT).show();
                MarkerOptions options = new MarkerOptions().position(userPos);
                Marker user = googleMap.addMarker(options);

            }

            else
            {
                Toast.makeText(getActivity(), "Arguments is null ! ", Toast.LENGTH_SHORT).show();

            }

            }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new OkHttpClient.Builder();
        builder.readTimeout(14, TimeUnit.HOURS);
        builder.connectTimeout(10,TimeUnit.SECONDS);
        client = builder.build();
        //moshi = Moshi.Builder().build();
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.247:10000").addConverterFactory(GsonConverterFactory.create()).client(client).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_maps, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}