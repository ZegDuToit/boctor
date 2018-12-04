package com.example.fridtjof.nav_bar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Overview extends FragmentActivity implements Serializable {
    private int _quantity, _id;
    private String _name;
    private double _price;
    private List<SupermarketSel> _supermarkets;
    private CheckBox _chkBox;
    private Bitmap _img;

    private ImageButton _imgB;

    public Overview(int id, String name, double price, int quantity, List<SupermarketSel> supermarketSels, Bitmap imgRes) {
        _id = id;
        _name = name;
        _price = price;
        _quantity = quantity;
        _supermarkets = supermarketSels;
        _img = imgRes;
    }

    public Overview(Overview obj){
        _img = obj.get_img();
        _quantity = obj.get_quantity();
        _name = obj.get_name();
        _price = obj.get_price();
        _supermarkets = obj.get_supermarkets();
    }

    public void createObj(Context contextFragment, LinearLayout parent){
        // obj Layout
        final RelativeLayout obj = new RelativeLayout(contextFragment);
        RelativeLayout.LayoutParams objParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        obj.setLayoutParams(objParam);
        obj.setBackgroundResource(R.color.color3);

        // Create a pic in obj
        ImageView img1 = new ImageView(contextFragment);
        LinearLayout.LayoutParams imgmar = new LinearLayout.LayoutParams(convertDpToPx(70, contextFragment), convertDpToPx(70, contextFragment));

        imgmar.setMargins(convertDpToPx(8, contextFragment), convertDpToPx(8, contextFragment), convertDpToPx(8, contextFragment), convertDpToPx(8, contextFragment));
        img1.setLayoutParams(imgmar);
        img1.setId(R.id.overviewImg1);
        img1.setImageBitmap(_img);

        // Create TextViews in linear layout in obj
        LinearLayout tv_lin = new LinearLayout(contextFragment);
        tv_lin.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams tv_linParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv_linParam.addRule(RelativeLayout.RIGHT_OF, R.id.overviewImg1);
        tv_lin.setLayoutParams(tv_linParam);

        TextView tv11 = new TextView(contextFragment);
        TextView tv12 = new TextView(contextFragment);
        TextView tv13 = new TextView(contextFragment);
        tv11.setText(_name);
        tv11.setTextSize(20);
        String str = MainActivity.gerDouble().format(_price) + " €";
        tv12.setText(str);
        str = Integer.toString(_quantity) + " Stück";
        tv13.setText(str);

        tv_lin.addView(tv11);
        tv_lin.addView(tv12);
        tv_lin.addView(tv13);

        //Create ImageButton in obj
        _imgB = new ImageButton(contextFragment);
        RelativeLayout.LayoutParams imgB1param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, convertDpToPx(70+8, contextFragment));
        //int id = View.generateViewId();
        _imgB.setId(_id);
        _imgB.setLayoutParams(imgB1param);
        _imgB.setBackgroundColor(Color.TRANSPARENT);

        View v = new View(contextFragment);
        RelativeLayout.LayoutParams vParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        vParam.addRule(RelativeLayout.ALIGN_BOTTOM);
        v.setLayoutParams(vParam);
        v.setBackgroundResource(R.color.horizontalLine);

        //Create Checkbox
        _chkBox = new CheckBox(contextFragment);
        RelativeLayout.LayoutParams chkBox_linParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        chkBox_linParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        _chkBox.setLayoutParams(chkBox_linParam);
        _chkBox.setId(_id);

        obj.addView(img1);
        obj.addView(tv_lin);
        obj.addView(_imgB);
        obj.addView(_chkBox);
        obj.addView(v);
        parent.addView(obj);

    }

    private int convertDpToPx(float dp, Context contextFragment){
        Resources r = contextFragment.getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return (int)px;
    }

    public int get_id() {
        return _id;
    }

    public ImageButton get_ImgB() {
        return _imgB;
    }

    public Bitmap get_img() {
        return _img;
    }

    public int get_quantity() {
        return _quantity;
    }

    public String get_name() {
        return _name;
    }

    public double get_price() {
        return _price;
    }

    public List<SupermarketSel> get_supermarkets() {
        return _supermarkets;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }

    public CheckBox get_chkBox(){
        return _chkBox;
    }
}
