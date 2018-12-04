package com.example.fridtjof.nav_bar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fridtjof.nav_bar.models.ProductContainer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AddScanFragment extends Fragment {
    ///////////////// Future db values
    private double deposit = 0;
    private String name;
    private Bitmap pic = null;
    private List<SupermarketSel> supermarketSels = new LinkedList<SupermarketSel>();
    ////////////
    View view;
    private int numQuantity = 1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_scan, container, false);


        Bundle bundleRec = getArguments();
        final String barcodeSc = bundleRec.getString("ScanRes");

        ////////// find Barcode -> name, type, pic
        getBarcodeDB(barcodeSc);

        TextView _tvBarcode = (TextView) view.findViewById(R.id.tv_addSc_barcode);
        final TextView _quantity = (TextView) view.findViewById(R.id.tv_addSc_quantity);
        ImageButton _minus = (ImageButton) view.findViewById(R.id.imgB_addSc_Minus);
        ImageButton _bin = (ImageButton) view.findViewById(R.id.imgB_addSc_Bin);
        ImageButton _plus = (ImageButton) view.findViewById(R.id.imgB_addSc_Plus);
        Button _store = (Button) view.findViewById(R.id.btn_addSc_store);

        final TextView _totDep = (TextView) view.findViewById(R.id.tv_addSc_totDep);






            _tvBarcode.setText(barcodeSc);


            _quantity.setText(Integer.toString(numQuantity));


            _minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (numQuantity > 1)
                        _quantity.setText(Integer.toString(--numQuantity));
                    _totDep.setText(gerDouble().format(deposit * numQuantity));

                }
            });

            _bin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ScanActivity.class);
                    startActivity(intent);
                }
            });

            _plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ++numQuantity;
                    _quantity.setText(Integer.toString(numQuantity));
                    _totDep.setText(gerDouble().format(deposit * numQuantity));
                }
            });

            _store.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = 0;
                    if (MainFragment.objMap.size() > 0)
                        id = Collections.max(MainFragment.objMap.keySet()) + 1;
                    Overview obj = new Overview(id, name, deposit, numQuantity, supermarketSels, pic);

                    MainFragment.objMap.put(obj.get_id(), obj);
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
        //}

        return view;
    }

    private void getBarcodeDB(final String barcode){
        final TextView _tvName = (TextView) view.findViewById(R.id.tv_addSc_name);
        final TextView _dep = (TextView) view.findViewById(R.id.tv_addSc_deposit);
        final TextView _totDep = (TextView) view.findViewById(R.id.tv_addSc_totDep);
        final ImageView _pic = (ImageView) view.findViewById(R.id.imgV_addSc_pic);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference depRef = ref.child("deposit");
        final boolean res1 = false;
        depRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //find barcode
                    if (child.getKey().toString().equals(barcode)){
                        found = true;
                        name = child.child("name").getValue().toString();
                        _tvName.setText(name);

                        if(child.hasChild("pic"))
                            pic = MainActivity.stringToBitMap(child.child("pic").getValue().toString());
                        else
                            pic =  BitmapFactory.decodeResource(getResources(), R.mipmap.ic_bottle);
                        _pic.setImageBitmap(pic);

                        //get aquivilant type
                        final String typeID = child.child("type").getValue().toString();
                        DatabaseReference typeRef = ref.child("type");
                        typeRef.child(typeID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        deposit = Double.parseDouble(dataSnapshot.child("type").getValue().toString());
                                        String outDep = MainActivity.gerDouble().format(deposit) + " €";
                                        _dep.setText(outDep);

                                        String outTotDep = MainActivity.gerDouble().format(deposit*numQuantity) + " €";
                                        _totDep.setText(outTotDep);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        //get Supermarkets
                        final DatabaseReference depDisRef = ref.child("deposit_distributor");
                        depDisRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot child : dataSnapshot.getChildren()){
                                    if(child.child("deposit").getValue().toString().equals(barcode)){
                                        String p = "Found deposit " + child.child("deposit").getValue().toString() + " distributor: " + child.child("distributor").getValue().toString();
                                        Log.e("DB", p);
                                        //get Supermarket name
                                        final String distributorID = child.child("distributor").getValue().toString();
                                        DatabaseReference disRef = ref.child("distributor");
                                        disRef.child(distributorID).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String p = "Found distriputor ID: " + dataSnapshot.getKey() + " Name: " + dataSnapshot.child("name").getValue().toString();
                                                Log.e("DB", p);
                                                SupermarketSel sup = new SupermarketSel(Integer.parseInt(distributorID), dataSnapshot.child("name").getValue().toString(), false);
                                                supermarketSels.add(sup);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        break;
                    }
                }
                if(!found){
                    Bundle bundleSend = new Bundle();
                    bundleSend.putString("barcodeNewEntry", barcode);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    NewDBEntry newDBEntry = new NewDBEntry();
                    newDBEntry.setArguments(bundleSend);
                    fragmentTransaction.replace(R.id.fl_scan, newDBEntry);
                    fragmentTransaction.commit();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public DecimalFormat gerDouble(){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern("#0.00");
        return df;
    }


}
