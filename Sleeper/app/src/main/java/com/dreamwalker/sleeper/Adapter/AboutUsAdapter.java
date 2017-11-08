package com.dreamwalker.sleeper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamwalker.sleeper.Interface.ItemClickListener;
import com.dreamwalker.sleeper.Model.AboutUs;
import com.dreamwalker.sleeper.R;
import com.dreamwalker.sleeper.Views.AboutAppActivity;
import com.dreamwalker.sleeper.Views.IntroActivity;
import com.dreamwalker.sleeper.Views.OpenLicenseActivity;

import java.util.List;

/**
 * Created by 2E313JCP on 2017-11-08.
 */

class AboutUsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView itemText;
    ItemClickListener itemClickListener;

    public AboutUsViewHolder(View itemView) {
        super(itemView);

        itemText = (TextView)itemView.findViewById(R.id.itemText);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }
}

public class AboutUsAdapter extends RecyclerView.Adapter<AboutUsViewHolder>{


    private List<AboutUs> aboutUsList;
    private Context context;

    public AboutUsAdapter(List<AboutUs> aboutUsList, Context context) {
        this.aboutUsList = aboutUsList;
        this.context = context;
    }

    @Override
    public AboutUsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_aboutus,parent,false);

        return new AboutUsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AboutUsViewHolder holder, int position) {

        holder.itemText.setText(aboutUsList.get(position).getItemText());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //Intent intent = new Intent(context, DetailAboutUsActivity.class);
                //intent.putExtra("index", aboutUsList.get(position).getItemText());
                //context.startActivity(intent);
                //Toast.makeText(context, "itemClicked", Toast.LENGTH_SHORT).show();

                if (aboutUsList.get(position).getItemText() == "Tutorial"){
                    Intent intent = new Intent(context, IntroActivity.class);
                    context.startActivity(intent);
                }
                if (aboutUsList.get(position).getItemText() == "Open Source License"){
                    Intent intent = new Intent(context, OpenLicenseActivity.class);
                    context.startActivity(intent);
                }

                if (aboutUsList.get(position).getItemText() == "About App"){
                    Intent intent = new Intent(context, AboutAppActivity.class);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return aboutUsList.size();
    }
}
