package com.oslsoftware.clientapp;


import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {
    String wilaya;
    String name;
    boolean status;
   String location;
   String ImageUrl;
public Restaurant()
{

}
    protected Restaurant(Parcel in) {
        wilaya = in.readString();
        name = in.readString();
        status = in.readByte() != 0;
        location = in.readString();
        ImageUrl = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getLocation() {
        return location;
    }
    public String getWilaya() {
        return wilaya;
    }
    public String getName() {
        return name;
    }
    public String getImageUrl()
    {
        return this.ImageUrl;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wilaya);
        dest.writeString(name);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(location);
        dest.writeString(ImageUrl);
    }
    //TODO : somehow add location

}
