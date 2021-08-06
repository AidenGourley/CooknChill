package com.example.cooknchill.auth;

public class Dish {

    public String dishDescription, nationality, dishPic0, dishPic1;
    public int id;
    public boolean discoverable;

    public Dish() {
        //Default constructor required for calls to DataSnapshot
    }

    public Dish(int id) {
        this.id = id;
        this.discoverable = false;
        this.dishDescription = "";
        this.nationality = "Select Dish Culture";
        this.dishPic0 = "https://firebasestorage.googleapis.com/v0/b/cooknchill.appspot.com/o/defaultFiles%2FdefaultDishPicture0.png?alt=media&token=4759b6ce-d2ce-4f93-b361-d2a2628d1c9b";
        this.dishPic1 = "https://firebasestorage.googleapis.com/v0/b/cooknchill.appspot.com/o/defaultFiles%2FdefaultDishPicture1.png?alt=media&token=852d9d71-8581-4b22-bfb4-6d0a6e0f7db3";
    }
}