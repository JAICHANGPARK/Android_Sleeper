package com.dreamwalker.sleeper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamwalker.sleeper.Interface.ItemClickListener;
import com.dreamwalker.sleeper.Model.News;
import com.dreamwalker.sleeper.R;
import com.dreamwalker.sleeper.Views.WebActivity;

import java.util.List;

/**
 * Created by 2E313JCP on 2017-11-17.
 */

class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView titleText, descriptionText, pubDateText;

    ItemClickListener itemClickListener;

    public NewsViewHolder(View itemView) {
        super(itemView);

        titleText =(TextView) itemView.findViewById(R.id.titleText);
        descriptionText = (TextView)itemView.findViewById(R.id.descriptionText);
        pubDateText = (TextView)itemView.findViewById(R.id.pubDateText);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}

public class NewsAdapter  extends RecyclerView.Adapter<NewsViewHolder>{
    Context context;
    List<News> newsList;

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView =  layoutInflater.inflate(R.layout.item_news,parent,false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(itemView);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, int position) {

        holder.titleText.setText(newsList.get(position).getTitle());
        holder.descriptionText.setText(newsList.get(position).getDescription());
        holder.pubDateText.setText(newsList.get(position).getPubDate());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                String link = newsList.get(position).getOriginallink();
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("webURL", link);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
