package com.dreamwalker.sleeper.Views;

import android.content.Context;
import android.content.Intent;
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

    ArrayList<Entry> yValue;
    ArrayList<HeartRate> heartRateList;
    ArrayList<String> monitoringDataList;

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
        yValue = new ArrayList<>();
        monitoringDataList = new ArrayList<>();
        // TODO: 2017-11-17 모니터힝 데이터 객체를 이곳에 생성했어요.


        // TODO: 2017-11-16 DetailSleepActivity에서 보낸온 값을 이곳에서 받는다.

        pageNumber = getIntent().getIntExtra("page", 0);
        date = getIntent().getStringExtra("date");
        Log.e(TAG, "pageNumber: " + pageNumber);

        // TODO: 2017-11-17 모니터링 화면에서 불러올 데이털르 처리하기 위해 아래와 같이 코드처리를 했어요.
        if (pageNumber != 10) {

            heartRateList = getIntent().getParcelableArrayListExtra("detail");
            // TODO: 2017-11-17 그 외의 경우 ( 심박, 코골이, 산소포화도 화면에서 가져올 때 여기를 거치겠죠?)
            for (int i = 0; i < heartRateList.size(); i++) {
                Log.e(TAG, i + " " + heartRateList.get(i));
                float hr = Float.parseFloat(heartRateList.get(i).getHeartrate());
                yValue.add(new Entry(i, hr));
            }

            // TODO: 2017-11-16 x축의 데이터를 표기하기위해 ArrayList에서 데이터를 역으로 가져와야한다.
            String[] value = new String[heartRateList.size()];
            for (int i = 0; i < heartRateList.size(); i++) {
                value[i] = heartRateList.get(i).getTime();
                Log.e(TAG, i + " " + value[i]);
            }

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new XAxisValueFormatter(value));
            xAxis.setGranularity(1);

        } else {

            // TODO: 2017-11-17 모니터링 데이터를 가져올 경우
            monitoringDataList = getIntent().getStringArrayListExtra("realdata");
            for (int i = 0; i < monitoringDataList.size(); i++) {
                Log.e(TAG, i + " " + monitoringDataList.get(i));
                float hr = Float.parseFloat(monitoringDataList.get(i));
                yValue.add(new Entry(i, hr));
            }
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        }

        LineDataSet lineDataSet = new LineDataSet(yValue, "전체 데이터");
        lineDataSet.setColor(Color.RED);
        if (pageNumber == 2) {
            lineDataSet.setColor(Color.WHITE); // 라인 색상 결정.
            lineDataSet.setCircleColor(Color.WHITE); // 라인 상의 동그란 원 색상 결정
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setCubicIntensity(0.2f);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillColor(Color.CYAN);
            lineDataSet.setFillAlpha(80);
            lineDataSet.setDrawCircles(false); // 라인 상의 동그란 원 삭제
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.animateX(1000);

        lineChart.getAxisRight().setEnabled(false);
        YAxis leftYAxis = lineChart.getAxisLeft();
        if (pageNumber == 0) {
            leftYAxis.setAxisMaximum(120f);
            leftYAxis.setAxisMinimum(50f);
        } else if (pageNumber == 1) {
            leftYAxis.setAxisMaximum(110f);
            leftYAxis.setAxisMinimum(80f);
        } else if (pageNumber == 2) {
            leftYAxis.setAxisMaximum(150f);
            leftYAxis.setAxisMinimum(80f);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (pageNumber == 10){
            finish();
        }else {
            Intent intent = new Intent(this, DetailSleepActivity.class);
            intent.putExtra("page", pageNumber);
            intent.putExtra("date", date);
            startActivity(intent);
            finish();
        }
    }
}
