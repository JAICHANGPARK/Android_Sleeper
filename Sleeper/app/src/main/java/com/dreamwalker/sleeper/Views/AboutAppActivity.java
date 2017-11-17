package com.dreamwalker.sleeper.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dreamwalker.sleeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutAppActivity extends AppCompatActivity {

    @BindView(R.id.GithubButton)
    Button githubButton;

    @BindView(R.id.qiitaButton)
    Button qiitaButton;

    @BindView(R.id.instagramButton)
    Button instagramButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        setTitle("Yes! Its Me Dreamwalker");
        ButterKnife.bind(this);
    }

    @OnClick(R.id.GithubButton)
    void onGithubClicked(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("webURL", "https://github.com/JAICHANGPARK");
        startActivity(intent);
    }

    @OnClick(R.id.qiitaButton)
    void onQittaClicked(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("webURL", "https://qiita.com/Dreamwalker");
        startActivity(intent);
    }

    @OnClick(R.id.instagramButton)
    void onInstaClicked(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("webURL", "https://www.instagram.com/itsmyowndreamwalker/");
        startActivity(intent);
    }
}
