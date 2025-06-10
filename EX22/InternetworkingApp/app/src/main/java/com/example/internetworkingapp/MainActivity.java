package com.example.internetworkingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    Button btnParseXmlFromUrl;
    ListView lvData;
    ArrayList<String> dataItemList;
    ArrayAdapter<String> adapter;
    String xmlUrl = "https://api.androidhive.info/piazza/?format=xml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnParseXmlFromUrl = findViewById(R.id.btnParseXmlFromUrl);
        lvData = findViewById(R.id.lvData);
        dataItemList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataItemList);
        lvData.setAdapter(adapter);

        btnParseXmlFromUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchXmlTask().execute(xmlUrl);
            }
        });
    }

    private class FetchXmlTask extends AsyncTask<String, Void, ArrayList<String>> {
        private XmlDataParser xmlDataParser; // Thêm đối tượng parser

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show();
            dataItemList.clear();
            adapter.notifyDataSetChanged();
            xmlDataParser = new XmlDataParser(); // Khởi tạo parser
        }

        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            String urlString = urls[0];
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlString);
            InputStream inputStream = null;
            ArrayList<String> results = new ArrayList<>();

            try {
                HttpResponse response = httpClient.execute(httpGet);
                inputStream = response.getEntity().getContent();
                results = xmlDataParser.parseXml(inputStream); // Gọi phương thức parse từ lớp riêng

            } catch (IOException e) {
                Log.e("FetchXmlTask", "Lỗi khi tải dữ liệu: " + e.getMessage(), e);
                // results.add("Lỗi kết nối: " + e.getMessage()); // Có thể thêm thông báo lỗi
                return results; // Trả về danh sách (có thể rỗng hoặc chứa lỗi)
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("FetchXmlTask", "Lỗi khi đóng InputStream", e);
                    }
                }
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<String> resultData) {
            super.onPostExecute(resultData);
            if (resultData != null && !resultData.isEmpty()) {
                // Kiểm tra xem có thông báo lỗi cụ thể từ parser không (nếu có)
                // Ví dụ: if (resultData.get(0).startsWith("Lỗi")) { ... }
                dataItemList.addAll(resultData);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Tải dữ liệu thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Không có dữ liệu hoặc lỗi khi tải/phân tích.", Toast.LENGTH_LONG).show();
            }
        }
    }
}