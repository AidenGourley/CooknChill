package com.example.cooknchill.auth;

public class User {
    public String uid, university,firstName,surname,course,bio,nationality,profilePic,dishPreference1,
            dishPreference2,dishPreference3;
    public int courseDuration = 0;
    public Dishes dishes;

    public User(){
    }

    public User(String uid, String university, String firstName, String surname){
        this.uid = uid;
        this.dishes = new Dishes(new Dish(0));
        this.university = university;
        this.firstName = firstName;
        this.surname = surname;
        this.course = "";
        this.courseDuration = 1;
        this.nationality = "";
        this.bio = "";
        this.profilePic = "https://firebasestorage.googleapis.com/v0/b/cooknchill.appspot.com/o/defaultFiles%2FdefaultProfilePicture.png?alt=media&token=34db9da4-b96f-4396-80b2-325765ca2754";
        this.dishPreference1 = "Select Nationality";
        this.dishPreference2 = "Select Nationality";
        this.dishPreference3 = "Select Nationality";
    }
}
