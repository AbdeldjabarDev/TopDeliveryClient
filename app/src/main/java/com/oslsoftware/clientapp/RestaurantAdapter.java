package com.oslsoftware.clientapp;

import android.app.ActivityManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
ArrayList<Restaurant> restaurants;
ArrayList<orderItem> Orders;




    public void setOrders(ArrayList<orderItem> orders) {
        Orders = orders;
    }

    public void setRestaurantFoodArrayList(Map<String,ArrayList<newFoodItem>>  restaurantFoodArrayList) {
        this.restaurantFoodArrayList = restaurantFoodArrayList;
    }

    Map<String,ArrayList<newFoodItem>> restaurantFoodArrayList;
Context mContext;
FragmentManager manager;
FoodList foodList;
    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    public RestaurantAdapter(Context mContext) {

        this.mContext = mContext;
        this.foodList = new FoodList();
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(restaurants.get(position).ImageUrl)
                .into(holder.restaurant_image);
        holder.restaurant_name.setText(restaurants.get(position).name);
        holder.restaurant_wilaya.setText(restaurants.get(position).wilaya);
        if(restaurants.get(position).status)
        {
            holder.RestOpened.setVisibility(View.VISIBLE);
            holder.RestClosed.setVisibility(View.GONE);
        }
        else
        {
            holder.RestOpened.setVisibility(View.GONE);
            holder.RestClosed.setVisibility(View.VISIBLE);
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manager != null)
                {
                    foodList = new FoodList();
                    foodList.setButtonsVisible(false);
                    FoodItemAdapter adapter = new FoodItemAdapter(mContext);
                    if(restaurantFoodArrayList.get(restaurants.get(position).getName()).isEmpty())
                    {
                        Toast.makeText(mContext, "the restaruant array list is empty", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        adapter.setFoodItems(restaurantFoodArrayList.get(restaurants.get(position).getName()));
                        adapter.setOrders(Orders);
                        foodList.setAdapter(adapter);
                        manager.beginTransaction().replace(R.id.content_container,foodList).commitNow();
                    }

                }

                else
                Toast.makeText(mContext, "Please set the fragment manager first", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
CardView parent;
TextView restaurant_name,restaurant_wilaya,RestOpened,RestClosed;
ImageView restaurant_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurant_name = itemView.findViewById(R.id.txtRestaurantName);
            restaurant_wilaya = itemView.findViewById(R.id.txtRestaurantWilaya);
            restaurant_image = itemView.findViewById(R.id.restaurant_image);
            parent = itemView.findViewById(R.id.cardRestParent);
            RestOpened = itemView.findViewById(R.id.txtrRestOpened);
            RestClosed = itemView.findViewById(R.id.txtRestClosed);
        }
    }

}
