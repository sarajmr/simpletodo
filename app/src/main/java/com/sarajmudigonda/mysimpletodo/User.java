package com.sarajmudigonda.mysimpletodo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.util.ArrayList;

import static java.security.AccessController.getContext;


/**
 * Created by Saraj.Mudigonda on 2/8/2017.
 */

public class User {
    public String food_name;
    public String order_day;


    public User(String food_name, String order_day) {
        this.food_name = food_name;
        this.order_day= order_day;
    }

    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();

        users.add(new User("Asian Tofu Salad", "Add to cart"));
        users.add(new User("Chicken Tostada Salad", "Add to cart"));
        users.add(new User("Greek Salad  With Chicken", "Add to cart"));
        users.add(new User("Grilled Tofu", "Add to cart"));
        users.add(new User("Paneer Tikka Masala", "Add to cart"));
        return users;
    }
}