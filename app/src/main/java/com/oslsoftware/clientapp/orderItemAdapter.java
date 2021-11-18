package com.oslsoftware.clientapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class orderItemAdapter extends BaseAdapter {
    ArrayList<orderItem> items;
    Context mContext;
    //TODO : add remove from cart button
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public orderItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setItems(ArrayList<orderItem> items) {
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView itemPrice,itemName,itemQuantity;
        ImageView itemImage,button_plus,button_minus;
        Button remove_bt;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.order_item,null);
        itemPrice = view.findViewById(R.id.txtOrderItemPrice);
        itemName = view.findViewById(R.id.txtOrderItemName);
        itemQuantity = view.findViewById(R.id.txtQuantity);
        itemImage = view.findViewById(R.id.orderItemImage);
        button_minus = view.findViewById(R.id.bt_minus);
        button_plus = view.findViewById(R.id.bt_plus);
        remove_bt = view.findViewById(R.id.removeBt);
        itemPrice.setText(String.valueOf(items.get(position).price) + " DZD");
        itemName.setText(items.get(position).name);
        itemQuantity.setText(String.valueOf(items.get(position).quantity));
        Glide.with(mContext)
                .asBitmap()
                .load(items.get(position).ImageUrl)
                .into(itemImage);
        button_minus.setOnClickListener(v -> {
            int q = items.get(position).quantity - 1;


            if(q > 0 )
            {
                items.get(position).setQuantity(q);
                itemQuantity.setText(String.valueOf(q));
                Log.d("OrderItemAdapter","item quantity text set with value" + q );
            }
        });
        button_plus.setOnClickListener(v -> {
            int q = items.get(position).quantity + 1;
            if(q <= 10)
            {
                items.get(position).setQuantity(q);
                itemQuantity.setText(String.valueOf(q));
                Log.d("OrderItemAdapter","item quantity text set with value" + q);
            }
        });
        remove_bt.setOnClickListener(v -> {
            items.remove(position);
            notifyDataSetChanged();
        });
        return view;
    }
}
