package com.dreamwalker.sleeper.Views;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dreamwalker.sleeper.Adapter.NewsAdapter;
import com.dreamwalker.sleeper.Model.News;
import com.dreamwalker.sleeper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity{
    private static final String TAG = "NewsActivity";
    private static final String clientId = "OsRhsGAxwvistG8jbiXW"; //애플리케이션 클라이언트 아이디값";
    private static final String clientSecret = "t9LwvBQmfb"; //애플리케이션 클라이언트 시크릿값";

    @BindView(R.id.newsRecyclerView)
    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;
    NewsAdapter adapter;
    List<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setTitle("News");
        ButterKnife.bind(this);
        newsList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new NewsAdapter(this, newsList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), layoutManager.getOrientation()
        );

        recyclerView.addItemDecoration(dividerItemDecoration);

        new NewsTask().execute();
    }

    class NewsTask extends AsyncTask<Void, Void, String> {

        String apiURL;

        // TODO: 2017-11-16 네이버 뉴스 api를 사용하여 코골이에 대한 뉴스 검색 결과를 얻고자 했다.
        @Override
        protected void onPreExecute() {
            try {

                apiURL = "https://openapi.naver.com/v1/search/news.json?query="
                        + URLEncoder.encode("코골이", "UTF-8")
                        + "&display=10"; // json 결과

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL(apiURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("X-Naver-Client-Id", clientId);
                httpURLConnection.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                int responseCode = httpURLConnection.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
                }

                //InputStream inputStream = httpURLConnection.getInputStream();
                //BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                //StringBuffer response = new StringBuffer();
                while ((temp = br.readLine()) != null) {
                    Log.e(TAG, "temp: " + temp);
                    stringBuilder.append(temp + "\n");
                    Log.e(TAG, "stringBuilder: " + stringBuilder.toString());
                }
                br.close();
                //inputStream.close();
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
            String title, originallink, link, description, pubDate;
            s = s + "}]}";
            try {
                jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                Log.e(TAG, "jsonArray: " + jsonArray);
                Log.e(TAG, "jsonArray: " + jsonArray.length());

                if (jsonArray.length() == 0) {

                    Intent intent = new Intent(NewsActivity.this, SettingActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    while (cnt < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(cnt);
                        title = object.getString("title");
                        originallink = object.getString("originallink");
                        link = object.getString("link");
                        pubDate = object.getString("pubDate");
                        description = object.getString("description");

                        News news = new News(title, originallink, description, pubDate);
                        newsList.add(news);

                        Log.e(TAG, "title: " + title);
                        Log.e(TAG, "originallink: " + originallink);
                        Log.e(TAG, "link: " + link);
                        Log.e(TAG, "pubDate: " + pubDate);
                        Log.e(TAG, "description: " + description);
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
