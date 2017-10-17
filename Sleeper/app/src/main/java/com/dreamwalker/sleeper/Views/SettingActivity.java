package com.dreamwalker.sleeper.Views;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamwalker.sleeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    @BindView(R.id.input_address)
    EditText address;

    @BindView(R.id.input_port)
    EditText port;

    @BindView(R.id.submitButton)
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Settings");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this); // 뷰 바인드


        fab = (FloatingActionButton)findViewById(R.id.fab);

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

    @OnClick(R.id.submitButton) void click(){
        Snackbar.make(getWindow().getDecorView().getRootView(), "설정 완료", Snackbar.LENGTH_LONG).show();

        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        intent.putExtra("address", address.getText().toString());
        intent.putExtra("port", port.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

}
