package com.oslsoftware.clientapp;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {
    ArrayList<newFoodItem> foodItems;
    ArrayList<orderItem> Orders;


    public ArrayList<orderItem> getOrders() {
        return Orders;
    }

    public ArrayList<newFoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(ArrayList<newFoodItem> foodItems) {
        this.foodItems = foodItems;
        notifyDataSetChanged();
    }
public void setOrders(ArrayList<orderItem> Orders)
{
    this.Orders = Orders;
}
    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    Context mContext;
    public FoodItemAdapter(Context context)
    {
        this.mContext = context;
        if(Orders == null)
            Orders = new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("FoodItemAdapter","onBindViewHolder Called ");
        holder.AddToCart.setVisibility(View.GONE);
        newFoodItem foodItem = foodItems.get(position);
Glide.with(mContext)
        .asBitmap()
        .load(foodItem.item.ImageUrl)
        .into(holder.FoodItemImage);
holder.FoodPrice.setText(String.valueOf(foodItem.item.price) + " DZD");
holder.FoodName.setText(foodItem.item.name);
holder.makingRestaurantName.setText(foodItem.makingRestaurant.name);
if(foodItem.getFoodItem().available != 0 || foodItem.makingRestaurant.getStatus() == false)
{
    holder.availability.setText("Available");
    holder.availability.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getResources().getDrawable(R.drawable.ic_available_icon),null);

}
else
{
    holder.availability.setText("Not Available");
    holder.availability.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getResources().getDrawable(R.drawable.ic_unavailabke_icon),null);
}
holder.parent.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(holder.AddToCart.getVisibility() == View.GONE)
        {
            TransitionManager.beginDelayedTransition(holder.parent);
            holder.AddToCart.setVisibility(View.VISIBLE);
            if(foodItem.getFoodItem().available == 0 )
            {
               holder.AddToCart.setEnabled(false);
            }
        }
        else
        {

            holder.AddToCart.setVisibility(View.GONE);

        }

    }
});
holder.AddToCart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
   orderItem item = new orderItem(foodItem);
if(Orders.isEmpty())
{
    Orders.add(item);
}
else
{
    if(!Orders.contains(item))
    {
        Orders.add(item);
        Log.d("FoodItemAdapter","item added to orders");
    }
}


    }
});
    }

    @Override
    public int getItemCount() {
        Log.d("FoodItemAdapter","item count" + foodItems.size());
        return foodItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
ImageView FoodItemImage;
CardView parent;
TextView FoodName,makingRestaurantName,FoodPrice,availability;
Button AddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            FoodItemImage = itemView.findViewById(R.id.food_image);
            FoodName = itemView.findViewById(R.id.food_name);
            FoodPrice = itemView.findViewById(R.id.food_price);
            makingRestaurantName = itemView.findViewById(R.id.restaurant_name);
            parent = itemView.findViewById(R.id.cardFoodItemParent);
            AddToCart = itemView.findViewById(R.id.bt_addToCart);
            availability = itemView.findViewById(R.id.txtAvailability);
        }
    }
}
