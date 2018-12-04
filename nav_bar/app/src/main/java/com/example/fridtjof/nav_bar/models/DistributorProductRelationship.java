package com.example.fridtjof.nav_bar.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DistributorProductRelationship {

    public String name;

    public DistributorProductRelationship() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public DistributorProductRelationship(String name) {
        this.name = name;
    }
}
