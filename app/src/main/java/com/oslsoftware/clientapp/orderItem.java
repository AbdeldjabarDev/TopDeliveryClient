package com.oslsoftware.clientapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.Random;

public class orderItem  implements Parcelable {
    int id;
    String name;
    int price;
    int quantity;
    String ImageUrl;
    Restaurant makingRestaurant;


    @Override
    public boolean equals(@Nullable Object obj) {
        orderItem item = (orderItem) obj;
        if(this.id == item.getId())
        {
            return true;
        }
        return false;

    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Restaurant getMakingRestaurant() {
        return makingRestaurant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public orderItem(FoodItem e)
    {

        id = e.hashCode();
        this.name = e.getName();
        this.price = e.getPrice();
        this.ImageUrl = e.getImageUrl();
        this.quantity = 1;


    }
    public orderItem(newFoodItem e)
    {
        id = e.getFoodItem().hashCode();
        this.name = e.getFoodItem().getName();
        this.price = e.getFoodItem().getPrice();
        this.ImageUrl = e.getFoodItem().getImageUrl();
        this.makingRestaurant = e.makingRestaurant;


    }

    protected orderItem(Parcel in) {
        name = in.readString();
        price = in.readInt();
        quantity = in.readInt();
        ImageUrl = in.readString();
        //makingRestaurant = in.readParcelable();

    }

    public static final Creator<orderItem> CREATOR = new Creator<orderItem>() {
        @Override
        public orderItem createFromParcel(Parcel in) {
            return new orderItem(in);
        }

        @Override
        public orderItem[] newArray(int size) {
            return new orderItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeInt(quantity);
        dest.writeString(ImageUrl);
        dest.writeParcelable(makingRestaurant,0);

    }
}
