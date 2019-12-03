package com.example.cooknchill.auth;

public class User {
    private String university;
    private String firstName;
    private String surname;

    public User(String university, String firstName, String surname){
        this.university = university;
        this.firstName = firstName;
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUniversity() {
        return university;
    }

    public String getSurname() {
        return surname;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

}
