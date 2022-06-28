package com.example.groupproject4520kroo.Model;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Group implements Serializable {
    private String name;
    private String id;

    public Group(String name, String id, String adminId) {
        this.name = name;
        this.id = id;
        this.adminId = adminId;
    }

    private String adminId;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }


    public Group(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Group{}";
    }

    public Group() {
    }
}
