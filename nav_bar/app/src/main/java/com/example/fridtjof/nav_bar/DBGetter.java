
package com.example.fridtjof.nav_bar;
import com.example.fridtjof.nav_bar.models.Product;
import com.example.fridtjof.nav_bar.models.ProductContainer;
import com.example.fridtjof.nav_bar.models.Type;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class DBGetter {

    private FirebaseDatabase _database;
    private DatabaseReference _ref;
    private ArrayList<String> distributors = new ArrayList<>();
    public DBGetter() {
        _database = FirebaseDatabase.getInstance();
        _ref = FirebaseDatabase.getInstance().getReference();
    }


    public ArrayList<String> getData(/*String bc, final TextView tv*/){

        DatabaseReference disRef = _ref.child("distributor");

        disRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    //String output = "\n" + child.getKey().toString() + child.child("name").getValue().toString();
                    //tv.append(child.getValue().toString());
                    getDistributor((Map<String,Object>) child.getValue());
                    //Type item = new Type(Integer.parseInt(child.getKey().toString()), child.child("name").getValue().toString(), Double.parseDouble(child.child("type").getValue().toString()));
                    //items.add(item);
                }
                //retDis();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return distributors;
    }

    public ArrayList<String> retDis(){
        return distributors;
    }

    private void getDistributor(Map<String, Object> id){
        for (Map.Entry<String, Object> entry : id.entrySet()){
            String singleDis = (String) entry.getValue();
            distributors.add(singleDis);
        }
    }
}
