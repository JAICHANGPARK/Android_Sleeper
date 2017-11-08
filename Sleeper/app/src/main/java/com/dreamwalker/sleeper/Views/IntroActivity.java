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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(SampleSlide.newInstance(R.layout.intro_slide_01));
        addSlide(SampleSlide.newInstance(R.layout.intro_slide_02));
        addSlide(IntroFragment.newInstance(R.layout.intro_slide_03));
        addSlide(Intro2Fragment.newInstance(R.layout.intro_slide_05)); // 와이파이 셋팅
        addSlide(SampleSlide.newInstance(R.layout.intro_slide_04));
        setFadeAnimation();
//        askForPermissions(new String[]{Manifest.permission.CAMERA},1);

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
