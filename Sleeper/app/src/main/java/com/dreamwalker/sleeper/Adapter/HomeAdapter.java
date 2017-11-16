package com.dreamwalker.sleeper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamwalker.sleeper.Interface.ItemClickListener;
import com.dreamwalker.sleeper.Model.Home;
import com.dreamwalker.sleeper.R;
import com.dreamwalker.sleeper.Views.SleepMainActivity;

import java.util.List;

/**
 * Created by 2E313JCP on 2017-11-15.
 */

class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView labelText, dataText;

    ItemClickListener itemClickListener;

    public HomeViewHolder(View itemView) {
        super(itemView);

        labelText = (TextView) itemView.findViewById(R.id.label);
        dataText = (TextView) itemView.findViewById(R.id.dataText);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    private List<Home> homeList;
    private Context context;

    public HomeAdapter(List<Home> homeList, Context context) {
        this.homeList = homeList;
        this.context = context;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_home, parent, false);
        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, int position) {

        holder.labelText.setText(homeList.get(position).getLabel());
        holder.dataText.setText(homeList.get(position).getData());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (homeList.get(position).getLabel() == "심박수") {

                    Intent intent = new Intent(context, SleepMainActivity.class);
                    context.startActivity(intent);
                }
                if (homeList.get(position).getLabel() == "산소포화도") {
                    int page = 1;
                    Intent intent = new Intent(context, SleepMainActivity.class);
                    intent.putExtra("One", page);
                    context.startActivity(intent);
                }
                if (homeList.get(position).getLabel() == "코골이") {
                    int page = 2;
                    Intent intent = new Intent(context, SleepMainActivity.class);
                    intent.putExtra("One", page);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }
}
