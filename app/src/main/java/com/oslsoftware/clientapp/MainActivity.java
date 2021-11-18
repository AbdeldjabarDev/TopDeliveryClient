package com.oslsoftware.clientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {
FirebaseAuth mauth;
    Retrofit retrofit;
    okhttp3.OkHttpClient client;
    //Moshi moshi;
    okhttp3.OkHttpClient.Builder builder;
    public interface ClientApi
    {
        @POST("/Register")
        Call<callStatus> Register(@Body Userinfo info);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mauth.getCurrentUser();
        if(currentUser != null)
        {

            File file = new File(getCacheDir().getAbsolutePath() + "/orderFile.of");
            if(!file.exists())
            {

                Intent intent = new Intent(this, content.class);
                startActivity(intent);

            }
            else
            {
                try {
                    FileReader fileReader =  new FileReader(file);
                    char[] buf = new char[(int) file.length()];
                    fileReader.read(buf);
                    String s = new String(buf);
                    String arr[] = s.split(":");
                    String orderId = arr[0];
                    String UserLocation = arr[1];
                    String restaurant = arr[2];
                    Intent intent = new Intent(this,OrderDelivery.class);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("UserLocation",UserLocation);
                    intent.putExtra("restaurant",restaurant);
                    startActivity(intent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        else
        {
            builder = new OkHttpClient.Builder();
            builder.readTimeout(14, TimeUnit.HOURS);
            builder.connectTimeout(10,TimeUnit.SECONDS);
            client = builder.build();
            //moshi = Moshi.Builder().build();
            retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.247:10000").addConverterFactory(GsonConverterFactory.create()).client(client).build();
            Login login = new Login();
            login.setRetrofit(retrofit);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main,login).commit();
        }



    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}