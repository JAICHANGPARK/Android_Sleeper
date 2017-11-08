package com.dreamwalker.sleeper.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.dreamwalker.sleeper.Adapter.LicenseAdapter;
import com.dreamwalker.sleeper.Model.AboutUs;
import com.dreamwalker.sleeper.R;

import java.util.ArrayList;
import java.util.List;

public class OpenLicenseActivity extends AppCompatActivity {

    private List<AboutUs> aboutUsList;
    private LicenseAdapter adapter;
    private ListView openlistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_license);
        setTitle("Open Source License");
        openlistview = (ListView)findViewById(R.id.openlistview);
        aboutUsList = new ArrayList<>();
        addLicense();

        adapter = new LicenseAdapter(aboutUsList,this);
        openlistview.setAdapter(adapter);
    }

    void addLicense(){

        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_lottie)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_appintro)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_butterknife)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_bubbleview)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_fbutton)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_bottombar)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_kenburnsview)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_circleimageview)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_glide)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_glide01)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_glide02)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_glide03)));
        aboutUsList.add(new AboutUs(getResources().getString(R.string.license_glide04)));

    }
}
