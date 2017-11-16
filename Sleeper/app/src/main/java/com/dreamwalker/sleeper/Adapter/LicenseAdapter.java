package com.dreamwalker.sleeper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamwalker.sleeper.Model.AboutUs;
import com.dreamwalker.sleeper.R;

import java.util.List;

/**
 * Created by 2E313JCP on 2017-11-08.
 */

public class LicenseAdapter extends BaseAdapter {

    private List<AboutUs> licenseList;
    private Context context;
    private LayoutInflater layoutInflater;

    public LicenseAdapter(List<AboutUs> licenseList, Context context) {
        this.licenseList = licenseList;
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return licenseList.size() ;
    }

    @Override
    public Object getItem(int position) {
        return licenseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        view = layoutInflater.inflate(R.layout.item_openlicense, null);
        TextView licenseText = (TextView)view.findViewById(R.id.licenseText);
        licenseText.setText(licenseList.get(position).getItemText());

        return view;
    }
}
