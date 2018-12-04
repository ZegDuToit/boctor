package com.example.fridtjof.nav_bar.models;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Type {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    private int id;
    public String name;
    public double value;

    public Type() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Type(int id, String name, double value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public Type(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public RelativeLayout createObj(Context _contextFragment){
        RelativeLayout parent = new RelativeLayout(_contextFragment);
        RelativeLayout.LayoutParams _llParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        parent.setLayoutParams(_llParam);

        TextView tv_type = new TextView(_contextFragment);
        tv_type.setText(name);
        tv_type.setTextSize(convertDpToPx(5, _contextFragment));

        TextView tv_deposit = new TextView(_contextFragment);
        RelativeLayout.LayoutParams tv_depositParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv_depositParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tv_deposit.setLayoutParams(tv_depositParam);
        tv_deposit.setText(Double.toString(value));

        parent.addView(tv_type);
        parent.addView(tv_deposit);

        return  parent;
    }

    private int convertDpToPx(float dp, Context _contextFragment){
        Resources r = _contextFragment.getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return (int)px;
    }
}
