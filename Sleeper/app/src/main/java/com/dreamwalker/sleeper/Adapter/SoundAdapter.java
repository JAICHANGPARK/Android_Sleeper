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

class SoundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView minText, maxText, dateText;
    ItemClickListener itemClickListener;

    public SoundViewHolder(View itemView) {
        super(itemView);

        maxText = (TextView) itemView.findViewById(R.id.maxSound);
        minText = (TextView) itemView.findViewById(R.id.minSound);
        dateText = (TextView) itemView.findViewById(R.id.dateText);
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

public class SoundAdapter extends RecyclerView.Adapter<SoundViewHolder>{
    private Context context;
    private List<HeartRate> soundList;

    public SoundAdapter(Context context, List<HeartRate> soundList) {
        this.context = context;
        this.soundList = soundList;
    }

    @Override
    public SoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_sound_fragment,parent,false);
        SoundViewHolder soundViewHolder = new SoundViewHolder(itemView);
        return soundViewHolder;
    }

    @Override
    public void onBindViewHolder(SoundViewHolder holder, int position) {

        holder.maxText.setText(soundList.get(position).getMaxHeartRate());
        holder.minText.setText(soundList.get(position).getMinHeartRate());
        holder.dateText.setText(soundList.get(position).getDate());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                int pageNum = 2;
                String date = soundList.get(position).getDate();
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
        return soundList.size();
    }
}
