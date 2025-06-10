package com.example.jsonparserapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnParseJson;
    ListView lvJsonData;
    ArrayList<String> dataList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnParseJson = findViewById(R.id.btnParseJson);
        lvJsonData = findViewById(R.id.lvJsonData);
        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        lvJsonData.setAdapter(adapter);

        btnParseJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseJsonData();
            }
        });
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("products.json"); // Đảm bảo file này có trong thư mục assets
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void parseJsonData() {
        dataList.clear(); // Xóa dữ liệu cũ
        String jsonString = loadJSONFromAsset();
        if (jsonString != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonString);

                // Lấy MaDM và TenDM
                String maDM = jsonObj.getString("MaDM");
                String tenDM = jsonObj.getString("TenDM");
                dataList.add(maDM);
                dataList.add(tenDM);

                // Lấy danh sách sản phẩm
                JSONArray sanphams = jsonObj.getJSONArray("Sanphams");

                // Duyệt qua từng sản phẩm
                for (int i = 0; i < sanphams.length(); i++) {
                    JSONObject sp = sanphams.getJSONObject(i);

                    String maSP = sp.getString("MaSP");
                    String tenSP = sp.getString("TenSP");
                    int soLuong = sp.getInt("Soluong");
                    long donGia = sp.getLong("DonGia"); // Sử dụng long cho đơn giá lớn
                    long thanhTien = sp.getLong("ThanhTien"); // Sử dụng long cho thành tiền lớn
                    String hinh = sp.getString("Hinh");

                    dataList.add(maSP + " - " + tenSP);
                    dataList.add(soLuong + " * " + donGia + " = " + thanhTien);
                    dataList.add(hinh);
                }

                adapter.notifyDataSetChanged(); // Cập nhật ListView

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi parse JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Không thể đọc file JSON từ assets", Toast.LENGTH_LONG).show();
        }
    }
}