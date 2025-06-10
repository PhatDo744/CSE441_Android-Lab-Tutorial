package com.example.vnexpressreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvNews;
    ArrayList<NewsItem> newsItemList;
    NewsAdapter adapter;
    String rssUrl = "https://vnexpress.net/rss/tin-moi-nhat.rss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvNews = findViewById(R.id.lvNews);
        newsItemList = new ArrayList<>();
        adapter = new NewsAdapter(this, R.layout.layout_listview_item, newsItemList);
        lvNews.setAdapter(adapter);

        new FetchRssTask().execute(rssUrl);

        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem selectedNews = newsItemList.get(position);
                if (selectedNews.getLink() != null && !selectedNews.getLink().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, SubActivity.class);
                    intent.putExtra("NEWS_URL", selectedNews.getLink());
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Không có link cho tin này.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class FetchRssTask extends AsyncTask<String, Void, ArrayList<NewsItem>> {
        private XMLNewsParser xmlNewsParser;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Đang tải tin tức...", Toast.LENGTH_SHORT).show();
            newsItemList.clear();
            xmlNewsParser = new XMLNewsParser();
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(String... urls) {
            String urlString = urls[0];
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlString);
            InputStream inputStream = null;
            ArrayList<NewsItem> results = new ArrayList<>();

            try {
                HttpResponse response = httpClient.execute(httpGet);
                inputStream = response.getEntity().getContent();
                results = xmlNewsParser.parseXml(inputStream);

            } catch (IOException e) {
                Log.e("FetchRssTask", "Lỗi khi tải dữ liệu RSS: " + e.getMessage(), e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("FetchRssTask", "Lỗi khi đóng InputStream", e);
                    }
                }
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsItem> resultData) {
            super.onPostExecute(resultData);
            if (resultData != null && !resultData.isEmpty()) {
                newsItemList.addAll(resultData);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Tải tin tức thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Không có tin tức hoặc lỗi khi tải.", Toast.LENGTH_LONG).show();
            }
        }
    }
}