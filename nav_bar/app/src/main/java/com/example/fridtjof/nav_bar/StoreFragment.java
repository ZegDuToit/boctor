package com.example.fridtjof.nav_bar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class StoreFragment extends Fragment {
    @Nullable
    //@Overrid
    //
    View view;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store, container, false);

        ImageButton _btn_back = (ImageButton) view.findViewById(R.id.imgB_storeF_back);

        _btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                MainFragment mainFragment = new MainFragment();

                fragmentTransaction.replace(R.id.fl_mainA_content, mainFragment);
                fragmentTransaction.commit();
            }
        });


        initObj();

        return view;
    }

    private int convertDpToPx(float dp){
        Resources r = this.getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return (int)px;
    }

    private void initObj(){
        LinearLayout _ll_supermarket = (LinearLayout) view.findViewById(R.id.ll_storeF_supermarket);
        TextView _tv = (TextView) view.findViewById(R.id.tv_storeF);

        Set<SupermarketSel> uniqueSuper = new HashSet<SupermarketSel>();

        int size = MainFragment.objMap.size();
        for(Overview obj : MainFragment.objMap.values()){
            for(SupermarketSel sup : obj.get_supermarkets()) {
                uniqueSuper.add(sup);
            }
        }

        List<SupermarketSel> outputSup = new LinkedList<SupermarketSel>();
        int id = 0;
        for(SupermarketSel sup : uniqueSuper){
            int count = 0;
            for(Overview obj : MainFragment.objMap.values()){
                if(obj.get_supermarkets().indexOf(sup) > -1){
                    ++count;
                }
            }
            double perc = count*100/size;
            SupermarketSel supermarketSel = new SupermarketSel(id, sup.get_name(), false, perc);
            ++id;
            outputSup.add(supermarketSel);
        }
        List<SupermarketSel> supList = new LinkedList<SupermarketSel>();
        for(SupermarketSel sup : outputSup){
            supList.add(sup);
        }
        //supList.addAll(uniqueSuper);
        Collections.sort(supList, new SupermarketComparator());
        for(SupermarketSel sup : supList){
            sup.createObj(getContext(), _ll_supermarket);
            setClickListener(sup);
        }
        /*
        SupermarketSel sup1 = new SupermarketSel(1, "Rewe", false, 100);
        SupermarketSel sup2 = new SupermarketSel(2, "Edeka", false, 50.2);
        SupermarketSel sup3 = new SupermarketSel(3, "Penny", false, 25.76);
        SupermarketSel sup4 = new SupermarketSel(4, "Kaufland", false, 0.024);
        SupermarketSel sup5 = new SupermarketSel(5, "Spar", false, 0.01);

        sup1.createObj(getContext(), _ll_supermarket);
        sup2.createObj(getContext(), _ll_supermarket);
        sup3.createObj(getContext(), _ll_supermarket);
        sup4.createObj(getContext(), _ll_supermarket);
        sup5.createObj(getContext(), _ll_supermarket);

        setClickListener(sup1);
        setClickListener(sup2);
        setClickListener(sup3);
        setClickListener(sup4);
        setClickListener(sup5);
        */
    }

    private void setClickListener(final SupermarketSel obj){
        obj.get_imgB().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                String arg = "geo:0,0?q=" + obj.get_name();
                Uri gmmIntentUri = Uri.parse(arg);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
}

