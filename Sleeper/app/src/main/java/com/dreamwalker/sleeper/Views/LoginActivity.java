package com.dreamwalker.sleeper.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.dreamwalker.sleeper.R;
import com.dreamwalker.sleeper.Utils.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextView signup;

    @BindView(R.id.nextButton)
    Button skipButton;
    
    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.userName)
    EditText idText;

    @BindView(R.id.password)
    EditText passwordText;

    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = (TextView)findViewById(R.id.signup);
        ButterKnife.bind(this);

        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("login_02.json");
        animationView.loop(true);
        animationView.playAnimation();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.nextButton)
    public void onSkip(View view){
        startActivity(new Intent(LoginActivity.this, IntroActivity.class));
    }
    
    @OnClick(R.id.loginButton)
    public void onLogin(View view){

        final String userID = idText.getText().toString(); // EditText에서 값을 받아옴.
        String userPassword = passwordText.getText().toString();

        Log.e(TAG, "userID: " + userID);
        Log.e(TAG, "userPassword: " + userPassword);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    Log.e(TAG, "jsonResult: " + jsonResponse);
                    Log.e(TAG, "success: " + success);

                    if (success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("로그인에 성공했습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userID", userID);
                        LoginActivity.this.startActivity(intent);
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("로그인에 실패했습니다. 계정을 확인하세요")
                                .setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
