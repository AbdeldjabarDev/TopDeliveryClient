package com.oslsoftware.clientapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoveryFragment extends Fragment {
    RecyclerView restaurantRecView;
    RestaurantAdapter adapter;
    List<orderItem> Orders;
    ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
    Map<String,ArrayList<newFoodItem>> restFoodAL;
public final String TAG = "DiscFragLog";

    public void setOrders(List<orderItem> orders) {
        Orders = orders;
    }

    public DiscoveryFragment() {
        // Required empty public constructor
    }

    public void setRestFoodAL(Map<String, ArrayList<newFoodItem>> restFoodAL) {
        this.restFoodAL = restFoodAL;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RestaurantAdapter(getContext());
        adapter.setRestaurantFoodArrayList(restFoodAL);
        adapter.setRestaurants(restaurantArrayList);
        adapter.setManager(getParentFragmentManager());
        adapter.setOrders((ArrayList<orderItem>) Orders);
       /* if(savedInstanceState!=null)
        {
            adapter.setRestaurants(savedInstanceState.getParcelableArrayList("restaurants"));
        }*/
        /*getParentFragmentManager().setFragmentResultListener("restaurants", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if(restaurantArrayList.isEmpty())
                {
                    restaurantArrayList = result.getParcelableArrayList("restaurants");
                }
                    if(!restaurantArrayList.isEmpty())
                    {
                        Log.d(TAG,"Result received and adapter set");
                        adapter.setRestaurants(restaurantArrayList);
                        adapter.notifyDataSetChanged();
                    }
            }
        });
        */
    }
    public void setRestaurantArrayList(ArrayList<Restaurant> i)
    {
        this.restaurantArrayList = i;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discovery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        restaurantRecView = view.findViewById(R.id.restaurantsRecView);
        restaurantRecView.setAdapter(adapter);
        restaurantRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
      /*  getParentFragmentManager().setFragmentResultListener("restaurants", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
            }
        });*/

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("restaurants",restaurantArrayList);
    }
}