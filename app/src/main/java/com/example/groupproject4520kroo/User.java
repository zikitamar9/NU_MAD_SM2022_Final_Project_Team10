package com.example.groupproject4520kroo;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class User implements Serializable {
    //    private ArrayList<Chat> chats;
    private String email;

    public User(String name) {
        this.email = name;
    }

    public User(){};



    public String getName() {
        return email;
    }

    public void setName(String name) {
        this.email = name;
    }


    @Override
    public String toString() {
        return "User{" +
                ", name='" + email + '\'' +
                '}';
    }
}
