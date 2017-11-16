package com.dreamwalker.sleeper.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamwalker.sleeper.Adapter.HomeAdapter;
import com.dreamwalker.sleeper.Model.Home;
import com.dreamwalker.sleeper.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    public  String address, port, userID;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private NavigationView navigationView;

    //private TextView userNameText;

    private boolean loginCheck = false;

    private BottomBar bottomBar;
    private BottomBarTab bottomBarTab;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HomeAdapter adapter;
    List<Home> homeList;
    List<Home> homeCacheList;

    AlertDialog dialog;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: 2017-11-09 세로모드 고정 
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // TODO: 2017-11-09 툴바 설정 및 인플레이트 
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: 2017-11-15 서버의 주소를 가져오는 부분. 
        SharedPreferences sharedPreferences = getSharedPreferences("SettingInit", Context.MODE_PRIVATE);
        address = sharedPreferences.getString("addressInit", "");
        port = sharedPreferences.getString("portInit", "");
        // TODO: 2017-11-15 만약 서버와 포트 값이 없다면 설정 화면으로 가도록 해야한다.
        if (!address.equals("") && !port.equals("")) {
            Log.e(TAG, "주소및포트값: " + "정상적으로 가져옴");

        } else {
            Log.e(TAG, "주소및포트값: " + "가져오지 못함.");
            Toast.makeText(this, "서버의 주소와 포트를 설정해 주세요.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }


        // TODO: 2017-11-15 recyclerView 초기화
        homeList = new ArrayList<>();
        homeCacheList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.home_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new HomeAdapter(homeList, getBaseContext());
        //recyclerView.setAdapter(adapter);

        // TODO: 2017-11-15 spot dialog init
        dialog = new SpotsDialog(this);

        // TODO: 2017-11-09 NoSQl PAPER LIB 초기화 
        Paper.init(this);

        // TODO: 2017-10-24 Navigation Drawer.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: 2017-10-24 Navigation 뷰 
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TODO: 2017-11-09 Bottom Bar inflate 
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        // TODO: 2017-10-24 네비게이션 드로우워의 해더 뷰 부분의 아이디를 설정하는 부분.
        View headerView = navigationView.getHeaderView(0);
        TextView userNameText = (TextView) headerView.findViewById(R.id.userName);
        //userNameText = (TextView)findViewById(R.id.userName);
        //userNameText.setText(userID);

        // TODO: 2017-11-09 swip 리프레시
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipRefreshmain);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHomeSource(true);

            }
        });

        // TODO: 2017-11-09 FloatingActionButton 초기설정 및 리스너 정의 

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_comment_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, TalkActivity.class);
                startActivity(intent);
            }
        });

        // TODO: 2017-11-09 인텐트에서 받아온 정보를 받아오고 이를 처리하는 부분
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        Log.e(TAG, "userID: " + userID);

        if (userID != null) {
            loginCheck = true;
            userNameText.setText(userID);
        } else {
            loginCheck = false;
            userNameText.setText("Guest");
        }

        // TODO: 2017-11-09 bottom Bar 리스터 설정.

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int i) {

                Log.e(TAG, "onTabSelected: " + i);
                if (i == R.id.tab_sleep) {

                    bottomBarTab = bottomBar.getTabWithId(R.id.tab_home);
                    bottomBarTab.setBadgeCount(5);

                    Intent intent = new Intent(MainActivity.this, SleepMainActivity.class);
                    startActivity(intent);

                } else if (i == R.id.tab_environment) {

                    Intent intent = new Intent(MainActivity.this, EnvironmentActivity.class);
                    startActivity(intent);

                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int i) {
                Log.e(TAG, "onTabReSelected: " + i);
                if (i == R.id.tab_home) {
                    // bottomBarTab.removeBadge();
                }
                if (i == R.id.tab_sleep) {
                    bottomBarTab.removeBadge();
                }
            }
        });

        loadHomeSource(false);
    }

    /**
     * 리프레시를 위한 함수
     *
     * @param isRefreshed
     */
    public void loadHomeSource(boolean isRefreshed) {

        if (!isRefreshed) {
            homeCacheList = Paper.book("home").read("cache");
            Log.e(TAG, "loadHomeSource: " + homeCacheList);

            if (homeCacheList != null && !homeCacheList.isEmpty()) {
                Log.e(TAG, "loadHomeSource: 캐쉬 리스트 로딩 ");
                adapter = new HomeAdapter(homeCacheList, getBaseContext());
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

            } else {
                dialog.show();
                new BackgroundTask().execute();
                adapter = new HomeAdapter(homeList, getBaseContext());
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
                Paper.book("home").write("cache", homeList);

            }
        } else {
            Log.e(TAG, "loadHomeSource: 리프레쉬 ");

            new BackgroundTask().execute();

            adapter = new HomeAdapter(homeList, getBaseContext());

            recyclerView.setAdapter(adapter);

            Paper.book("home").write("cache", homeList);
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (loginCheck) {
            menu.findItem(R.id.action_Login).setVisible(false);
        } else {
            menu.findItem(R.id.action_Logout).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_Login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_Logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        // TODO: 2017-10-27 Page 변수는 네비게이션 드러우어에서 눌린 아이템이  SleepMainActivity의 탭으로 바로 이동하기 위한 변수이다.
        int page;

        switch (id) {
            case R.id.nav_summary:
                break;
            case R.id.nav_heartrate:
                intent = new Intent(MainActivity.this, SleepMainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_spo2:
                page = 1;
                // TODO: 2017-10-27  SleepMainActivity로 표시할 Tablayout의 index를 넘겨준다.
                intent = new Intent(MainActivity.this, SleepMainActivity.class);
                intent.putExtra("One", page);
                startActivity(intent);
                break;
            case R.id.nav_snoring:
                page = 2;
                intent = new Intent(MainActivity.this, SleepMainActivity.class);
                intent.putExtra("One", page);
                startActivity(intent);
                break;

            case R.id.nav_environment:
                intent = new Intent(MainActivity.this, EnvironmentActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_share:
                Toast.makeText(this, "아직 구현되지 않았어요 ㅠㅠ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_Setting:
                intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_aboutUs:
                intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                //Toast.makeText(this, "아직 구현되지 않았어요 ㅠㅠ", Toast.LENGTH_SHORT).show();

                break;
        }

//        if (id == R.id.nav_summary) {
//            // Handle the camera action
//        } else if (id == R.id.nav_heartrate) {
//
//        } else if (id == R.id.nav_spo2) {
//
//        } else if (id == R.id.nav_snoring) {
//
//        } else if (id == R.id.nav_environment) {
//
//        } else if (id == R.id.nav_Setting) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    // TODO: 2017-11-09 뒤로가기 두번 눌러 종료 하기. 
    private long lastTimeBackPressed;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 눌러 종료합니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        // TODO: 2017-10-12 해당 웹서버에 접속할수있게 해줌.
        @Override
        protected void onPreExecute() {
            try {

                target = "http://" + address + "/UserHome.php";

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
            String hr, spo2, sound, temp, humi;

            try {
                jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                if (jsonArray.length() == 0) {

                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    JSONObject object = jsonArray.getJSONObject(cnt);
                    if (object == null){
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        hr = object.getString("heartrate");
                        spo2 = object.getString("spo2");
                        sound = object.getString("sound");
                        temp = object.getString("temp");
                        humi = object.getString("humi");

                        homeList.clear();
                        homeList.add(new Home("심박수", hr + " BPM"));
                        homeList.add(new Home("산소포화도", spo2 + "%"));
                        homeList.add(new Home("코골이", sound));
                        homeList.add(new Home("실내온도", temp + " C"));
                        homeList.add(new Home("실내습도", humi + " %"));
                        adapter.notifyDataSetChanged();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
