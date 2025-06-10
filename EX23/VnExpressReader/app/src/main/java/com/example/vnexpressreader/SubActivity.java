package com.example.vnexpressreader;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {

    WebView webViewNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        webViewNews = findViewById(R.id.webViewNews);
        webViewNews.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();
        String newsUrl = intent.getStringExtra("NEWS_URL");

        if (newsUrl != null && !newsUrl.isEmpty()) {
            webViewNews.loadUrl(newsUrl);
        } else {
            webViewNews.loadData("<html><body><h1>Lỗi: Không tìm thấy URL.</h1></body></html>", "text/html", "UTF-8");
        }
    }
}