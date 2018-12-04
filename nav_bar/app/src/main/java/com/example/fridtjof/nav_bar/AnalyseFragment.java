package com.example.fridtjof.nav_bar;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fridtjof.nav_bar.models.Type;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class AnalyseFragment extends Fragment {
    View view;

    @Nullable
    //@Overrid
    //

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_analyse, container, false);

        final TextView tv = (TextView) view.findViewById(R.id.tv_analF_dbContent);
        final ImageView iv = (ImageView) view.findViewById(R.id.iv_analF);

        //final DatabaseReference disRef = ref.child("deposit").child("4029764001807").child("pic");
        // Get the data from an ImageView as bytes

        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);




/*
       disRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               //v.setText(dataSnapshot.getValue().toString());
                iv.setImageBitmap(stringToBitMap(dataSnapshot.getValue().toString()));
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
*/
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Uri chosenImageUri = data.getData();

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), chosenImageUri);
                //Bitmap bmp =  BitmapFactory.decodeFile(");//your image
                ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
                bmp.recycle();
                byte[] byteArray = bYtE.toByteArray();
                String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);

                DatabaseReference nameRef = ref.child("deposit").child("4029764001807").child("pic");
                nameRef.setValue(imageFile);
            } catch (Exception e){
                e.getMessage();
            }
        }
    }

    public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}