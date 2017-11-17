package com.dreamwalker.sleeper.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamwalker.sleeper.Adapter.EnvironmentAdapter;
import com.dreamwalker.sleeper.Model.Environment;
import com.dreamwalker.sleeper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentActivity extends AppCompatActivity implements NestedScrollView.OnScrollChangeListener {
    private static final String TAG = "EnvironmentActivity";

    public String address;
    public String port;

    public String userChooseDate;

    private ListView listView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EnvironmentAdapter adapter;
    List<Environment> environmentList;

    CalendarView calendarView;

    FloatingActionButton fab;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayShowTitleEnabled(false); // toolbar의 텍스트를 없앤다.
        getSupportActionBar().setTitle("Environment"); //이 방법도 있음.

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        listView = (ListView) findViewById(R.id.envListView);
//        environmentList = new ArrayList<>();
//        adapter = new EnvironmentAdapter(this, environmentList);
//        listView.setAdapter(adapter);


        calendarView = (CalendarView) findViewById(R.id.calendarView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        nestedScrollView = (NestedScrollView) findViewById(R.id.scroll);

        environmentList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.envRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EnvironmentAdapter(this, environmentList);
        recyclerView.setAdapter(adapter);
        nestedScrollView.getParent().requestChildFocus(nestedScrollView, nestedScrollView);

        SharedPreferences sharedPreferences = getSharedPreferences("SettingInit", Context.MODE_PRIVATE);

        address = sharedPreferences.getString("addressInit", "");
        port = sharedPreferences.getString("portInit", "");

        Log.e(TAG, "addressInit: " + address);
        Log.e(TAG, "portInit: " + port);

        if (!address.equals("") && !port.equals("")) {
            Log.e(TAG, "주소및포트값: " + "정상적으로 가져옴");

        } else {
            Log.e(TAG, "주소및포트값: " + "가져오지 못함.");

            Toast.makeText(this, "서버의 주소와 포트를 설정해 주세요.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EnvironmentActivity.this, SettingActivity.class);
            startActivity(intent);
            finish();
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                String yearString = Integer.toString(year);
                yearString = yearString.substring(2); // 2017의 년도에서 인덱스 2번쨰 1 이하의 문자열을 반환한다. (17 반환)

                Log.e(TAG, "onSelectedDayChange: " + date);
                userChooseDate = yearString + "-" + (month + 1) + "-" + dayOfMonth;
                Toast.makeText(EnvironmentActivity.this, userChooseDate, Toast.LENGTH_SHORT).show();
                environmentList.clear();
                adapter.notifyDataSetChanged();
                new BackgroundGetTask().execute();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
                Toast.makeText(EnvironmentActivity.this, "Hello Sleeper?", Toast.LENGTH_SHORT).show();
            }
        });

        new BackgroundTask().execute();
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        Log.e(TAG, "scrollX: " + scrollX);
        Log.e(TAG, "scrollY: " + scrollY);
        Log.e(TAG, "oldScrollX: " + oldScrollX);
        Log.e(TAG, "oldScrollY: " + oldScrollY);
        if (v.getChildAt(v.getChildCount() - 1) != null) {
            if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                //code to fetch more data for endless scrolling

            }
        }

    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        // TODO: 2017-10-12 해당 웹서버에 접속할수있게 해줌.
        @Override
        protected void onPreExecute() {
            try {

                target = "http://" + address + "/UserEnv.php";

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            int cnt = 0; // json 배열 회전 변수
            JSONObject jsonObject;
            String temp, humi, gas, fire, dust, date, time;

            try {
                jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                if (jsonArray.length() == 0) {

                    Snackbar.make(getWindow().getDecorView().getRootView(), "데이터가 없습니다.", Snackbar.LENGTH_LONG)
                            .setAction("확인",null).show();

                } else {

                    while (cnt < 10) {

                        JSONObject object = jsonArray.getJSONObject(cnt);

                        temp = object.getString("temp");
                        humi = object.getString("humi");
                        gas = object.getString("gas");
                        fire = object.getString("fire");
                        dust = object.getString("dust");
                        date = object.getString("envdate");
                        time = object.getString("envtime");

                        environmentList.add(new Environment(temp, humi, gas, fire, dust, date, time));
                        cnt++;
                    }
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class BackgroundGetTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {

            try {
                target = "http://" + address + "/UserEnvDataList.php?envdate=" + URLEncoder.encode(userChooseDate, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String temp;
                StringBuilder stringBuilder = new StringBuilder();

                while ((temp = bufferedReader.readLine()) != null) {
                    Log.e(TAG, "temp: " + temp);
                    stringBuilder.append(temp + "\n");
                    Log.e(TAG, "stringBuilder: " + stringBuilder.toString());
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            int cnt = 0;
            JSONObject jsonObject;
            String temp, humi, gas, fire, dust, date, time;

            try {
                jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("response");

                Log.e(TAG, "jsonArray: " + jsonArray);
                Log.e(TAG, "jsonArray: " + jsonArray.length());

                if (jsonArray.length() == 0) {

                    Snackbar.make(getWindow().getDecorView().getRootView(), "데이터가 없습니다.", Snackbar.LENGTH_LONG)
                            .setAction("확인",null)
                            .show();
                } else {

                    recyclerView.setVisibility(View.VISIBLE);

                    while (cnt < 10) {
                        JSONObject object = jsonArray.getJSONObject(cnt);
                        temp = object.getString("temp");
                        humi = object.getString("humi");
                        gas = object.getString("gas");
                        fire = object.getString("fire");
                        dust = object.getString("dust");
                        date = object.getString("envdate");
                        time = object.getString("envtime");
                        environmentList.add(new Environment(temp, humi, gas, fire, dust, date, time));
                        cnt++;
                    }
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
