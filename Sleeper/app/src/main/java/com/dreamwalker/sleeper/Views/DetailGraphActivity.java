package com.dreamwalker.sleeper.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dreamwalker.sleeper.Model.HeartRate;
import com.dreamwalker.sleeper.R;
import com.dreamwalker.sleeper.Utils.XAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class DetailGraphActivity extends AppCompatActivity {
    private static final String TAG = "DetailGraphActivity";
    private LineChart lineChart;
    public String date, address, port;

    ArrayList<HeartRate> heartRateList;
    private int pageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_graph);
        lineChart = (LineChart) findViewById(R.id.detail_linechart);

        setTitle("Detail Graph");
        SharedPreferences sharedPreferences = getSharedPreferences("SettingInit", Context.MODE_PRIVATE);
        address = sharedPreferences.getString("addressInit", "");
        port = sharedPreferences.getString("portInit", "");
        // TODO: 2017-11-15 만약 서버와 포트 값이 없다면 설정 화면으로 가도록 해야한다.
        if (!address.equals("") && !port.equals("")) {
            Log.e(TAG, "주소및포트값: " + "정상적으로 가져옴");

        } else {
            Log.e(TAG, "주소및포트값: " + "가져오지 못함.");
//            Toast.makeText(this, "서버의 주소와 포트를 설정해 주세요.", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//            startActivity(intent);
        }

        heartRateList = new ArrayList<>();
        ArrayList<Entry> yValue = new ArrayList<>();
        heartRateList = getIntent().getParcelableArrayListExtra("detail");
        pageNumber = getIntent().getIntExtra("page",0);
        for (int i = 0; i < heartRateList.size(); i++){
            Log.e(TAG, i + " " + heartRateList.get(i));
            float hr = Float.parseFloat(heartRateList.get(i).getHeartrate());
            yValue.add(new Entry(i, hr));
        }

        LineDataSet lineDataSet = new LineDataSet(yValue, "전체 데이터");
        lineDataSet.setColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.animateX(1000);

        String[] value = new String[heartRateList.size()];
        for (int i = 0; i < heartRateList.size(); i++){
            value[i] = heartRateList.get(i).getTime();
            Log.e(TAG, i + " " + value[i]);
        }

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new XAxisValueFormatter(value));
        xAxis.setGranularity(1);

        lineChart.getAxisRight().setEnabled(false);
        YAxis leftYAxis = lineChart.getAxisLeft();
        if (pageNumber == 0){
            leftYAxis.setAxisMaximum(120f);
            leftYAxis.setAxisMinimum(50f);
        }else if(pageNumber == 1){
            leftYAxis.setAxisMaximum(110f);
            leftYAxis.setAxisMinimum(80f);
        }


    }
}
