package com.dreamwalker.sleeper.Views;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dreamwalker.sleeper.R;

import dmax.dialog.SpotsDialog;

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        alertDialog = new SpotsDialog(this);
        alertDialog.show();

        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                alertDialog.dismiss();
            }
        });

        if (getIntent() != null) {
            if (!getIntent().getStringExtra("webURL").isEmpty()) {
                webView.loadUrl(getIntent().getStringExtra("webURL"));
            }
        }
    }
}
