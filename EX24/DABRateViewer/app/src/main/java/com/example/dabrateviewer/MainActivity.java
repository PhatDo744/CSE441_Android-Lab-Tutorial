package com.example.dabrateviewer;

import androidx.appcompat.app.AppCompatActivity; // Sử dụng AppCompatActivity cho các tính năng hiện đại
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ListView lvTygia;
    TextView txtdate;
    ArrayList<Tygia> dstygia;
    MyArrayAdapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTygia = (ListView) findViewById(R.id.lv1); // ID trong activity_main.xml là lv1
        txtdate = (TextView) findViewById(R.id.txtdate);

        getdate();

        dstygia = new ArrayList<Tygia>();
        myadapter = new MyArrayAdapter(MainActivity.this, R.layout.item, dstygia);
        lvTygia.setAdapter(myadapter);

        // Tạo và thực thi AsyncTask
        TyGiaTask task = new TyGiaTask();
        task.execute();
    }

    // Phương thức lấy ngày giờ hệ thống
    public void getdate() {
        Date currentDate = Calendar.getInstance().getTime();
        // Format theo định dạng dd/MM/yyyy
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        // Hiển thị lên TextView
        txtdate.setText("Hôm Nay: " + simpleDate.format(currentDate));
    }

    // Lớp AsyncTask để thực hiện việc lấy dữ liệu từ API trong nền
    class TyGiaTask extends AsyncTask<Void, Void, ArrayList<Tygia>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (myadapter != null) { // Kiểm tra myadapter trước khi clear
                myadapter.clear();
            }
        }

        @Override
        protected ArrayList<Tygia> doInBackground(Void... params) {
            ArrayList<Tygia> ds = new ArrayList<Tygia>();
            try {
                // Đây là link Server
                URL url = new URL("http://dongabank.com.vn/exchange/export"); // Sử dụng HTTP nếu HTTPS có vấn đề chứng chỉ trên một số thiết bị/emulator cũ
                // Mở Connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // Thiết lập Method là Get dữ liệu
                connection.setRequestMethod("GET");
                // Thiết lập thuộc tính
                connection.setRequestProperty("Content-type", "application/json; charset=utf-8");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) "); // Khoảng trắng cuối có thể gây lỗi
                connection.setRequestProperty("Accept", "*/*");

                // Lấy chuỗi dữ liệu InputStream trả về
                InputStream is = connection.getInputStream();
                // Chuyển kiểu về UTF-8 và Đưa vào bộ đọc dữ liệu
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                // Lưu vào bộ đệm
                BufferedReader br = new BufferedReader(isr);
                String line = br.readLine();
                StringBuilder builder = new StringBuilder();
                while (line != null) {
                    builder.append(line);
                    line = br.readLine();
                }
                String json = builder.toString();

                // Bỏ hai ngoặc tròn trong dữ liệu trả về (nếu API thực sự trả về như vậy)
                // DongA Bank API hiện tại trả về JSON thuần, không có dấu ngoặc đơn bao quanh
                if (json.startsWith("(") && json.endsWith(")")) {
                    json = json.substring(1, json.length() - 1);
                }


                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("items");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    Tygia tiGia = new Tygia();
                    tiGia.setType(item.getString("type"));

                    if (item.has("imageurl")) {
                        tiGia.setImageurl(item.getString("imageurl"));
                        // Tải hình ảnh
                        try {
                            URL urlImage = new URL(tiGia.getImageurl());
                            HttpURLConnection imageConnection = (HttpURLConnection) urlImage.openConnection();
                            imageConnection.setRequestMethod("GET");
                            // Không cần User-Agent và Accept cho hầu hết các image URL đơn giản
                            // imageConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
                            // imageConnection.setRequestProperty("Accept", "*/*");
                            imageConnection.setDoInput(true); // Cần thiết cho việc đọc InputStream
                            imageConnection.connect(); // Thực hiện kết nối
                            InputStream imageIs = imageConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(imageIs);
                            tiGia.setBitmap(bitmap);
                            if (imageIs != null) imageIs.close(); // Đóng InputStream
                            imageConnection.disconnect(); // Ngắt kết nối
                        } catch (Exception e) {
                            Log.e("ImageLoadError", "Lỗi tải ảnh: " + tiGia.getImageurl() + " - " + e.toString());
                        }
                    }
                    // Kiểm tra sự tồn tại của key trước khi lấy giá trị
                    if (item.has("muatienmat")) {
                        tiGia.setMuatienmat(item.getString("muatienmat"));
                    } else {
                        tiGia.setMuatienmat(""); // Hoặc giá trị mặc định khác
                    }

                    if (item.has("muack")) {
                        tiGia.setMuack(item.getString("muack"));
                    } else {
                        tiGia.setMuack("");
                    }

                    if (item.has("bantienmat")) { // Sửa tên key
                        tiGia.setBantienmat(item.getString("bantienmat")); // Sửa tên key
                    } else {
                        tiGia.setBantienmat("");
                    }

                    if (item.has("banck")) {
                        tiGia.setBanck(item.getString("banck"));
                    } else {
                        tiGia.setBanck("");
                    }
                    ds.add(tiGia);
                }
                Log.d("JSON_DONGA", json); // Ghi log chuỗi JSON gốc
                if (is != null) is.close(); // Đóng InputStream chính
                connection.disconnect(); // Ngắt kết nối chính

            } catch (Exception ex) {
                Log.e("LoiTyGiaTask", ex.toString());
            }
            return ds;
        }

        @Override
        protected void onPostExecute(ArrayList<Tygia> result) {
            super.onPostExecute(result);
            if (myadapter != null && result != null) { // Kiểm tra null cho result
                myadapter.clear();
                myadapter.addAll(result);
                // myadapter.notifyDataSetChanged(); // addAll đã gọi notifyDataSetChanged()
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}