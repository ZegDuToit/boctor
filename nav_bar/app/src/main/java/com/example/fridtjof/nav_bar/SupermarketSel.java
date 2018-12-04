package com.example.fridtjof.nav_bar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;

public class SupermarketSel {
    
    private int _id;
    private  String _name;
    private double _percentage;
    private boolean _checkB;

    private ImageButton _imgB;
    private  CheckBox _chkB;
    
    public SupermarketSel(int id, String name, boolean checkB, double percentage){
        _id = id;
        _name = name;
        _percentage = percentage;
        _checkB = checkB;
    }

    public SupermarketSel(int id, String name, boolean checkB){
        _id = id;
        _name = name;
        _checkB = checkB;
    }
    
    public void createObj(Context _contextFragment,  LinearLayout _parent){
        RelativeLayout _ll = new RelativeLayout(_contextFragment);
        RelativeLayout.LayoutParams _llParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        _ll.setLayoutParams(_llParam);

        TextView _tv_supermarket = new TextView(_contextFragment);
        _tv_supermarket.setText(_name);
        _tv_supermarket.setTextSize(convertDpToPx(5, _contextFragment));

        _ll.addView(_tv_supermarket);


        if(!_checkB) {
            //////// acordance
            TextView _tv_accordance = new TextView(_contextFragment);
            RelativeLayout.LayoutParams _tv_accordanceParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            _tv_accordance.setLayoutParams(_tv_accordanceParam);
            _tv_accordance.setGravity(Gravity.RIGHT);

            String perStr = gerDouble().format(_percentage) + " %";
            _tv_accordance.setText(perStr);
            _tv_accordance.setTextSize(convertDpToPx(5, _contextFragment));
            _ll.addView(_tv_accordance);
            /////////
            ///////// imgB
            _imgB = new ImageButton(_contextFragment);
            RelativeLayout.LayoutParams imgB1param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            _imgB.setId(_id);
            _imgB.setLayoutParams(imgB1param);
            _imgB.setBackgroundColor(Color.TRANSPARENT);

            _ll.addView(_imgB);
            //////////
        } else{
            _chkB = new AppCompatCheckBox(_contextFragment);

            RelativeLayout.LayoutParams _chkBParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            _chkBParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            _chkB.setLayoutParams(_chkBParam);
            _chkB.setGravity(Gravity.RIGHT);

            _ll.addView(_chkB);
        }

        View v = new View(_contextFragment);
        RelativeLayout.LayoutParams vParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        vParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        v.setLayoutParams(vParam);
        v.setBackgroundResource(R.color.horizontalLine);


        _ll.addView(v);

        _parent.addView(_ll);
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

    private DecimalFormat gerDouble(){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern("#0.00");
        return df;
    }

    public ImageButton get_imgB(){
        return _imgB;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public boolean is_chkB() {
        return _chkB.isChecked();
    }

    public CheckBox get_chkB(){
        return _chkB;
    }

    public double get_percentage() {
        return _percentage;
    }

    @Override  // ** don't forget this annotation
    public int hashCode() { // *** note capitalization of the "C"
        return _name.hashCode();
    }

    @Override
    public boolean equals(Object me) {
        SupermarketSel binMe = (SupermarketSel) me;
        if(_name.equals(binMe.get_name()))
            return true;
        else
            return false;
    }


}
class SupermarketComparator implements Comparator<SupermarketSel> {
    public int compare(SupermarketSel sup1, SupermarketSel sup2) {
        return (int)(sup2.get_percentage() - sup1.get_percentage())*100;
    }
}
