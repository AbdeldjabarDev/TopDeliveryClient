package com.oslsoftware.clientapp;

public class Userinfo {
    String phone_number;
    String card_number;
    String user_name;
    String password;
    String user_email;

    public String getUser_name() {
        return user_name;
    }
public Userinfo()
{
    this.password = "";
    this.card_number ="";
    this.password ="";
    this.user_email ="";
    this.user_name = "";
}
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public Userinfo(String phone_number, String card_number) {
        this.phone_number = phone_number;
        this.card_number = card_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }
}
