package com.example.fridtjof.nav_bar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fridtjof.nav_bar.models.ProductContainer;
import com.google.zxing.Result;

import java.text.DecimalFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView zXingScannerView;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        verifyPermissions();
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }
    public static BlockingQueue<ProductContainer> blockingQueue = new LinkedBlockingQueue<>();

    @Override
    public void handleResult(Result rawResult) {
        //DBGetter db = new DBGetter();
        //db.loadByBarcode(blockingQueue, rawResult.getText());
        // <--CRASHED APP DONT KNOW WHY!!!!!!!!!!111

        Bundle bundle = new Bundle();
        bundle.putString("ScanRes", rawResult.getText());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AddScanFragment addScanFragment = new AddScanFragment();
        addScanFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fl_scan, addScanFragment);
        fragmentTransaction.commit();
        setContentView(R.layout.activity_scan);
}

    private void verifyPermissions() {
        String[] permissions = {Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ScanActivity.this, permissions, 1);
        }
    }
}