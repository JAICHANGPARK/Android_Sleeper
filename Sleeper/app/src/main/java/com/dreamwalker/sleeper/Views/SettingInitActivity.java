package com.dreamwalker.sleeper.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamwalker.sleeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingInitActivity extends AppCompatActivity {
    private static final String TAG = "SettingInitActivity";
    @BindView(R.id.settingButton)
    Button settingButton;
    @BindView(R.id.input_address)
    EditText edtAddress;
    @BindView(R.id.input_port)
    EditText edtPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_init);

        ButterKnife.bind(this);

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
    }

    @OnClick(R.id.settingButton)
    public void doneSetting(View v) {
        if (edtAddress.getText().toString().equals("") && edtPort.getText().toString().equals("")) {
            Snackbar.make(v, "주소와 포트를 정확하게 입력해주세요", Snackbar.LENGTH_LONG).show();
        } else if (edtAddress.getText().toString().isEmpty()) {
            Snackbar.make(v, "주소 입력해주세요", Snackbar.LENGTH_LONG).show();
        } else if (edtPort.getText().toString().isEmpty()) {
            Snackbar.make(v, "포트 주소를 입력해주세요", Snackbar.LENGTH_LONG).show();
        } else if (!checkIp(edtAddress.getText().toString())) {
            Snackbar.make(v, "정확한 형식의 주소를 입력해주세요.", Snackbar.LENGTH_LONG).show();
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

    /**
     *
     * 정확한 형식의 아이피 주소를 받기위한 정규표현식 확인 함수입니다.
     * 안드로이드 api8 이상 부터 ip 주소를 확인해주는 함수가 생성되어
     * 쉽게 사용이 가능했습니다.
     *
     * @param ip
     * @return boolean. ( 정확한 형식이면 true 리턴 )
     *
     */
    public static boolean checkIp(String ip) {
        boolean isCorrect = Patterns.IP_ADDRESS.matcher(ip).matches();
        return isCorrect;
    }
}
