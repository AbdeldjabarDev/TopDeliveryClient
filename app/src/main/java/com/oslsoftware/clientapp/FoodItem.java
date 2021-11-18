package com.oslsoftware.clientapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.jar.Pack200;

public class FoodItem implements Parcelable {

    String name;
    int price;
    String ImageUrl;
    int available;

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public FoodItem()
{

}
    protected FoodItem(Parcel in) {
        name = in.readString();
        price = in.readInt();
        ImageUrl = in.readString();
        available  =  in.readInt();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public int getPrice() {
        return price;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(ImageUrl);
        dest.writeInt(available);
    }
}
