package com.example.cooknchill.auth;

public class User {
    public String uid, university,firstName,surname,course="";
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
    }
}
