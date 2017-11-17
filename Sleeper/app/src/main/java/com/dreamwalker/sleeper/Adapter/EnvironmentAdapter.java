package com.dreamwalker.sleeper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamwalker.sleeper.Model.Environment;
import com.dreamwalker.sleeper.R;

import java.util.List;

/**
 * Created by 2E313JCP on 2017-10-26.
 */

class EnvironmentViewHolder extends RecyclerView.ViewHolder {

    TextView tempText, tempTextValue;
    TextView humiText, humiTextValue;
    TextView coText, coTextValue;
    TextView fireText, fireTextValue;
    TextView dustText, dustTextValue;
    TextView dateText, dateTextValue;
    TextView timeText, timeTextValue;

    public EnvironmentViewHolder(View view) {
        super(view);

        tempText = view.findViewById(R.id.tempText);
        tempTextValue = view.findViewById(R.id.tempTextValue);

        humiText = view.findViewById(R.id.humiText);
        humiTextValue = view.findViewById(R.id.humiTextValue);

        coText = view.findViewById(R.id.coText);
        coTextValue = view.findViewById(R.id.coTextValue);

        fireText = view.findViewById(R.id.fireText);
        fireTextValue = view.findViewById(R.id.fireTextValue);

        dustText = view.findViewById(R.id.dustText);
        dustTextValue = view.findViewById(R.id.dustTextValue);

        dateText = view.findViewById(R.id.dateText);
        dateTextValue = view.findViewById(R.id.dateTextValue);

        timeText = view.findViewById(R.id.timeText);
        timeTextValue = view.findViewById(R.id.timeTextValue);


    }
}

public class EnvironmentAdapter extends RecyclerView.Adapter<EnvironmentViewHolder> {
    private static final String TAG = "EnvironmentAdapter";

    Context context;
    List<Environment> environmentList;

    public EnvironmentAdapter(Context context, List<Environment> environmentList) {
        this.context = context;
        this.environmentList = environmentList;
    }

    @Override
    public EnvironmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_environment, parent, false);

        return new EnvironmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EnvironmentViewHolder holder, int position) {

        holder.tempTextValue.setText(environmentList.get(position).getTemp());
        holder.humiTextValue.setText(environmentList.get(position).getHumi());
        holder.fireTextValue.setText(environmentList.get(position).getFire());
        holder.coTextValue.setText(environmentList.get(position).getGas());
        holder.dustTextValue.setText(environmentList.get(position).getDust());
        holder.dateTextValue.setText(environmentList.get(position).getEnvDate());
        holder.timeTextValue.setText(environmentList.get(position).getEnvTime());

    }

    @Override
    public int getItemCount() {
        return environmentList.size();
    }


}
