package com.dreamwalker.sleeper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamwalker.sleeper.Model.HeartRate;
import com.dreamwalker.sleeper.R;

import java.util.List;

/**
 * Created by 2E313JCP on 2017-11-16.
 */
class MainDetailViewHolder extends RecyclerView.ViewHolder{
    TextView heartrate, date, time;

    public MainDetailViewHolder(View itemView) {
        super(itemView);
        heartrate = (TextView)itemView.findViewById(R.id.detail_heart);
        date = (TextView)itemView.findViewById(R.id.detail_date);
        time = (TextView)itemView.findViewById(R.id.detail_time);
    }
}

public class MainDetailAdapter extends RecyclerView.Adapter<MainDetailViewHolder>{
    Context context;
    List<HeartRate> heartRateList;

    public MainDetailAdapter(Context context, List<HeartRate> heartRateList) {
        this.context = context;
        this.heartRateList = heartRateList;
    }


    @Override
    public MainDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_detail,parent,false);
        MainDetailViewHolder mainDetailViewHolder = new MainDetailViewHolder(itemView);

        return mainDetailViewHolder;
    }

    @Override
    public void onBindViewHolder(MainDetailViewHolder holder, int position) {

        holder.heartrate.setText(heartRateList.get(position).getHeartrate());
        holder.date.setText(heartRateList.get(position).getDate());
        holder.time.setText(heartRateList.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return heartRateList.size();
    }
}
