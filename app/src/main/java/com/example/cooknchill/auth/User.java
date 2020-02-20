package com.example.cooknchill.auth;

public class User {
    public String uid, university,firstName,surname,course,bio,nationality,profilePic,dishPreference1,
            dishPreference2,dishPreference3;
    public int courseDuration = 0;

    public User(){
    }

    public User(String uid, String university, String firstName, String surname){
        this.uid = uid;
        this.university = university;
        this.firstName = firstName;
        this.surname = surname;
        this.course = "";
        this.courseDuration = 1;
        this.nationality = "";
        this.bio = "";
        this.profilePic = "";
        this.dishPreference1 = "Select Nationality";
        this.dishPreference2 = "Select Nationality";
        this.dishPreference3 = "Select Nationality";
    }
}
