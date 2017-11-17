package com.dreamwalker.sleeper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamwalker.sleeper.Interface.ItemClickListener;
import com.dreamwalker.sleeper.Model.HeartRate;
import com.dreamwalker.sleeper.R;
import com.dreamwalker.sleeper.Views.DetailSleepActivity;

import java.util.List;

/**
 * Created by 2E313JCP on 2017-11-16.
 */

class HeartRateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView minText, maxText, dateText;

    ItemClickListener itemClickListener;

    public HeartRateViewHolder(View itemView) {
        super(itemView);

        maxText = (TextView)itemView.findViewById(R.id.max_hr);
        minText = (TextView)itemView.findViewById(R.id.min_hr);
        dateText = (TextView)itemView.findViewById(R.id.dateText);
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

public class HeartRateAdapter extends RecyclerView.Adapter<HeartRateViewHolder> {
    private List<HeartRate> heartRateList;
    private Context context;

    public HeartRateAdapter(List<HeartRate> heartRateList, Context context) {
        this.heartRateList = heartRateList;
        this.context = context;
    }

    @Override
    public HeartRateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_hr_fragment,parent,false);
        HeartRateViewHolder heartRateViewHolder = new HeartRateViewHolder(itemView);
        return heartRateViewHolder;
    }

    @Override
    public void onBindViewHolder(HeartRateViewHolder holder, int position) {

        holder.maxText.setText(heartRateList.get(position).getMaxHeartRate());
        holder.minText.setText(heartRateList.get(position).getMinHeartRate());
        holder.dateText.setText(heartRateList.get(position).getDate());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                int pageNum = 0;
                String date = heartRateList.get(position).getDate();
                Intent intent = new Intent(context, DetailSleepActivity.class);
                intent.putExtra("date",date);
                intent.putExtra("page",pageNum);
                context.startActivity(intent);

                Toast.makeText(context ,date, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return heartRateList.size();
    }
}
