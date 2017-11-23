package com.dreamwalker.sleeper.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.dreamwalker.sleeper.Adapter.MainDetailAdapter;
import com.dreamwalker.sleeper.Model.HeartRate;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailSleepActivity extends AppCompatActivity {

    private static final String TAG = "DetailSleepActivity";

    private boolean isOpen = false;

    RecyclerView recyclerView;
    //RecyclerView.LayoutManager layoutManager;
    MainDetailAdapter adapter;
    LinearLayoutManager layoutManager;

    FloatingActionButton floatingActionButton;
    ArrayList<HeartRate> heartRateList;

    public String date, address, port;
    private int pageNumber ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sleep);

        setTitle("Detail Information");
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

        final Intent intent = getIntent();
        //이전페이지에서 데이터 값을 가져옴.
        date = intent.getStringExtra("date"); // date 값 처리하기? 널 포인트 뜸.
        Log.e(TAG, "date 데이터 가져욤: " +  date);

        // TODO: 2017-11-16 null 처리 하기. 
        if (date == null){

            // TODO: 2017-11-17 문제점 발견 : 당일날 데이터를 가져오기 떄문에 계속 빈칸이 었음.
            // TODO: 2017-11-16 현재시간을 가져와야 합니다.
            long now = System.currentTimeMillis();
            Date dateNow = new Date(now);

            SimpleDateFormat dateYear = new SimpleDateFormat("yyyy");
            SimpleDateFormat dateMonth = new SimpleDateFormat("MM");
            SimpleDateFormat dateDay = new SimpleDateFormat("dd");

            String year = dateYear.format(dateNow);
            String Month = dateMonth.format(dateNow);
            String day = dateDay.format(dateNow);

            year = year.substring(2);
            date = year + "-" + Month + "-" + day;
            Log.e(TAG, "date 데이터가 널값이라 데이터 값 가져욤: " +  date);
        }

        pageNumber = intent.getIntExtra("page", 0);
        Log.e(TAG, "pageNumber: " + pageNumber );

        heartRateList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.detail_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainDetailAdapter(this, heartRateList);
        recyclerView.setAdapter(adapter);

        // TODO: 2017-11-16 리사이클러뷰에서 구분선을 생성하는 방법.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // TODO: 2017-11-16 리사이클러뷰 스크롤시 플로팅 액션버튼을 제거하고 생성하는 리스너를 생성해야한다. 
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });

        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailSleepActivity.this, DetailGraphActivity.class);
                intent1.putParcelableArrayListExtra("detail",heartRateList);
                intent1.putExtra("page", pageNumber);
                intent1.putExtra("date",date);
                startActivity(intent1);
                finish();
            }
        });

        // TODO: 2017-11-16 프레그먼트에서 넘어오는 페이지 넘버에 따른 테스크 처리를 달리했다.
        if (pageNumber == 0){
            new BackgroundGetTask().execute();
        }else if(pageNumber == 1){
            new DetailSpo2Task().execute();
        }else if (pageNumber == 2){
            new DetailSoundTask().execute();
        }else if (pageNumber == 10){
            finish();
        }
    }
  /*  private void viewGraph(){

        if(isOpen){
            int x = layoutList.getRight();
            int y = layoutList.getBottom();
            Log.e(TAG, "viewGraph: " + " x : " + x + " y : " + y );
            int StartRadius = 0;
            int endRadius = (int)Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());

            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), android.R.color.white,null)));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(layoutGraph, x, y,StartRadius,endRadius);
                layoutGraph.setVisibility(View.VISIBLE);
                animator.start();
            }
            isOpen = true;
        }

    }*/

    class BackgroundGetTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://" + address + "/UserHRDetail.php?maindate=" + URLEncoder.encode(date, "UTF-8");
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
                    //Log.e(TAG, "temp: " + temp);
                    stringBuilder.append(temp + "\n");
                    //Log.e(TAG, "stringBuilder: " + stringBuilder.toString());
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
            String heartrate, date, time;

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
                    while (cnt  < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(cnt);
                        heartrate = object.getString("heartrate");
                        date = object.getString("maindate");
                        time = object.getString("maintime");
                        heartRateList.add(new HeartRate(heartrate, date, time));
                        cnt++;
                    }
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class DetailSpo2Task extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://" + address + "/UserSpo2Detail.php?maindate=" + URLEncoder.encode(date, "UTF-8");
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
                    //Log.e(TAG, "temp: " + temp);
                    stringBuilder.append(temp + "\n");
                    //Log.e(TAG, "stringBuilder: " + stringBuilder.toString());
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
            String spo2, date, time;

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
                    while (cnt  < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(cnt);
                        spo2 = object.getString("spo2");
                        date = object.getString("maindate");
                        time = object.getString("maintime");
                        heartRateList.add(new HeartRate(spo2, date, time));
                        cnt++;
                    }
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class DetailSoundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://" + address + "/UserSoundDetail.php?maindate=" + URLEncoder.encode(date, "UTF-8");
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
                    //Log.e(TAG, "temp: " + temp);
                    stringBuilder.append(temp + "\n");
                    //Log.e(TAG, "stringBuilder: " + stringBuilder.toString());
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
            String sound, date, time;

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
                    while (cnt  < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(cnt);
                        sound = object.getString("sound");
                        date = object.getString("maindate");
                        time = object.getString("maintime");
                        heartRateList.add(new HeartRate(sound, date, time));
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
