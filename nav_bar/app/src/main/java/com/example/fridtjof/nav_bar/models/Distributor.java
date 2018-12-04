package com.example.fridtjof.nav_bar.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Distributor {

    public String name;

    public Distributor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Distributor(String name) {
        this.name = name;
    }
}
