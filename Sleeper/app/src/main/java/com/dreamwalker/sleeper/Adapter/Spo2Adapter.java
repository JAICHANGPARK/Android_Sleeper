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

class Spo2AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView minText, maxText, dateText;
    ItemClickListener itemClickListener;

    public Spo2AdapterViewHolder(View itemView) {
        super(itemView);

        maxText = (TextView)itemView.findViewById(R.id.maxSpo2);
        minText = (TextView)itemView.findViewById(R.id.minSpo2);
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

public class Spo2Adapter extends RecyclerView.Adapter<Spo2AdapterViewHolder> {
    private List<HeartRate> Spo2List;
    private Context context;

    public Spo2Adapter(List<HeartRate> heartRateList, Context context) {
        this.Spo2List = heartRateList;
        this.context = context;
    }

    @Override
    public Spo2AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_spo2_fragment,parent,false);
        Spo2AdapterViewHolder spo2AdapterViewHolder = new Spo2AdapterViewHolder(itemView);
        return spo2AdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(Spo2AdapterViewHolder holder, int position) {

        holder.maxText.setText(Spo2List.get(position).getMaxHeartRate());
        holder.minText.setText(Spo2List.get(position).getMinHeartRate());
        holder.dateText.setText(Spo2List.get(position).getDate());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                int pageNum = 1;
                String date = Spo2List.get(position).getDate();
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
        return Spo2List.size();
    }
}
