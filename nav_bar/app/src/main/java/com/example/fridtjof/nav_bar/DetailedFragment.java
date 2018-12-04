package com.example.fridtjof.nav_bar;

import android.content.res.Resources;
import android.graphics.ImageFormat;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailedFragment extends Fragment {
    @Nullable
    //@Override
            View myview;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);

        Bundle bundle = getArguments();
        final int id = bundle.getInt("OverviewID");

        if(MainFragment.objMap.containsKey(id)){

            final Overview obj = MainFragment.objMap.get(id);
            TextView _detailedViewError = (TextView) view.findViewById(R.id.detailedViewError);
            ImageView _imageView2 = (ImageView) view.findViewById(R.id.imageView2);
            TextView _price = (TextView) view.findViewById(R.id.tv_detailF_price);
            LinearLayout _parent = (LinearLayout) view.findViewById(R.id.ll_detailF_supermarket);
            final TextView _tvquantity = (TextView) view.findViewById(R.id.tv_detailF_quantity);
            ImageButton _minus = (ImageButton) view.findViewById(R.id.imgB_detailF_minus);
            ImageButton _plus = (ImageButton) view.findViewById(R.id.imgB_detailF_plus);
            ImageButton _bin = (ImageButton) view.findViewById(R.id.imgB_detailF_bin);
            final TextView _dep = (TextView) view.findViewById(R.id.tv_detailF_totDep);


            _detailedViewError.setText(obj.get_name());
            _imageView2.setImageBitmap(obj.get_img());
            String str = MainActivity.gerDouble().format(obj.get_price()) + " €";
            _price.setText(str);

            double totDep = obj.get_price() * obj.get_quantity();
            str = MainActivity.gerDouble().format(totDep) + " €";
            _dep.setText(str);



            _tvquantity.setText(Integer.toString(obj.get_quantity()));


            if(obj.get_supermarkets() != null) {
                for(SupermarketSel sup : obj.get_supermarkets()){
                    TextView tv = new TextView(getContext());
                    tv.setText(sup.get_name());
                    tv.setTextSize(convertDpToPx(5));
                    _parent.addView(tv);
                }
            }
            _minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = obj.get_quantity();
                    if(quantity > 1) {
                        obj.set_quantity(--quantity);
                        _tvquantity.setText(Integer.toString(obj.get_quantity()));

                        double totDep = obj.get_price() * obj.get_quantity();
                        String str = MainActivity.gerDouble().format(totDep) + " €";
                        _dep.setText(str);
                    }
                }
            });

            _plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = obj.get_quantity();
                    obj.set_quantity(++quantity);
                    _tvquantity.setText(Integer.toString(obj.get_quantity()));

                    double totDep = obj.get_price() * obj.get_quantity();
                    String str = MainActivity.gerDouble().format(totDep) + " €";
                    _dep.setText(str);
                }
            });

            _bin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainFragment.objMap.remove(id);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    MainFragment mainFragment = new MainFragment();

                    fragmentTransaction.replace(R.id.fl_mainA_content, mainFragment);
                    fragmentTransaction.commit();
                }
            });

        }

        ImageButton _imgBBack = (ImageButton) view.findViewById(R.id.imgB_detailF_back);

        _imgBBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                MainFragment mainFragment = new MainFragment();

                fragmentTransaction.replace(R.id.fl_mainA_content, mainFragment);
                fragmentTransaction.commit();
            }
        });

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

}
