package com.dreamwalker.sleeper.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dreamwalker.sleeper.Adapter.AboutUsAdapter;
import com.dreamwalker.sleeper.Model.AboutUs;
import com.dreamwalker.sleeper.R;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AboutUsAdapter adapter;
    List<AboutUs> aboutUsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        recyclerView = (RecyclerView)findViewById(R.id.list_aboutus);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        aboutUsList = new ArrayList<>();

        aboutUsList.add(new AboutUs("About App"));
        aboutUsList.add(new AboutUs("Tutorial"));
        aboutUsList.add(new AboutUs("Open Source License"));

        adapter = new AboutUsAdapter(aboutUsList,this);
        recyclerView.setAdapter(adapter);

    }
}
