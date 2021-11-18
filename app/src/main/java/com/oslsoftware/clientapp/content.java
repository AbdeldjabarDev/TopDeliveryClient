package com.oslsoftware.clientapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class content extends AppCompatActivity {
    FoodList foodList;
    ProgressDialog progressDialog1;
    ArrayList<orderItem> Orders;
    public final String myTAG = "ClientContentAct";
    MyAccountFragment myAccountFragment;
    DiscoveryFragment discoveryFragment;
    MyCart fragmentMyCart;
    FragmentTransaction fragmentTransaction;
    FoodItemAdapter adapter;
    FragmentManager fragmentManager;
    LocationManager mLocationManager;
    ArrayList<Restaurant> restaurantArrayList;
    FirebaseFirestore firestore;
    List<String> FoodCatergoriesArrayList;
    Map</*category*/String,ArrayList<newFoodItem>> foodItemlist;
    Map</*restaurant*/String,ArrayList<newFoodItem>> restFoodList;
    Retrofit retrofit;
    okhttp3.OkHttpClient client;
    AtomicReference<Integer> depth;
    //Moshi moshi;
    okhttp3.OkHttpClient.Builder builder;
    public void fetchRestaurants()
    {
        firestore.collection("/Restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Restaurant res = documentSnapshot.toObject(Restaurant.class);
                        restaurantArrayList.add(res);
                        documentSnapshot.getReference().addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error != null)
                                {
                                    Log.d("contentActivity","Error on event restaurant");

                                }
                                Restaurant  newRes = value.toObject(Restaurant.class);
                                restaurantArrayList.set(restaurantArrayList.indexOf(res),newRes);

                            }
                        });
                    }
                    fetchFoodItems();
                } else
                {
                    Toast.makeText(getApplicationContext(), "Failed to fetch Restaurant list: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    Log.d(myTAG,"Failed to fetch Restaurant list: " + task.getException().toString());
                }
            }
        });
    }
    public void  fetchFoodItems()
    {
        if(!restaurantArrayList.isEmpty())
        {
            depth.getAndSet(restaurantArrayList.size() * 5);
            for (Restaurant res : restaurantArrayList) //loop over all restaurants
            {
                // DocumentReference resColl = firestore.collection("Restaurants").document(res.name);
                fetchNext(res);
            }
        }
        else
        {
            Toast.makeText(this, "Empty restuarants array list", Toast.LENGTH_SHORT).show();
            Log.d(myTAG,"Empty restuarants array list");
        }
    }
    public void fetchNext(Restaurant resname)
    {
        DocumentReference restRef = firestore.collection("Restaurants").document(resname.name);
        for(int index = 0 ;index < FoodCatergoriesArrayList.size();index++)
        {
            ArrayList<newFoodItem> buffer = new ArrayList<>();
            Task<QuerySnapshot> task =  restRef.collection(FoodCatergoriesArrayList.get(index)).get();
            int finalIndex = index;
            final int[] i = {1};
            task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                    {

                        newFoodItem myNewFoodItem = new newFoodItem(doc.toObject(FoodItem.class),resname);
                        buffer.add(myNewFoodItem);

                        if(doc.getMetadata().isFromCache() && i[0] != 0)
                        {
                            AlertDialog dialog = new AlertDialog.Builder(content.this).create();
                            dialog.setMessage(getString(R.string.items_not_synced));
                            dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            i[0] = 0;
                        }
                        doc.getReference().addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error != null)
                                {
                                    Log.d("contentActivity","Error on event fooditem changed");
                                }
                                   newFoodItem foodItem = new newFoodItem(value.toObject(FoodItem.class),resname);
                                   ArrayList<newFoodItem> NewArrayList = foodItemlist.get(FoodCatergoriesArrayList.get(finalIndex));
                                   NewArrayList.set(NewArrayList.indexOf(myNewFoodItem),foodItem);
                            }
                        });


                        progressDialog1.dismiss();
                    }
                    ArrayList<newFoodItem> buffer2 = restFoodList.get(resname.name);//get what we previously put in resname slot;
                    if(buffer2 == null)
                    buffer2 = new ArrayList<>();
                    buffer2.addAll(buffer);//add what we currently get to what we previously put
                    restFoodList.put(resname.name,buffer2);//put result
                    foodItemlist.put(FoodCatergoriesArrayList.get(finalIndex),buffer);
                    depth.getAndSet(depth.get() - 1);
                    if(depth.get() == 0)
                    {
                        progressDialog1.dismiss();
                        Toast.makeText(content.this, "Food items fetched successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);


        getSupportActionBar().hide();
        progressDialog1  = new ProgressDialog(this);
        depth = new AtomicReference<>(0);
        firestore = FirebaseFirestore.getInstance();
        foodItemlist = new HashMap<>();
        restFoodList = new HashMap<>();
        FoodCatergoriesArrayList = Arrays.asList("pizzas", "sandwiches", "plats", "drinks", "burgers");
        restaurantArrayList = new ArrayList<>();
        foodList = new FoodList();
        myAccountFragment = new MyAccountFragment();
        fragmentMyCart = new MyCart();
        discoveryFragment = new DiscoveryFragment();
        adapter = new FoodItemAdapter(this);
        fetchRestaurants();
        Orders = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        builder = new OkHttpClient.Builder();
        builder.readTimeout(14, TimeUnit.HOURS);
        builder.connectTimeout(10,TimeUnit.SECONDS);
        client = builder.build();
        //moshi = Moshi.Builder().build();
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.247:10000").addConverterFactory(GsonConverterFactory.create()).client(client).build();
        fragmentMyCart.setRetrofit(retrofit);

        BottomNavigationView bottomNavView = findViewById(R.id.bottom_navView);
        bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.Home :
                       fragmentManager.beginTransaction().replace(R.id.content_container,foodList)
                               .setCustomAnimations(R.anim.slide_in,R.anim.slide_out).commit();
                        return true;
                    case R.id.Favourites:
                      fragmentManager.beginTransaction().replace(R.id.content_container,discoveryFragment)
                        .setCustomAnimations(R.anim.slide_in,R.anim.slide_out).commit();
                      discoveryFragment.setOrders(Orders);
                        return true;
                    case R.id.MyCart:
                        fragmentManager.beginTransaction().replace(R.id.content_container,fragmentMyCart)
                                .setCustomAnimations(R.anim.slide_in,R.anim.slide_out).commit();
                        fragmentMyCart.setOrderItems(Orders);
                        return true;
                    case R.id.MyAccount:
                        fragmentManager.beginTransaction().replace(R.id.content_container,myAccountFragment)
                                .setCustomAnimations(R.anim.slide_in,R.anim.slide_out).commit();
                        return true;
                }
                return false;
            }
        });
        progressDialog1.setTitle("Fetching food items");
        progressDialog1.setCancelable(false);
        progressDialog1.setMessage("loading ...");
        progressDialog1.show();
    }





    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<newFoodItem> dummy = new ArrayList<>();
        adapter.setOrders(Orders);
        adapter.setFoodItems(dummy);
        foodList.setFoodItemlist(foodItemlist);
        foodList.setAdapter(adapter);
        foodList.setButtonsVisible(true);
        foodList.setRestFoodList(restFoodList);
        foodList.setRestaurantArrayList(restaurantArrayList);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in,R.anim.fade_out,R.anim.fade_in,R.anim.slide_out);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.content_container,foodList).commit();
        /*Bundle restaurants = new Bundle();
        restaurants.putParcelableArrayList("restaurants",restaurantArrayList);
        fragmentManager.setFragmentResult("restaurants",restaurants);*/
        //fragmentMyCart.setOrderItems(adapter.getOrders());
        discoveryFragment.setRestFoodAL(restFoodList);
        discoveryFragment.setRestaurantArrayList(restaurantArrayList);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
              && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},100);
        }
      else
      {
if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
{

    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(getString(R.string.gps_disabled))
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, final int id) {

                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),20);
                    dialog.dismiss();
                }
            });

    final AlertDialog alert = builder.create();
    alert.show();
}

      }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}