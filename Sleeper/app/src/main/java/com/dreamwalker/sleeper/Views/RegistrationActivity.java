package com.dreamwalker.sleeper.Views;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.dreamwalker.sleeper.R;
import com.dreamwalker.sleeper.Utils.RegisterRequest;
import com.dreamwalker.sleeper.Utils.ValidateRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    //@BindView(R.id.toolbar)
    //Toolbar toolbar;

    //@BindView(R.id.rl_bottom_sheet)
    //RelativeLayout rlBottomSheet;

    @BindView(R.id.otherLogin)
    TextView otherLogin;

    @BindView(R.id.userName)
    EditText idText;

    @BindView(R.id.password)
    EditText passwordText;

    @BindView(R.id.email)
    EditText emailText;

    @BindView(R.id.registerButton)
    FButton registerButton;

    @BindView(R.id.vaildateButton)
    FButton vaildateButton;

    private AlertDialog dialog;

    private boolean validate = false; //아이디 중복검사 변수


    //private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle("Sleeper"); // appbar의 빈 공간의 타이틀을 채워줌
        //setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("regi_01.json");
        animationView.loop(true);
        animationView.playAnimation();

//        fab.setVisibility(View.GONE);
//        fab.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

//        bottomSheetBehavior = BottomSheetBehavior.from(rlBottomSheet);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_EXPANDED:
//                        setTitle("Other Login");
//                        break;
//
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        fab.setVisibility(View.GONE);
//                        break;
//
//                    default:
//                        setTitle("Sleeper");
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                // Slide ...
//                // 1이면 완전 펼쳐진 상태
//                // 0이면 peekHeight인 상태
//                // -1이면 숨김 상태
//                Log.i("TAG", "slideOffset " + slideOffset);
//
//            }
//        });
    }

    @OnClick(R.id.otherLogin)
    public void OnOtherLogin(View view) {
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        fab.setVisibility(View.VISIBLE);
    }
//

//    @OnClick(R.id.fab)
//    public void OnFab(View view){
//
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//    }


    @OnClick(R.id.registerButton)
    public void onRegisterButton(View view) {

        String userID = idText.getText().toString();
        String userPassword = passwordText.getText().toString();
        String userEmail = emailText.getText().toString();

        if (!validate) {

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
            dialog = builder.setMessage("먼저 중복체크를 해주세요.").setNegativeButton("확인", null)
                    .create();
            dialog.show();
            return;
        }

        if (userID.equals("") || userPassword.equals("") || userEmail.equals("")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
            dialog = builder.setMessage("빈 칸 없이 입력해주세요.").setNegativeButton("확인", null)
                    .create();
            dialog.show();
            return;
        }
        if (userPassword.length() < 8) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
            dialog = builder.setMessage("보안을 높이기 위해 8자리 이상의 비밀번호로 구성해주세요").setNegativeButton("확인", null)
                    .create();
            dialog.show();
            return;
        }
        if (!checkEmail(userEmail)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
            dialog = builder.setMessage("정확한 이메일을 입력해주세요").setNegativeButton("확인", null)
                    .create();
            dialog.show();
            return;
        }
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    Log.e(TAG, "jsonResult: " + jsonObject);
                    Log.e(TAG, "sucesse: " + success);

                    if (success) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        dialog = builder.setMessage("회원 등록에 성공했습니다.").setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        finish(); // 회원가입 성공후 창 닫음.
                    } else {
                        // TODO: 2017-10-12 중복 체크에 실패 했다면.
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        dialog = builder.setMessage("회원 등록에 실패했습니다.").setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userEmail, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        queue.add(registerRequest);
    }

    @OnClick(R.id.vaildateButton)
    public void onValidateButton(View view) {

        String userID = idText.getText().toString();

        if (validate) {
            return;
        }
        if (userID.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
            dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다.").setPositiveButton("확인", null)
                    .create();
            dialog.show();
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    Log.e(TAG, "jsonResult: " + jsonObject);
                    Log.e(TAG, "sucesse: " + success);

                    if (success) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        dialog = builder.setMessage("사용할 수 있는 아이디입니다..").setPositiveButton("확인", null)
                                .create();
                        dialog.show();

                        validate = true;

                        idText.setEnabled(false);
                        vaildateButton.setEnabled(false);

                        vaildateButton.setBackgroundColor(getResources().getColor(R.color.fbutton_color_emerald));

                    } else {
                        // TODO: 2017-10-12 중복 체크에 실패 했다면.
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        dialog = builder.setMessage("사용할 수 없는 아이디입니다..").setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        queue.add(validateRequest);

    }

    @Override
    public void onBackPressed() {
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 이메일 포맷 체크
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }

    public static boolean checkIp(String ip) {
        boolean isCorrect = Patterns.IP_ADDRESS.matcher(ip).matches();
        return isCorrect;
    }
}
