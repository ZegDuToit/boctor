package com.example.fridtjof.nav_bar.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {
    private String name;
    private Integer typeID;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Product(String name, Integer type) {
        this.name = name;
        this.typeID = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return typeID;
    }

    public void setType(Integer type) {
        this.typeID = type;
    }


}
