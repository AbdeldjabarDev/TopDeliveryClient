package com.oslsoftware.clientapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.Clock$$CC;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class FoodList extends Fragment {

    RecyclerView FoodRecView;
    Button pizzas,sandwiches,plats,drinks,burgers;
ProgressDialog progressDialog1,progressDialog2;
    List<String> FoodCatergoriesArrayList;
    boolean ButtonsVisible;
    //Map<String,ArrayList<FoodItem>> allItems;
    FoodItemAdapter adapter;
View lastSelected;
    enum SelectionOption
    {
        PIZZAS,SANDWICHES,PLATS,BURGER,DRINKS,ICECREAM
    }
    SelectionOption option = SelectionOption.PIZZAS;
    Map</*category*/String,ArrayList<newFoodItem>> foodItemlist;
    Map</*restaurant*/String,ArrayList<newFoodItem>> restFoodList;
    ArrayList<Restaurant> restaurantArrayList;

    public FoodList()  {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog1 = new ProgressDialog(getActivity());
        progressDialog2 = new ProgressDialog(getActivity());
        progressDialog1.setTitle("Fetching food items");
        progressDialog1.setCancelable(false);
        progressDialog1.setMessage("loading ...");
        progressDialog2.setTitle("Fetching restaurants");
        progressDialog2.setMessage("loading");
        progressDialog2.setCancelable(false);
        /*FoodCatergoriesArrayList = new ArrayList<>();
        adapter = new FoodItemAdapter(getActivity());
        restaurantArrayList = new ArrayList<>();
        foodItemlist = Collections.synchronizedMap(new HashMap<>());
        restFoodList = Collections.synchronizedMap(new HashMap<>());*/
    }


    public void setRestFoodList(Map<String, ArrayList<newFoodItem>> restFoodList) {
        this.restFoodList = restFoodList;
    }

    public void setRestaurantArrayList(ArrayList<Restaurant> list)
    {
        restaurantArrayList = list;
    }
public void setAdapter(FoodItemAdapter adapter)
{
    this.adapter = adapter;
}
public void setButtonsVisible(boolean visible)
{
    ButtonsVisible = visible;

}
private void setButtonsVisibility(boolean v)
{
    if(!v)
    {
        pizzas.setVisibility(View.GONE);
        sandwiches.setVisibility(View.GONE);
        burgers.setVisibility(View.GONE);
        plats.setVisibility(View.GONE);
        drinks.setVisibility(View.GONE);
    }
    else
    {
        return;
    }
}
    public void setFoodItemlist(Map<String, ArrayList<newFoodItem>> foodItemlist) {
        this.foodItemlist = foodItemlist;
    }

    /*Moshi moshi = new Moshi.Builder().build();
    Type type1 = Types.newParameterizedType(List.class,Restaurant.class);*/
   /* Type type2 = Types.newParameterizedType(List.class,FoodItem.class);
    Type type3 = Types.newParameterizedType(List.class,String.class);
    Type type4 = Types.newParameterizedType(List.class,FoodCategory.class);
    JsonAdapter<List<FoodCategory>> FoodCategoriesAdapter = moshi.adapter(type4);
JsonAdapter<List<Restaurant>> restaurantJsonAdapter = moshi.adapter(type1);
JsonAdapter<List<FoodItem>> foodItemJsonAdapter = moshi.adapter(type2);
JsonAdapter<List<String>> stringJsonAdapter = moshi.adapter(type3);*/
   /* JsonAdapter<Restaurant> restaurantJsonAdapter = moshi.adapter(Restaurant.class);
JsonAdapter<List<Restaurant>> restaurantListAdapter = moshi.adapter(type1);*/

    @Override
    public void onStart() {
        super.onStart();
        FoodRecView.setAdapter(adapter);
        FoodRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        FoodCatergoriesArrayList = Arrays.asList("pizzas", "sandwiches", "plats", "drinks", "burgers");
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pizzas.setOnClickListener(myOnclickListener);
        burgers.setOnClickListener(myOnclickListener);
        drinks.setOnClickListener(myOnclickListener);
        plats.setOnClickListener(myOnclickListener);
        sandwiches.setOnClickListener(myOnclickListener);
        setButtonsVisibility(ButtonsVisible);
    }

    public View.OnClickListener myOnclickListener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          switch (v.getId())
          {
              case R.id.bt_pizzas:
                  if(lastSelected != null)
                      lastSelected.setBackgroundColor(Color.parseColor("#f1f3f4"));
                  option = SelectionOption.PIZZAS;
                  adapter.setFoodItems(foodItemlist.get("pizzas"));
                  adapter.notifyDataSetChanged();
                  v.setBackgroundColor(Color.parseColor("#fce883"));
                  lastSelected = v;
                  break;
              case R.id.bt_burgers:
                  if(lastSelected != null)
                      lastSelected.setBackgroundColor(Color.parseColor("#f1f3f4"));
                  option = SelectionOption.BURGER;
                  adapter.setFoodItems(foodItemlist.get("burgers"));
                  adapter.notifyDataSetChanged();
                  v.setBackgroundColor(Color.parseColor("#fce883"));
                  lastSelected = v;
                  break;
              case R.id.bt_plats:
                  if(lastSelected != null)
                      lastSelected.setBackgroundColor(Color.parseColor("#f1f3f4"));
                  option = SelectionOption.PLATS;
                  adapter.setFoodItems(foodItemlist.get("plats"));
                  adapter.notifyDataSetChanged();
                  v.setBackgroundColor(Color.parseColor("#fce883"));
                  lastSelected = v;
                  break;
              case R.id.bt_sandwiches:
                  if(lastSelected != null)
                      lastSelected.setBackgroundColor(Color.parseColor("#f1f3f4"));
                  option = SelectionOption.SANDWICHES;
                  adapter.setFoodItems(foodItemlist.get("sandwiches"));
                  adapter.notifyDataSetChanged();
                  v.setBackgroundColor(Color.parseColor("#fce883"));
                  lastSelected  =v;
                  break;
              case R.id.bt_drinks:
                  if(lastSelected != null)
                      lastSelected.setBackgroundColor(Color.parseColor("#f1f3f4"));
                  adapter.setFoodItems(foodItemlist.get("drinks"));
                  adapter.notifyDataSetChanged();
                  option = SelectionOption.DRINKS;
                  v.setBackgroundColor(Color.parseColor("#fce883"));
                  lastSelected = v;
                  break;
          }
      }
  };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_food_list, container, false);
        FoodRecView = root.findViewById(R.id.FoodRecView);
pizzas = root.findViewById(R.id.bt_pizzas);
sandwiches = root.findViewById(R.id.bt_sandwiches);
plats = root.findViewById(R.id.bt_plats);
drinks = root.findViewById(R.id.bt_drinks);
burgers = root.findViewById(R.id.bt_burgers);
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

}