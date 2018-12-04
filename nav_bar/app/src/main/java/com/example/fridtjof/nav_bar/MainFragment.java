package com.example.fridtjof.nav_bar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.os.Bundle;


@SuppressWarnings("serial")
public class MainFragment extends Fragment implements Serializable{

    public static Map<Integer, Overview> objMap = new HashMap<Integer, Overview>();
    public static boolean objInit = true;

    View view;
    @Nullable
    //@Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        LinearLayout parent = (LinearLayout) view.findViewById(R.id.ll_mainF_allObj);
        if(objInit){
            iniObjects();
            objInit = false;
        }
        for (Overview it : objMap.values()) {
            it.createObj(getContext(), parent);
            setClickListener(it);
        }

        TextView _totDep = (TextView) view.findViewById(R.id.tv_mainF_totalDep);
        String str = MainActivity.gerDouble().format(getTotalDep()) + " €";

        Button _store = (Button) view.findViewById(R.id.btn_mainF_store);

        _store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                StoreFragment storeFragment = new StoreFragment();
                fragmentTransaction.replace(R.id.fl_mainA_content, storeFragment);
                fragmentTransaction.commit();
            }
        });

        _totDep.setText(str);

        return view;
    }

    private double getTotalDep(){
        double res = 0;
        int quantity = 0;
        double deposit = 0;
        for(Overview it : objMap.values()){
            quantity = it.get_quantity();
            deposit = it.get_price();
            res += quantity * deposit;
        }
        return res;
    }



    private void setClickListener(final Overview obj){
        obj.get_ImgB().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("OverviewID", obj.get_id());

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                DetailedFragment detailedFragment = new DetailedFragment();
                detailedFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fl_mainA_content, detailedFragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void iniObjects() {
        /*
        LinearLayout parent = (LinearLayout) view.findViewById(R.id.ll_mainF_allObj);
        int id = 1;
        final Overview obj1 = new Overview(id, R.mipmap.ic_astra, "Astra", 0.08, 20);
        final Overview obj2 = new Overview(++id, R.mipmap.ic_clubmate, "Club Mate", 0.15, 5);
        final Overview obj3 = new Overview(++id, R.mipmap.ic_sterni, "Sternburg", 0.08, 50);
        final Overview obj4 = new Overview(++id, R.mipmap.ic_astra, "Astra", 0.08, 20);
        final Overview obj5 = new Overview(++id, R.mipmap.ic_clubmate, "Club Mate", 0.15, 5);
        final Overview obj6 = new Overview(++id, R.mipmap.ic_sterni, "Sternburg", 0.08, 50);

        List<String> supermarketsObj1 = new ArrayList<String>();
        supermarketsObj1.add("Rewe");
        supermarketsObj1.add("Edeka");
        supermarketsObj1.add("Kaufland");
        supermarketsObj1.add("Spar");

        obj1.set_supermarkets(supermarketsObj1);


        List<String> supermarketsObj2 = new ArrayList<String>();

        supermarketsObj2.add("Aldi");
        supermarketsObj2.add("Lidl");
        supermarketsObj2.add("Rewe");

        obj2.set_supermarkets(supermarketsObj2);

        objMap.put(obj1.get_id(), obj1);
        objMap.put(obj2.get_id(), obj2);
        objMap.put(obj3.get_id(), obj3);
        objMap.put(obj4.get_id(), obj4);
        objMap.put(obj5.get_id(), obj5);
        objMap.put(obj6.get_id(), obj6);
        */
    }
}
