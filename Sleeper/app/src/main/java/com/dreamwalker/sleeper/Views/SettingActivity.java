package com.dreamwalker.sleeper.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamwalker.sleeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity";

    private FloatingActionButton fab;

    @BindView(R.id.input_address)
    EditText edtAddress;

    @BindView(R.id.input_port)
    EditText edtPort;

    @BindView(R.id.submitButton)
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Settings");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this); // 뷰 바인드

        SharedPreferences sharedPreferences = getSharedPreferences("SettingInit", Context.MODE_PRIVATE);
        String address = sharedPreferences.getString("addressInit", "");
        String port = sharedPreferences.getString("portInit", "");
        Log.e(TAG, "addressInit: " + address);
        Log.e(TAG, "portInit: " + port);

        if (!address.equals("") && !port.equals("")) {
            edtAddress.setText(address);
            edtPort.setText(port);
            Log.e(TAG, "portInit: " + "실행?");
        } else {
            edtAddress.setText("");
            edtPort.setText("");
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                String[] address = {"itsmejeffrey.dev@gmail.com"};    //이메일 주소 입력
                emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "앱 관련 문의");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "앱 관련 문의 드립니다. 하단에 문제의 내용을 추가해주세요 ");
                startActivity(emailIntent);
            }
        });

    }

    @OnClick(R.id.submitButton)
    void click(View v) {

//        Snackbar.make(getWindow().getDecorView().getRootView(), "설정 완료", Snackbar.LENGTH_LONG).show();
//
//        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
//        intent.putExtra("address", edtAddress.getText().toString());
//        intent.putExtra("port", edtPort.getText().toString());
//        startActivity(intent);

        if (edtAddress.getText().toString().equals("") && edtPort.getText().toString().equals("")) {
            Snackbar.make(v, "주소와 포트를 정확하게 입력해주세요", Snackbar.LENGTH_LONG).show();
        } else {

            String addressInit = edtAddress.getText().toString();
            String portInit = edtPort.getText().toString();

            // 버튼을 누르면 SharedPreferences에 값을 저장하도록 하자..
            SharedPreferences pref = getSharedPreferences("SettingInit", Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();
            ed.putString("addressInit", addressInit);
            ed.putString("portInit", portInit);
            ed.commit();

            Log.e(TAG, "addressInit: " + addressInit);
            Log.e(TAG, "portInit: " + portInit);

            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

}
