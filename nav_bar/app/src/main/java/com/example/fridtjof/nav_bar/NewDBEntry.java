package com.example.fridtjof.nav_bar;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CompoundButtonCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fridtjof.nav_bar.models.CustomAdapter;
import com.example.fridtjof.nav_bar.models.Type;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class NewDBEntry extends Fragment {
    View view;
    private static Map<Integer, SupermarketSel> supMap = new HashMap<Integer, SupermarketSel>();
    private List items = new LinkedList();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String barcode;
    private int dbChecksum = 0;
    private  String imageFile = null;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_dbentry, container, false);

        Bundle bundle = getArguments();
        barcode = bundle.getString("barcodeNewEntry");

        final EditText _bar = (EditText) view.findViewById(R.id.et_newDB_barcode);
        final EditText _name = (EditText) view.findViewById(R.id.et_newDB_name);
        //final EditText _deposit = (EditText) view.findViewById(R.id.et_newDB_dep);

        final TextView _tvDepRes = view.findViewById(R.id.tv_newDB_spinnerres);
        Button _btnStore = (Button) view.findViewById(R.id.btn_newDB_store);
        Button _btnImg = (Button) view.findViewById(R.id.btn_newDB_imagSel);

        _bar.setText(barcode);

        getSupermarkets();

        getDeposits();

        _tvDepRes.setText(" ");

        _btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        _btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chkSum = 0;

                if(_bar.getText().toString().trim().length() > 0){
                    ++chkSum;

                } else{
                    _bar.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                }
                if (_name.getText().toString().trim().length() > 0){
                    ++chkSum;

                } else{
                    _name.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                }

                int count = 0;
                List<Integer> selSup = new ArrayList<>();
                for(SupermarketSel it : supMap.values()){
                    if(it.is_chkB()){
                        ++count;
                        selSup.add(it.get_id());
                    }
                }
                if(count > 0)
                    ++chkSum;

                else{
                    ColorStateList colorStateList = new ColorStateList(
                            new int[][]{
                                    new int[]{-android.R.attr.state_checked}, // unchecked
                                    new int[]{android.R.attr.state_checked} , // checked
                            },
                            new int[]{
                                    Color.parseColor("#db0d0d"),
                                    Color.parseColor("#122823"),
                            }
                    );
                    for(SupermarketSel it : supMap.values()){
                        CompoundButtonCompat.setButtonTintList(it.get_chkB(),colorStateList);
                    }
                }

                //////////Store in DB
                if(chkSum >= 3){
                    Spinner _deposit = (Spinner) view.findViewById(R.id.sp_newDB_dep);
                    Type resT = new Type();
                    resT = (Type) _deposit.getSelectedItem();

                    ///////// Insert Deposit
                    DatabaseReference nameRef = ref.child("deposit").child(_bar.getText().toString()).child("name");
                    nameRef.setValue(_name.getText().toString());

                    DatabaseReference typeRef = ref.child("deposit").child(_bar.getText().toString()).child("type");
                    typeRef.setValue(Integer.toString(resT.getId()));

                    if(imageFile != null){
                        DatabaseReference picRef = ref.child("deposit").child(barcode).child("pic");
                        picRef.setValue(imageFile);
                    }

                    ///////// Insert Deposit - Distributor
                    DatabaseReference dep_disRef = ref.child("deposit_distributor");
                    for(Integer it : selSup) {
                        DatabaseReference pushedRef = dep_disRef.push();
                        pushedRef.child("deposit").setValue(_bar.getText().toString());
                        pushedRef.child("distributor").setValue(it.toString());
                    }
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }

                //////////
            }
        });

        return view;
    }
    private void getSupermarkets(){
        final LinearLayout _parent = (LinearLayout) view.findViewById(R.id.ll_newDB_sv);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("distributor");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<Integer, String> distributors = new HashMap<Integer, String>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Integer id = Integer.parseInt(child.getKey());
                    String name = child.child("name").getValue().toString();
                    distributors.put(id, name);
                }
                for(Map.Entry<Integer, String> entry : distributors.entrySet()) {
                    SupermarketSel sup = new SupermarketSel(entry.getKey(), entry.getValue(), true);
                    sup.createObj(getContext(), _parent);
                    supMap.put(sup.get_id(), sup);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //private final Spinner _deposit = (Spinner) view.findViewById(R.id.sp_newDB_dep);
    private void getDeposits(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("type");

        final Spinner _deposit = (Spinner) view.findViewById(R.id.sp_newDB_dep);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<Integer, String> types = new HashMap<Integer, String>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Integer id = Integer.parseInt(child.getKey());
                    String name = child.child("name").getValue().toString();
                    Double value = Double.parseDouble(child.child("type").getValue().toString());

                    Type type = new Type(id, name, value);

                    items.add(type);
                }
                CustomAdapter adapter = new CustomAdapter(getContext(), R.layout.arrayadapter_layout, items);
                _deposit.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            final TextView _tvDepRes = view.findViewById(R.id.tv_newDB_spinnerres);
            Uri chosenImageUri = data.getData();
            _tvDepRes.setText(getFileName(chosenImageUri));
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), chosenImageUri);
                //Bitmap bmp =  BitmapFactory.decodeFile(");//your image
                ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 10, bYtE);
                bmp.recycle();
                byte[] byteArray = bYtE.toByteArray();
                imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (Exception e){
                e.getMessage();
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}