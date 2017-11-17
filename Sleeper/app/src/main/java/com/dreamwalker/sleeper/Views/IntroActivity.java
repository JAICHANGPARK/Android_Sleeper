package com.dreamwalker.sleeper.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.dreamwalker.sleeper.Intro2Fragment;
import com.dreamwalker.sleeper.IntroFragment;
import com.dreamwalker.sleeper.R;
import com.dreamwalker.sleeper.SampleSlide;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by 2E313JCP on 2017-10-18.
 */

public class IntroActivity extends AppIntro {

    private static final String TAG = "IntroActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPreferences pref = getSharedPreferences("ActivityIntro", Context.MODE_PRIVATE);
//        //만약 키값에서 얻어온 값이 false이면 액티비티를 실행하고
//        if (pref.getBoolean("activity_executed", false)) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            Log.e(TAG, "onCreate: " + pref.getBoolean("activity_executed", false));
//            finish();
//        } else {
//            SharedPreferences.Editor ed = pref.edit();
//            ed.putBoolean("activity_executed", true);
//            ed.commit();
//        }

        addSlide(SampleSlide.newInstance(R.layout.intro_slide_01));
        addSlide(SampleSlide.newInstance(R.layout.intro_slide_02));
        addSlide(IntroFragment.newInstance(R.layout.intro_slide_03));
        addSlide(Intro2Fragment.newInstance(R.layout.intro_slide_05)); // 와이파이 셋팅
        addSlide(SampleSlide.newInstance(R.layout.intro_slide_04));
        setFadeAnimation();
        //askForPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
