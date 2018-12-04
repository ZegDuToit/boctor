package com.example.fridtjof.nav_bar.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fridtjof.nav_bar.MainActivity;
import com.example.fridtjof.nav_bar.R;

import java.util.List;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CustomAdapter extends ArrayAdapter<String>{

        private final LayoutInflater mInflater;
        private final Context mContext;
        private final List<Type> items;
        private final int mResource;

        public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, 0, objects);

            mContext = context;
            mInflater = LayoutInflater.from(context);
            mResource = resource;
            items = objects;
        }
        @Override
        public View getDropDownView(int position, @Nullable View convertView,
                                    @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        @Override
        public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        private View createItemView(int position, View convertView, ViewGroup parent){
            final View view = mInflater.inflate(mResource, parent, false);

            TextView offTypeTv = (TextView) view.findViewById(R.id.tv_aa_type);
            TextView numOffersTv = (TextView) view.findViewById(R.id.tv_aa_value);

            Type offerData = items.get(position);

            offTypeTv.setText(offerData.getName());

            String str = MainActivity.gerDouble().format(offerData.getValue()) + " €";
            numOffersTv.setText(str);

            return view;
        }
    }